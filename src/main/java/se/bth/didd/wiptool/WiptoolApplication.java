package se.bth.didd.wiptool;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtContext;
import org.skife.jdbi.v2.DBI;
import com.github.rkmk.container.FoldingListContainerFactory;
import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.github.rkmk.mapper.CustomMapperFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import se.bth.didd.wiptool.api.Login;
import se.bth.didd.wiptool.auth.AuthFilterUtils;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.resources.CapabilityResource;
import se.bth.didd.wiptool.resources.IssueResource;
import se.bth.didd.wiptool.resources.LoginResource;
import se.bth.didd.wiptool.resources.RolesResource;
import se.bth.didd.wiptool.resources.SkillResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.PolymorphicAuthDynamicFeature;
import io.dropwizard.auth.PolymorphicAuthValueFactoryProvider;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.bth.didd.wiptool.configuration.LocalDateMapperFactory;
import se.bth.didd.wiptool.configuration.WiptoolConfiguration;
import se.bth.didd.wiptool.db.AuthDAO;
import se.bth.didd.wiptool.db.CapabilityDAO;
import se.bth.didd.wiptool.db.IssuesDAO;
import se.bth.didd.wiptool.db.PeopleDAO;
import se.bth.didd.wiptool.db.ProjectDAO;
import se.bth.didd.wiptool.db.RedmineDAO;
import se.bth.didd.wiptool.db.SprintDAO;
import se.bth.didd.wiptool.redmine.Redmine;
import se.bth.didd.wiptool.db.RolesDAO;
import se.bth.didd.wiptool.db.SkillDAO;
import se.bth.didd.wiptool.resources.PeopleResource;
import se.bth.didd.wiptool.resources.ProjectResource;
import se.bth.didd.wiptool.resources.SprintResource;

/**
 * Main application class. Will start dropwizard application server.
 * 
 * @see <a href=
 *      "http://www.dropwizard.io/getting-started.html">http://www.dropwizard.io/getting-started.html</a>
 *
 */
public class WiptoolApplication extends Application<WiptoolConfiguration> {

	@Override
	public String getName() {
		return "wiptool";
	}

	/**
	 * ConfiguredAssetsBundle class can be used instead of AssetsBundle in
	 * Dropwizard applications to take advantage of the ability to specify
	 * redirects for URIs to that loads them from disk instead of the classpath.
	 * This allows developers to edit browser-interpreted files and reload them
	 * without needing to recompile source.
	 */
	@Override
	public void initialize(final Bootstrap<WiptoolConfiguration> bootstrap) {
		bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/", "index.html"));
	}

