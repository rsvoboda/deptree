/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat Middleware LLC, and individual contributors
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

import java.io.FileReader;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public final class Slf4jTestCase {
    static Node tree;

    @BeforeClass
    public static void initDepTree() throws Exception {
        Parser parser = new TextParser();
        tree = parser.parse(new FileReader("target/dependency-tree.txt"));

    }
        
    /**
     * slf4j is excluded by upstream, it is causing problems for wsi ts too
     * @throws Exception
     */
    @Test
    @Ignore("Not relevant since https://issues.jboss.org/browse/JBWS-3977")
    public void testNoSlf4j() throws Exception {
        Visitor visitor = new UnexpectedGroupIdVisitor("org.slf4j");
        visitor.visit(tree);
    }

    /**
     * Check slf4j dependencies valid since since https://issues.jboss.org/browse/JBWS-3977"
     * @throws Exception
     */
    @Test
    public void testSlf4j() throws Exception {
        Visitor visitor = new ExplicitGroupIdAndArtifactIdsVisitor("org.slf4j", "slf4j-log4j12", "slf4j-api");
        visitor.visit(tree);
    }
}
