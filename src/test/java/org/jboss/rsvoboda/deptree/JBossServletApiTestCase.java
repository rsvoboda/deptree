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

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileReader;

public final class JBossServletApiTestCase {
    static Node tree;

    @BeforeClass
    public static void initDepTree() throws Exception {
        Parser parser = new TextParser();
        tree = parser.parse(new FileReader("target/dependency-tree.txt"));

    }
        
    /**
     * Test for https://issues.jboss.org/browse/JBWS-3972
     * @throws Exception 
     */
    @Test
    public void testNo30Spec() throws Exception {
        Visitor visitor = new UnexpectedGroupIdAndArtifactIdVisitor("org.jboss.spec.javax.servlet", "jboss-servlet-api_3.0_spec");
        visitor.visit(tree);
    }
    /**
     * Test for https://issues.jboss.org/browse/JBWS-3972
     * @throws Exception
     */
    @Test
    public void test31Spec() throws Exception {
        Visitor visitor = new ExpectedGroupIdAndArtifactIdVisitor("org.jboss.spec.javax.servlet", "jboss-servlet-api_3.1_spec");
        visitor.visit(tree);
    }
}
