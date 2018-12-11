package dk.nykredit.car.util;

import dk.nykredit.car.ft.CrudAt;
import dk.nykredit.infra.InitAppBean;
import dk.nykredit.car.it.CrudIt;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

import java.io.File;

/**
 * Arquillian WebArchive factory
 */
public class Deployments {
    protected static final String WEB_INF= "src/main/webapp/WEB-INF";

    /**
     * @return base WebArchive for all arquillian tests
     */
    public static WebArchive getBaseDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class);
        war.addPackages(true, "com.nykredit.car");
        war.deleteClass(InitAppBean.class);
        war.deletePackages(true,CrudIt.class.getPackage()).deletePackages(true,CrudAt.class.getPackage());
        //LIBS
        MavenResolverSystem resolver = Maven.resolver();
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.github.adminfaces:admin-template").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.github.adminfaces:admin-theme").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.modules:deltaspike-jsf-module-api").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.modules:deltaspike-jsf-module-impl").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.core:deltaspike-core-impl").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.core:deltaspike-core-api").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.modules:deltaspike-security-module-api").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.modules:deltaspike-security-module-impl").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.modules:deltaspike-proxy-module-api:1.8.0").withoutTransitivity().asSingleFile());
        war.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.assertj:assertj-core:1.7.0").withoutTransitivity().asSingleFile());

        //WEB-INF
        war.addAsWebInfResource(new File(WEB_INF,"beans.xml"), "beans.xml");
        war.addAsWebInfResource(new File(WEB_INF,"web.xml"), "web.xml");
        war.addAsWebInfResource(new File(WEB_INF,"faces-config.xml"), "faces-config.xml");

        //resources
        war.addAsResource("persistence.xml", "META-INF/persistence.xml");

        return war;
    }

}
