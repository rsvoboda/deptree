Dependency tree checks on jbossws-cxf-client
========================
Maven project which generates dependency tree text file which is analyzed in tests

Building & Running
-------------------
> mvn -Pcxf clean test 

> mvn -Pcxf clean test -Dversion.jbossws-cxf-client=5.1.9.Final

> mvn -Pwf test -Dtest=AggregatedDetailsTestCase

Project site
-------------------
http://rsvoboda.github.io/deptree/
