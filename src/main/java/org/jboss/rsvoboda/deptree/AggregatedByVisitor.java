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

import java.util.*;

/**
 * Get aggregated details based on GAV of the artifacts
 * Aggregation is defined by {@link Aggregator} enum
 * Details are printed via {@link #toString()} method
 *
 * @author Rostislav Svoboda
 */
public class AggregatedByVisitor implements Visitor {

    private Aggregator aggregator;
    private Map<String,SortedSet<String>> aggregatedMap = new TreeMap<>();

    /**
     * GAV aggregation enum
     */
    public enum Aggregator {
        GROUP_ID, ARTIFACT_ID, VERSION
    }

    public AggregatedByVisitor(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public void visit(Node node) throws VisitException {

        String key = null;
        switch (aggregator) {
            case GROUP_ID:
                key = node.getGroupId();
                break;
            case VERSION:
                key = node.getVersion();
                break;
            case ARTIFACT_ID:
                key = node.getArtifactId();
                break;
        }

        SortedSet<String> gavSet = aggregatedMap.getOrDefault(key, new TreeSet<>());
        gavSet.add(node.getArtifactCanonicalForm());
        aggregatedMap.put(key, gavSet);

        for (Node child : node.getChildNodes()) {
            visit(child);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Formatter formatter = new Formatter(sb);
        aggregatedMap.forEach((k,v)-> formatter.format("%1$-5d %2$-45s %3$s %n", v.size(), k, v));
        return sb.toString();
    }
}