	@Override
	public void run(final WiptoolConfiguration configuration, final Environment environment) throws UnsupportedEncodingException {
		/*
		 * This will create a new managed connection pool to the database and a
		 * new DBI instance for you to use
		 */
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
		final AuthDAO authDAO = jdbi.onDemand(AuthDAO.class);
		final ProjectDAO projectDAO = jdbi.onDemand(ProjectDAO.class);
		final SprintDAO sprintDAO = jdbi.onDemand(SprintDAO.class);
		final PeopleDAO peopleDAO = jdbi.onDemand(PeopleDAO.class);
		final IssuesDAO issuesDAO = jdbi.onDemand(IssuesDAO.class);
		final RolesDAO rolesDAO = jdbi.onDemand(RolesDAO.class);
		final RedmineDAO redmineDAO = jdbi.onDemand(RedmineDAO.class);
		final CapabilityDAO capabilityDAO = jdbi.onDemand(CapabilityDAO.class);
		final SkillDAO skillDAO = jdbi.onDemand(SkillDAO.class);
		/*
		 * Registering the CustomMapperFactory & FoldingListContainerFactory to
		 * dbi. This is essential for folding the objects.
		 * @see (https://github.com/Manikandan-K/jdbi-folder) 
		 * @see (http://blog.asnpce.com/technology/641)
		 * This tool uses jdbi-folder dependency for folding the objects 
		 * (generating nested objects in API responses)
		 */
		CustomMapperFactory customMapperFactory = new CustomMapperFactory();
		customMapperFactory.register(new LocalDateMapperFactory());
		jdbi.registerMapper(customMapperFactory);
		jdbi.registerContainerFactory(new FoldingListContainerFactory());
		/* Creating empty tables in the database */
		peopleDAO.createPeopleTable();
		projectDAO.createProjectTable();
		sprintDAO.createSprintTable();
		sprintDAO.createSprintParticipationTable();
		sprintDAO.createSprintDomainsDbTable();
		sprintDAO.createSprintAssetsDbTable();
		sprintDAO.createDomainsInaSprintTable();
		sprintDAO.createAssetsInaSprintTable();
		sprintDAO.createSprintDevelopmentEnvDbTable();
		sprintDAO.createDevelopmentEnvInaSprintTable();
		sprintDAO.createSprintRequirementsDBTable();
		sprintDAO.createRequirementsSelectedForSprintTable();
		sprintDAO.createSprintQuestionnaireTable();
		issuesDAO.createIssuesTable();
		sprintDAO.createSprintComprisingIssuesTable();
		rolesDAO.createRolesDBTable();
		rolesDAO.createRolesOfPeopleTable();
		projectDAO.createProjectParticipationTable();
		authDAO.createLoginCredentialsTable();
		capabilityDAO.createCapabilityDBTable();
		capabilityDAO.createCapabilityMeasuresTable();
		capabilityDAO.createCapabilityAssessmentTable();
		skillDAO.createSkillDBTable();
		skillDAO.createSkillAssessmentTable();
		

		/*
		 * Add configuration parameters to the run method. Register all the
		 * additional configuration parameters from YAML file, with resources
		 */
		environment.jersey().register(
				new ProjectResource(projectDAO, configuration.getRedmineUrl(),configuration.getApiAccessKey()));
		environment.jersey().register(
				new SprintResource(sprintDAO, configuration.getRedmineUrl(),configuration.getApiAccessKey()));
		environment.jersey().register(
				new PeopleResource(peopleDAO, configuration.getRedmineUrl(),configuration.getApiAccessKey()));
		environment.jersey().register(
				new RolesResource(rolesDAO));
		environment.jersey().register(
				new CapabilityResource(capabilityDAO));
		environment.jersey().register(
				new Redmine(redmineDAO, configuration.getRedmineUrl(),configuration.getApiAccessKey()));
		environment.jersey().register(
				new SkillResource(skillDAO));
		environment.jersey().register(
				new IssueResource(issuesDAO, configuration.getRedmineUrl(),configuration.getApiAccessKey()));
		environment.jersey().register(new LoginResource(authDAO));
		
		registerAuthFilters(environment);
		
		CapabilityResource capability = new CapabilityResource(capabilityDAO);
		capability.insertDefaultCapabilities();
		
		Login login = new Login();
		login.setUserFirstName("Default admin");
		login.setUserLastName("Default admin");
		login.setUserMailId(configuration.getAdminUserName());
		login.setPassword(configuration.getAdminPassword());
		login.setUserName(configuration.getAdminUserName());
		
		LoginResource sampleLoginResource = new LoginResource(authDAO);
		sampleLoginResource.registerNewUser(login);
		
	}
	private void registerAuthFilters(Environment environment) {
		AuthFilterUtils authFilterUtils = new AuthFilterUtils();
		final AuthFilter<BasicCredentials, PrincipalImpl> basicFilter = authFilterUtils.buildBasicAuthFilter();
		final AuthFilter<JwtContext, User> jwtFilter = authFilterUtils.buildJwtAuthFilter();

		final PolymorphicAuthDynamicFeature<Principal> feature = new PolymorphicAuthDynamicFeature<>(
				ImmutableMap.of(PrincipalImpl.class, basicFilter, User.class, jwtFilter));
		final AbstractBinder binder = new PolymorphicAuthValueFactoryProvider.Binder<>(
				ImmutableSet.of(PrincipalImpl.class, User.class));

		
		environment.jersey().register(feature);
		environment.jersey().register(binder);
		environment.jersey().register(RolesAllowedDynamicFeature.class);
	}
	public static void main(final String[] args) throws Exception {
		new WiptoolApplication().run(args);
		
	}

}
