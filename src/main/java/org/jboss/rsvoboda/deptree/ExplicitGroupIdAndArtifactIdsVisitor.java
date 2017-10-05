/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat Middleware LLC, and individual contributors
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Explicit GroupId and ArtifactIds visitor
 * @author Rostislav Svoboda
 */
public class ExplicitGroupIdAndArtifactIdsVisitor implements Visitor {
    private final String groupId;
    private final String[] artifactIds;
    private int groupIdOccurrenceCounter;
    private Map<String, Boolean> matchFoundMap;

    public ExplicitGroupIdAndArtifactIdsVisitor(String groupId, String... artifactIds) {
        this.groupId = groupId;
        this.artifactIds = artifactIds;
        matchFoundMap = new HashMap<>(artifactIds.length);
        groupIdOccurrenceCounter = 0;
    }

    
    @Override
    public void visit(Node node) throws VisitException {

        if (node.getGroupId().equals(groupId)) {
            groupIdOccurrenceCounter ++;

            for (String artifactId : artifactIds) {
                if (node.getArtifactId().equals(artifactId)) {
                    matchFoundMap.put(artifactId, true);
                }
            }
        }
        for (Node child : node.getChildNodes()) {
            visit(child);
        }

        // not all expected artifacts are found or more artifacts with defined groupId are found
        if (node.getParent() == null && ( matchFoundMap.size() != artifactIds.length || groupIdOccurrenceCounter != artifactIds.length)) {
            throw new VisitException("Dependency tree does not contain expected GroupId:ArtifactIds '" + groupId + " : " + Arrays.toString(artifactIds) +
                    " :: found GroupId:ArtifactIds: " + matchFoundMap.size() + ",  found artifacts with defined GroupId: " + groupIdOccurrenceCounter);
        }
    }

}
