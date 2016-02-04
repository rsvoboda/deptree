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
import org.junit.Test;

public final class XmlBindTestCase {
    static Node tree;

    @BeforeClass
    public static void initDepTree() throws Exception {
        Parser parser = new TextParser();
        tree = parser.parse(new FileReader("target/dependency-tree.txt"));
    }
  
    
    /**
     * javax.xml.bind shouldn't be used, we have org.jboss.spec.javax.xml.bind GroupId
     * @throws Exception 
     */
    @Test
    public void testNoJavaxXmlBind() throws Exception {
        Visitor visitor = new UnexpectedGroupIdVisitor("javax.xml.bind");
        visitor.visit(tree);
    }
    @Test
    public void testComOrgJbossXmlBind() throws Exception {
        Visitor visitor = new ExpectedGroupIdAndArtifactIdVisitor("org.jboss.spec.javax.xml.bind", "jboss-jaxb-api_2.2_spec");
        visitor.visit(tree);
    }
    /**
     * com.sun.xml.bind shouldn't be used, we have org.glassfish.jaxb stuff
     * com.sun.xml.bind.external:rngom is expected to be present, performing search for exact GroupId
     * @throws Exception 
     */
    @Test
    public void testNoComSunXmlBind() throws Exception {
        Visitor visitor = new UnexpectedGroupIdVisitor("com.sun.xml.bind");
        visitor.visit(tree);
    }
    
    @Test
    public void testNoComSunXmlBindJaxbXjc() throws Exception {
        Visitor visitor = new UnexpectedGroupIdAndArtifactIdVisitor("com.sun.xml.bind", "jaxb-xjc");
        visitor.visit(tree);
    }
    @Test
    public void testComOrgGlassfishJaxbXjc() throws Exception {
        Visitor visitor = new ExpectedGroupIdAndArtifactIdVisitor("org.glassfish.jaxb", "jaxb-xjc");
        visitor.visit(tree);
    }
    
    @Test
    public void testNoComSunXmlBindJaxbCore() throws Exception {
        Visitor visitor = new UnexpectedGroupIdAndArtifactIdVisitor("com.sun.xml.bind", "jaxb-core");
        visitor.visit(tree);
    }
    @Test
    public void testComOrgGlassfishJaxbCore() throws Exception {
        Visitor visitor = new ExpectedGroupIdAndArtifactIdVisitor("org.glassfish.jaxb", "jaxb-core");
        visitor.visit(tree);
    }
    
    @Test
    public void testNoComSunXmlBindJaxbImpl() throws Exception {
        Visitor visitor = new UnexpectedGroupIdAndArtifactIdVisitor("com.sun.xml.bind", "jaxb-impl");
        visitor.visit(tree);
    }
    @Test
    public void testComOrgGlassfishJaxbRuntime() throws Exception {
        Visitor visitor = new ExpectedGroupIdAndArtifactIdVisitor("org.glassfish.jaxb", "jaxb-runtime");
        visitor.visit(tree);
    }
  
}
