/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.rsvoboda.deptree;

/**
 *
 * @author Rostislav Svoboda
 */
public class ExpectedGroupIdAndArtifactIdVisitor implements Visitor {
    private final String groupId;
    private final String artifactId;
    private boolean substringSearch = false;
    private boolean matchFound = false;

    public ExpectedGroupIdAndArtifactIdVisitor(String groupId, String artifactId) {
        this(groupId, artifactId, false);
    }
    
    public ExpectedGroupIdAndArtifactIdVisitor(String groupId, String artifactId, boolean substringSearch) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.substringSearch = substringSearch;
    }

    
    @Override
    public void visit(Node node) throws VisitException {
        
        if (substringSearch && node.getGroupId().contains(groupId) && node.getArtifactId().contains(artifactId)) {
            matchFound = true;
        } else if (node.getGroupId().equals(groupId) && node.getArtifactId().equals(artifactId)) {
            matchFound = true;
        } 
        for (Node child : node.getChildNodes()) {
            visit(child);
        }
        if (node.getParent() == null && !matchFound) {
            throw new VisitException("Dependency tree does not contain GroupId:ArtifactId '" + groupId + ":" + artifactId +"', substringSearch " + substringSearch);
        }
    }

}
