/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.nykredit.car.test.infra;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.DeploymentConfiguration;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.DeploymentConfiguration.DeploymentContentBuilder;
import org.jboss.arquillian.container.test.spi.client.deployment.AutomaticDeployment;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;

/**
 *
 * @author rafael-pestano
 */
public class CdiCrudAutomaticDeployment implements AutomaticDeployment {

	private static WebArchive deploymentCache;

	@Override
	public DeploymentConfiguration generateDeploymentScenario(TestClass tc) {

		if (skipAutomaticDeployment(tc)) {
			return null; // skip if test class has @Deployment
		}

		if (deploymentCache == null) { // avoid rebuild project on every test class
			deploymentCache = (WebArchive) EmbeddedMaven.forProject(new File("pom.xml"))
					.useMaven3Version("3.3.9")
					.setGoals("package")
					.setQuiet()
					.skipTests(true)
					.ignoreFailure()
					.build().getDefaultBuiltArchive();
		}
		
		return new DeploymentContentBuilder(ShrinkWrap.create(WebArchive.class).merge(deploymentCache))//use merge to not modify original archive
				.withDeployment().withTestable(isTestable(tc)).build().get();

	}

	private boolean skipAutomaticDeployment(TestClass tc) {
		return tc.getMethod(Deployment.class) != null;
	}
	
	private boolean isTestable(TestClass tc) {
		return tc.getAnnotation(RunAsClient.class) == null;
	}

}
