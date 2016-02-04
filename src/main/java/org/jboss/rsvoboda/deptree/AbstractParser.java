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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParser implements Parser {

    /**
     * Parses a string representing a Maven artifact in standard notation.
     * @param artifact
     * @return an instance of {@link Node} representing the artifact.
     */
    protected Node parseArtifactString(final String artifact) {
        final List<String> tokens = new ArrayList<String>(7);
        int tokenStart = 0;
        boolean tokenStarted = false;
        boolean hasDescription = false;
        boolean omitted = false;
        int tokenEnd = 0;
        for (; tokenEnd < artifact.length(); tokenEnd++) {
            final char c = artifact.charAt(tokenEnd);
            switch (c){
                case ' ': // in descriptions only
                    if(tokenStarted && ! hasDescription) {
                        tokens.add(artifact.substring(tokenStart, tokenEnd));
                        tokenStarted = false;
                        hasDescription = true;
                    }
                    continue;

                case ':':
                case ')': //end of descriptions and omitted artifacts
                    tokens.add(artifact.substring(tokenStart, tokenEnd));
                    tokenStarted = false;
                    continue;

                case '-': // in omitted artifacts descriptions
                    continue;

                case '(': // in omitted artifacts
                    if(tokenEnd == 0) {
                        omitted = true;
                    }
                    continue;

                default:
                    if(! tokenStarted) {
                        tokenStart = tokenEnd;
                        tokenStarted = true;
                    }
                    continue;
            }
        }

        //last token
        if(tokenStarted) {
            tokens.add(artifact.substring(tokenStart, tokenEnd));
        }

        String groupId;
        String artifactId;
        String packaging;
        String classifier;
        String version;
        String scope;
        String description;

        if(tokens.size() == 4) {

            groupId = tokens.get(0);
            artifactId = tokens.get(1);
            packaging = tokens.get(2);
            version = tokens.get(3);
            scope = null;
            description = null;
            classifier = null;

        } else if(tokens.size() == 5) {

            groupId = tokens.get(0);
            artifactId = tokens.get(1);
            packaging = tokens.get(2);
            version = tokens.get(3);
            scope = tokens.get(4);
            description = null;
            classifier = null;

        } else if(tokens.size() == 6) {

            if(hasDescription) {
                groupId = tokens.get(0);
                artifactId = tokens.get(1);
                packaging = tokens.get(2);
                version = tokens.get(3);
                scope = tokens.get(4);
                description = tokens.get(5);
                classifier = null;
            } else {
                groupId = tokens.get(0);
                artifactId = tokens.get(1);
                packaging = tokens.get(2);
                classifier = tokens.get(3);
                version = tokens.get(4);
                scope = tokens.get(5);
                description = null;
            }

        } else if(tokens.size() == 7) {

            groupId = tokens.get(0);
            artifactId = tokens.get(1);
            packaging = tokens.get(2);
            classifier = tokens.get(3);
            version = tokens.get(4);
            scope = tokens.get(5);
            description = tokens.get(6);

        } else {
            throw new IllegalStateException("Wrong number of tokens: " + tokens.size() + " for artifact: " + artifact);
        }

        final Node node = new Node(
            groupId,
            artifactId,
            packaging,
            classifier,
            version,
            scope,
            description,
            omitted
        );
        return node;
    }
}