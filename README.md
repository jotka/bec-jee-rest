# Simple Java EE CDI Crud example.


##Building with maven:

    mvn clean package


The application is available at http://localhost:8080/cdi-crud

##Running tests:

###with maven:

    mvn clean test -Ptests -Pwildfly-managed


###with an IDE:
    activate container profile in your IDE and Run CrudIt.java|CrudBdd.java|CrudRest|CrudAt as Junit test


#Technologies:

* CDI
* JSF
* Hibernate
* Primefaces/AdminFaces
* Deltaspike
* Arquillian
* Cucumber
* DBUnit
* JaxRS
* Swagger
* Openshift


== Application servers

Tested under:

* Wildfly 8.x
* with minor efforts(mainly persistence configurarion) should run on other *JavaEE 7* application servers

== Forge plugin
if you use http://forge.jboss.org/1.x/[JBoss Forge 1.x] (soon on forge 2) you may have a https://github.com/rmpestano/crud-plugin[look at this plugin] which generates Crud in the format you see at this project.



