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
public class UnexpectedGroupIdVisitor implements Visitor {
    private final String groupId;
    private boolean substringSearch = false;

    public UnexpectedGroupIdVisitor(String groupId) {
        this.groupId = groupId;
    }
    
    public UnexpectedGroupIdVisitor(String groupId, boolean substringSearch) {
        this.groupId = groupId;
        this.substringSearch = substringSearch;
    }

    
    @Override
    public void visit(Node node) throws VisitException {
        
        if (substringSearch && node.getGroupId().contains(groupId)) {
            throw new VisitException("Dependency tree contains unexpected GroupId searching for '" + groupId + "' substring");
        } else if (node.getGroupId().equals(groupId)) {
            throw new VisitException("Dependency tree contains unexpected GroupId searching for '" + groupId + "' string");
        } 
        for (Node child : node.getChildNodes()) {
            visit(child);
        }

    }

}
