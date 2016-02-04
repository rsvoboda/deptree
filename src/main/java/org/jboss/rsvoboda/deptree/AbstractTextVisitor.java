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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;


/**
 * Base class for visitors that generate an output
 *
 */
public abstract class AbstractTextVisitor implements Visitor {


    private final StringWriter sw;

    private final BufferedWriter bw;

    public AbstractTextVisitor() {
        sw = new StringWriter();
        bw = new BufferedWriter(sw);
    }

    public void visit(Node node){
        try {
            writeNode(node);
            for (Node child : node.getChildNodes()) {
                visit(child);
            }
        } catch (IOException e) {
        }
    }

    private void writeNode(Node node) throws IOException {
        //the tree symbols
        writeTreeSymbols(node);
        //the node itself
        bw.write(node.getArtifactCanonicalForm());
        bw.newLine();
    }

    private void writeTreeSymbols(Node node) throws IOException {
        if(node.getParent() != null) {
            writeParentTreeSymbols(node.getParent());
            bw.write(getTreeSymbols(node));
        }
    }

    private void writeParentTreeSymbols(Node node) throws IOException {
        if(node.getParent() != null) {
            writeParentTreeSymbols(node.getParent());
            bw.write(getParentTreeSymbols(node));
        }
    }

    public abstract String getTreeSymbols(Node node);

    public abstract String getParentTreeSymbols(Node node);

    @Override
    public String toString() {
        try {
            bw.flush();
            sw.flush();
            return sw.toString();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                bw.close();
            } catch (IOException e) { }
            try {
                sw.close();
            } catch (IOException ex) { }
        }
    }

}
