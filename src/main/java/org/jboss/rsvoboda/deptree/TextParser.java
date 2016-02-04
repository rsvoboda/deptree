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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TextParser extends AbstractParser {

    private List<String> lines;
    private int lineIndex = 0;
    
    public Node parse(Reader reader) throws ParseException {

        try {
            this.lines = splitLines(reader);
        } catch (IOException e) {
            throw new ParseException(e);
        }

        if (lines.isEmpty()) {
            return null;
        }

        return parseInternal(0);

    }

    private List<String> splitLines(final Reader reader) throws IOException {
        String line = null;
        final BufferedReader br;
        if (reader instanceof BufferedReader) {
            br = (BufferedReader) reader;
        } else {
            br = new BufferedReader(reader);
        }
        final List<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    private Node parseInternal(final int depth) {

        //current node
        final Node node = this.parseLine();

        this.lineIndex++;

        //children
        while (this.lineIndex < this.lines.size() && this.computeDepth(this.lines.get(this.lineIndex)) > depth) {
            final Node child = this.parseInternal(depth + 1);
            if (node != null) {
                node.addChildNode(child);
            }
        }
        return node;

    }

    private int computeDepth(final String line) {
        return getArtifactIndex(line) / 3;
    }

    /**
     * sample lineIndex structure:
     * <pre>|  |  \- org.apache.activemq:activeio-core:test-jar:tests:3.1.0:compile</pre>
     *
     * @return
     */
    private Node parseLine() {
        String line = this.lines.get(this.lineIndex);
        String artifact = extractArtifact(line);
        return parseArtifactString(artifact);
    }

    private String extractArtifact(String line) {
        return line.substring(getArtifactIndex(line));
    }

    private int getArtifactIndex(final String line) {
        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);
            switch (c) {
                case ' '://whitespace, standard and extended
                case '|'://standard
                case '+'://standard
                case '\\'://standard
                case '-'://standard
                    continue;
                default:
                    return i;
            }
        }
        return -1;
    }

}
