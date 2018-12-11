package dk.nykredit.car.it;

import dk.nykredit.car.CarService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class SimplestDeployment {

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive war = (WebArchive) EmbeddedMaven.forProject(new File("pom.xml"))
                .useMaven3Version("3.3.9")
                .setGoals("package")
                .setQuiet()
                .skipTests(true)
                .ignoreFailure()
                .build().getDefaultBuiltArchive();

        war.addAsResource("persistence.xml", "META-INF/persistence.xml");//replace with test persistence
        return war;
    }  
    
    @Inject
    CarService carService;


    @Test
    @UsingDataSet("car.yml")
    public void shouldCountCars() {
        assertNotNull(carService);
        Assert.assertEquals(carService.crud().count(), 4);
    }
}
