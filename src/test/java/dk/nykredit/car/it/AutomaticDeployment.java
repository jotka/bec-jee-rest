package dk.nykredit.car.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import dk.nykredit.car.CarService;
import dk.nykredit.car.test.infra.CdiCrudAutomaticDeployment;
import org.jboss.arquillian.container.test.api.BeforeDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by rmpestano on 07/06/18.
 * 
 * This test will use automatic deployment provided by {@link CdiCrudAutomaticDeployment}
 * 
 */
@RunWith(Arquillian.class)
public class AutomaticDeployment {

	@BeforeDeployment
	public static Archive<?> beforeDeployment(Archive<?> archive) {
		WebArchive war = (WebArchive) archive;
		war.addAsResource("persistence.xml", "META-INF/persistence.xml");// replace with test persistence (just an example of how to modify war, could be done in CdiCrudAutomaticDeployment)
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
