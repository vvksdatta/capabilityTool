package se.bth.didd.wiptool.resources;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.github.rkmk.container.FoldingList;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Version;
import com.taskadapter.redmineapi.bean.VersionFactory;
import se.bth.didd.wiptool.api.SprintIdProjectId;
import se.bth.didd.wiptool.api.NewSprint;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.RolesOfPeopleSprint;
import se.bth.didd.wiptool.api.SelectedSprintRequirement;
import se.bth.didd.wiptool.api.SprintRequirementsTabsTemplate;
import se.bth.didd.wiptool.api.SprintRequirement;
import se.bth.didd.wiptool.api.SprintRequirementNameLevel;
import se.bth.didd.wiptool.api.SprintRequirementNameValueDescriptionUser;
import se.bth.didd.wiptool.api.SprintRequirementUpdate;
import se.bth.didd.wiptool.api.Sprint;
import se.bth.didd.wiptool.api.SprintAllDomainsAssets;
import se.bth.didd.wiptool.api.SprintAsset;
import se.bth.didd.wiptool.api.SprintBriefSummary;
import se.bth.didd.wiptool.api.SprintCompanyDrivenFactors;
import se.bth.didd.wiptool.api.SprintDevelopmentEnvironment;
import se.bth.didd.wiptool.api.SprintDomain;
import se.bth.didd.wiptool.api.SprintIdProjectIdParticipants;
import se.bth.didd.wiptool.api.SprintQuestionnaireTemplate;
import se.bth.didd.wiptool.api.SprintSummary;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.db.SprintDAO;
import se.bth.didd.wiptool.api.CompanyDrivenFactorsNames;
import se.bth.didd.wiptool.api.ErrorMessage;
import se.bth.didd.wiptool.api.ExistingSprint;

@Path("/sprints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

/*
 * This servers as the main API resource for sprints entity
 */
public class SprintResource {

	private String redmineUrl;
	private String apiAccessKey;
	private SprintDAO sprintDAO;

	/*
	 * This constructor is invoked in the run method of the main application and
	 * it maps the redmine configuration values to the variables 'redmineUrl'
	 * and 'apiAcessKey'.
	 */

	public SprintResource(SprintDAO sprintDao, String redmineUrl, String apiAccessKey) {
		super();
		this.redmineUrl = redmineUrl;
		this.apiAccessKey = apiAccessKey;
		this.sprintDAO = sprintDao;
	}

	@GET
	@Path("/summary")
	public List<SprintSummary> getSummary() {

		/*
		 * As specified in jdbi-folder, whenever we need results to be folded,
		 * we need to use FoldingList instead of List. Result will be folded.
		 * Just call getValues on FoldingList will give List<?> values.
		 */
		FoldingList<SprintSummary> foldedResult = sprintDAO.getSummary();
		List<SprintSummary> sprintSummary = foldedResult.getValues();
		return sprintSummary;
	}

	@GET
	@Path("/listOfSprintsInProject/{projectId}")
	public Response getListOfSprintsInProject(@PathParam("projectId") Integer projectId) {

		try {
			FoldingList<SprintSummary> foldedResult = sprintDAO.getListOfSprintsInProject(projectId);
			List<SprintSummary> sprintSummary = foldedResult.getValues();
			return Response.ok(sprintSummary).build();

		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/existingSprintDetails/{sprintId}/{projectId}")
	public Response existingSprintDetails(@PathParam("sprintId") Integer sprintId,
			@PathParam("projectId") Integer projectId) {

		ExistingSprint sprintToDisplay = new ExistingSprint();

		List<Sprint> thisSprint;
		try {
			thisSprint = sprintDAO.findByIds(projectId, sprintId);
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
		for (Sprint selectedSprint : thisSprint) {
			sprintToDisplay.setProjectId(selectedSprint.getProjectId());
			sprintToDisplay.setSprintId(selectedSprint.getSprintId());
			sprintToDisplay.setSprintDescription(selectedSprint.getSprintDescription());
			sprintToDisplay.setSprintEndDate(selectedSprint.getSprintEndDate());
			sprintToDisplay.setSprintEstimatedEffort(selectedSprint.getSprintEstimatedEffort());
			sprintToDisplay.setSprintActualEffort(selectedSprint.getSprintActualEffort());
			sprintToDisplay.setSprintName(selectedSprint.getSprintName());
			sprintToDisplay.setSprintStartDate(selectedSprint.getSprintStartDate());
			sprintToDisplay.setSprintUpdatedBy(selectedSprint.getSprintUpdatedBy());
			sprintToDisplay.setExpectedCoachingPotential(selectedSprint.getExpectedCoachingPotential());
			sprintToDisplay.setExpectedTeamKnowledgeDiversity(selectedSprint.getExpectedTeamKnowledgeDiversity());
			// status: open or closed
			sprintToDisplay.setSprintStatus(selectedSprint.getSprintStatus());
			sprintToDisplay.setSprintPhase(selectedSprint.getSprintPhase());
		}
		try {
			sprintToDisplay.setApplicationDomain(sprintDAO.getDomainsOfSprint(projectId, sprintId));
			sprintToDisplay.setKindOfAssets(sprintDAO.getAssetsOfSprint(projectId, sprintId));
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
		return Response.ok(sprintToDisplay).build();
	}

	@PUT
	@Path("/addNewSprint")
	public Response addNewSprint(NewSprint newSprint) throws RedmineException {

		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiAccessKey);
		Version newSprintOnRedmine = VersionFactory.create(newSprint.getProjectId(), newSprint.getSprintName());
		newSprintOnRedmine.setDescription(newSprint.getSprintDescription());
		newSprintOnRedmine.setDueDate(newSprint.getSprintEndDate());
		newSprintOnRedmine.setStatus("open");
		Sprint saveSprint = new Sprint();
		SprintIdProjectId newCreatedSprint = new SprintIdProjectId();
		try {
			Version sprintCreatedOnRedmine = redmineManager.getProjectManager().createVersion(newSprintOnRedmine);
			newCreatedSprint.setSprintId(sprintCreatedOnRedmine.getId());
			newCreatedSprint.setProjectId(newSprint.getProjectId());

			saveSprint.setProjectId(newSprint.getProjectId());
			saveSprint.setSprintId(newCreatedSprint.getSprintId());
			saveSprint.setSprintName(newSprint.getSprintName());
			saveSprint.setSprintEstimatedEffort(newSprint.getSprintEstimatedEffort());
			saveSprint.setSprintDescription(newSprint.getSprintDescription());
			saveSprint.setSprintStartDate(newSprint.getSprintStartDate());
			saveSprint.setSprintEndDate(newSprint.getSprintEndDate());
			saveSprint.setSprintLastUpdate(sprintCreatedOnRedmine.getCreatedOn());
			saveSprint.setSprintRedmineUpdate(sprintCreatedOnRedmine.getCreatedOn());
			saveSprint.setSprintStatus("open");
			saveSprint.setSprintUpdatedBy(newSprint.getSprintUpdatedBy());
			try {
				sprintDAO.insert(saveSprint);
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}

		} catch (RedmineException e) {

			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

		for (SprintDomain domain : newSprint.getApplicationDomain()) {

			try {
				sprintDAO.insertDomainsOfSprint(saveSprint.getProjectId(), saveSprint.getSprintId(),
						domain.getDomainId());
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}
		}

		for (SprintAsset asset : newSprint.getKindOfAssets()) {
			try {
				sprintDAO.insertAssetsOfSprint(saveSprint.getProjectId(), saveSprint.getSprintId(), asset.getAssetId());
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}

		}

		return Response.ok(newCreatedSprint).build();
	}

	@POST
	@Path("/updateSprint")
	public Response updateSprint(ExistingSprint existingSprint) throws RedmineException {

		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiAccessKey);
		Version existingSprintOnRedmine = VersionFactory.create(existingSprint.getSprintId());
		existingSprintOnRedmine.setName(existingSprint.getSprintName());
		existingSprintOnRedmine.setDescription(existingSprint.getSprintDescription());
		existingSprintOnRedmine.setDueDate(existingSprint.getSprintEndDate());

		if (existingSprint.getSprintStatus().equals("true")) {
			existingSprintOnRedmine.setStatus("open");
		} else {
			existingSprintOnRedmine.setStatus("closed");
		}
		Sprint saveSprint = new Sprint();
		SprintIdProjectId updatedSprintDetails = new SprintIdProjectId();
		try {
			redmineManager.getProjectManager().update(existingSprintOnRedmine);
			Version sprintUpdatedOnRedmine = redmineManager.getProjectManager()
					.getVersionById(existingSprint.getSprintId());
			updatedSprintDetails.setSprintId(existingSprint.getSprintId());
			updatedSprintDetails.setProjectId(existingSprint.getProjectId());

			saveSprint.setProjectId(existingSprint.getProjectId());
			saveSprint.setSprintId(existingSprint.getSprintId());
			saveSprint.setSprintName(existingSprint.getSprintName());
			saveSprint.setSprintEstimatedEffort(existingSprint.getSprintEstimatedEffort());
			saveSprint.setSprintActualEffort(existingSprint.getSprintActualEffort());
			saveSprint.setSprintDescription(existingSprint.getSprintDescription());
			saveSprint.setSprintStartDate(existingSprint.getSprintStartDate());
			saveSprint.setSprintEndDate(existingSprint.getSprintEndDate());
			saveSprint.setSprintLastUpdate(sprintUpdatedOnRedmine.getUpdatedOn());
			saveSprint.setSprintRedmineUpdate(sprintUpdatedOnRedmine.getUpdatedOn());
			if (existingSprint.getSprintStatus().equals("true")) {
				saveSprint.setSprintStatus("open");
			} else if (existingSprint.getSprintStatus().equals("false")) {
				saveSprint.setSprintStatus("closed");
			}
			saveSprint.setSprintPhase(existingSprint.getSprintPhase());
			saveSprint.setSprintUpdatedBy(existingSprint.getSprintUpdatedBy());
			try {
				sprintDAO.updateSprintById(saveSprint);
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}

		} catch (RedmineException e) {

			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

		if (existingSprint.getApplicationDomain().size() != 0) {
			sprintDAO.DeleteDomainsOfSprint(saveSprint.getProjectId(), saveSprint.getSprintId());
		}
		for (SprintDomain domain : existingSprint.getApplicationDomain()) {

			try {
				if (sprintDAO.ifDomainExistsInASprint(saveSprint.getProjectId(), saveSprint.getSprintId(),
						domain.getDomainId()) == false) {
					sprintDAO.insertDomainsOfSprint(saveSprint.getProjectId(), saveSprint.getSprintId(),
							domain.getDomainId());
				}
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}
		}

		if (existingSprint.getKindOfAssets().size() != 0) {
			sprintDAO.DeleteAssetsOfSprint(saveSprint.getProjectId(), saveSprint.getSprintId());
		}
		for (SprintAsset asset : existingSprint.getKindOfAssets()) {
			try {
				if (sprintDAO.ifAssetExistsInASprint(saveSprint.getProjectId(), saveSprint.getSprintId(),
						asset.getAssetId()) == false) {
					sprintDAO.insertAssetsOfSprint(saveSprint.getProjectId(), saveSprint.getSprintId(),
							asset.getAssetId());
				}

			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}

		}

		return Response.ok(updatedSprintDetails).build();
	}

	@GET
	@Path("/getSprintParticipants/{sprintId}/{projectId}")
	public Response getSprintParticipants(@PathParam("sprintId") Integer sprintId,
			@PathParam("projectId") Integer projectId) {
		try {
			List<RolesOfPeopleSprint> rolesOfPeople = sprintDAO.getSprintParticipants(projectId, sprintId);
			return Response.ok(rolesOfPeople).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@PUT
	@Path("/searchAsset")
	public List<SprintRequirementsTabsTemplate> searchAsset(String enteredAsset) {
		if (enteredAsset != "") {
			List<SprintAsset> assets = sprintDAO.getAssetId("%" + enteredAsset + "%");
			List<SprintRequirementsTabsTemplate> sampleAsset = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintAsset asset : assets) {
				// System.out.println("the skill is "+ skill.getSkillName());
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(asset.getAssetName());
				sample.setDisplay(asset.getAssetName());
				sampleAsset.add(sample);
			}
			return sampleAsset;
		} else {
			List<SprintAsset> assets = sprintDAO.getAllAssets();
			List<SprintRequirementsTabsTemplate> sampleDomain = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintAsset asset : assets) {
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(asset.getAssetName());
				sample.setDisplay(asset.getAssetName());
				sampleDomain.add(sample);
			}
			return sampleDomain;

		}
	}

	@PUT
	@Path("/searchDomain")
	public List<SprintRequirementsTabsTemplate> searchDomain(String enteredDomain) {
		if (enteredDomain != "") {
			// System.out.println("the "+ enteredSkill);
			List<SprintDomain> domains = sprintDAO.getDomainId("%" + enteredDomain + "%");
			List<SprintRequirementsTabsTemplate> sampleDomain = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintDomain domain : domains) {
				// System.out.println("the skill is "+ skill.getSkillName());
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(domain.getDomainName());
				sample.setDisplay(domain.getDomainName());
				sampleDomain.add(sample);
			}
			return sampleDomain;
		} else {
			List<SprintDomain> domains = sprintDAO.getAllDomains();
			List<SprintRequirementsTabsTemplate> sampleDomain = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintDomain domain : domains) {
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(domain.getDomainName());
				sample.setDisplay(domain.getDomainName());
				sampleDomain.add(sample);
			}
			return sampleDomain;

		}
	}

	@PUT
	@Path("/searchEnvironment")
	public List<SprintRequirementsTabsTemplate> searchDevelopmentEnvironment(String enteredDevelopmentEnv) {
		if (enteredDevelopmentEnv != "") {
			// System.out.println("the "+ enteredSkill);
			List<SprintDevelopmentEnvironment> developmentEnvs = sprintDAO
					.getDevelopmentEnvId("%" + enteredDevelopmentEnv + "%");
			List<SprintRequirementsTabsTemplate> sampleDevelopmentEnv = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintDevelopmentEnvironment environment : developmentEnvs) {
				// System.out.println("the skill is "+ skill.getSkillName());
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(environment.getEnvName());
				sample.setDisplay(environment.getEnvName());
				sampleDevelopmentEnv.add(sample);
			}
			return sampleDevelopmentEnv;
		} else {
			List<SprintDevelopmentEnvironment> developmentEnvs = sprintDAO.getAllDevelopmentEnv();
			List<SprintRequirementsTabsTemplate> sampleDevelopmentEnv = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintDevelopmentEnvironment environment : developmentEnvs) {
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(environment.getEnvName());
				sample.setDisplay(environment.getEnvName());
				sampleDevelopmentEnv.add(sample);
			}
			return sampleDevelopmentEnv;

		}
	}

	@GET
	@Path("/getAllSprintDomains")
	public List<SprintDomain> getAllDomains() {
		return sprintDAO.getAllDomains();
	}

	@GET
	@Path("/getAllSprintAssets")
	public List<SprintAsset> getAllAsets() {
		return sprintDAO.getAllAssets();
	}

	@GET
	@Path("/getAllSprintDomainsAssets")
	public Response getAllSprintDomainsAssets() {

		SprintAllDomainsAssets allDomainsAssetsList = new SprintAllDomainsAssets();
		try {
			allDomainsAssetsList.setLatestdomainsList(sprintDAO.getAllDomains());
			allDomainsAssetsList.setLatestassetsList(sprintDAO.getAllAssets());
			return Response.ok(allDomainsAssetsList).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/getAllSprintDevelopmentEnvironments")
	public List<SprintDevelopmentEnvironment> getAllDevelopmentEnvironments() {
		return sprintDAO.getAllDevelopmentEnv();
	}

	@PUT
	@Path("/updateSprintParticipants")
	public Response updateSprintParticipants(SprintIdProjectIdParticipants sprintdetails)
			throws RedmineException, SQLException {

		List<Integer> sprintParticipantsWhoContinue = new ArrayList<>();
		for (People person : sprintdetails.getSprintParticipants()) {

			List<RolesOfPeopleSprint> existingparticipants = sprintDAO
					.getSprintParticipants(sprintdetails.getProjectId(), sprintdetails.getSprintId());
			for (RolesOfPeopleSprint eachExistingParticipant : existingparticipants) {

				if (eachExistingParticipant.getPersonId().equals(person.getPersonId())) {

					sprintParticipantsWhoContinue.add(person.getPersonId());
					sprintDAO.updateSprintParticipantExists(sprintdetails.getProjectId(), sprintdetails.getSprintId(),
							person.getPersonId());
				}

			}

		}

		try {

			// System.out.println("projectId is" +sprintdetails.getProjectId()+"
			// sprint Id is "+ sprintdetails.getSprintId()+ " partici are " +
			// foo2);

			sprintDAO.deletePeopleWhoNoLongerParticipate(sprintdetails.getProjectId(), sprintdetails.getSprintId());

			for (People person : sprintdetails.getSprintParticipants()) {

				if (sprintDAO.ifPersonParticipatesInSprint(sprintdetails.getProjectId(), person.getPersonId(),
						sprintdetails.getSprintId()) == false) {
					sprintDAO.insertIntoSprintParticipation(sprintdetails.getProjectId(), person.getPersonId(),
							sprintdetails.getSprintId());
				}

				sprintDAO.updateSprintParticipantDoesnotExist(sprintdetails.getProjectId(), sprintdetails.getSprintId(),
						person.getPersonId());
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
		SuccessMessage success = new SuccessMessage();
		success.setSuccess("update successful");
		return Response.ok(success).build();
	}

	@POST
	@Path("/insertDomain")
	public Response insertDomain(String newDomain) {
		if (newDomain != "") {

			try {
				sprintDAO.insertSprintDomainsDB(newDomain);
			} catch (Exception e1) {
				return null;
			}

			SprintDomain sampleDomain = new SprintDomain();
			sampleDomain.setDomainId(0);
			sampleDomain.setDomainName(newDomain);

			return Response.ok(sampleDomain).build();

		}

		return null;
	}

	@POST
	@Path("/insertAsset")
	public Response insertAsset(String newAsset) {
		if (newAsset != "") {

			try {
				sprintDAO.insertSprintAssetsDB(newAsset);
			} catch (Exception e1) {
				return null;
			}

			SprintAsset sampleAsset = new SprintAsset();
			sampleAsset.setAssetId(0);
			sampleAsset.setAssetName(newAsset);

			return Response.ok(sampleAsset).build();
		}

		return null;
	}

	@POST
	@Path("/insertCompanyDrivenFactors")
	public Response insertCompanyDrivenFactors(SprintCompanyDrivenFactors companyDrivenFactors) {
		if (companyDrivenFactors.getProjectId() != null) {
			Long timeStamp = Calendar.getInstance().getTimeInMillis();
			Date date = new Date(timeStamp);
			Sprint sampleSprint = new Sprint();
			sampleSprint.setProjectId(companyDrivenFactors.getProjectId());
			sampleSprint.setSprintId(companyDrivenFactors.getSprintId());
			sampleSprint.setExpectedCoachingPotential(companyDrivenFactors.getCoachingPotential());
			sampleSprint.setExpectedTeamKnowledgeDiversity(companyDrivenFactors.getTeamKnowledgeDiversity());
			sampleSprint.setSprintUpdatedBy(companyDrivenFactors.getUpdatedBy());
			sampleSprint.setSprintLastUpdate(date);
			try {
				sprintDAO.updateSprintCompanyDrivenFactors(sampleSprint);
				if (companyDrivenFactors.getDevelopmentEnvironments().size() != 0) {
					if (sprintDAO.ifExistsDevelopmentEnvInaSprint(companyDrivenFactors.getProjectId(),
							companyDrivenFactors.getSprintId())) {
						sprintDAO.deleteDevelopmentEnvInaSprint(companyDrivenFactors.getProjectId(),
								companyDrivenFactors.getSprintId());
					}
				}

			} catch (Exception e) {
				System.out.println(e);
				ErrorMessage error = new ErrorMessage();
				error.setError(e.toString());
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			for (SprintDevelopmentEnvironment eachEnvironment : companyDrivenFactors.getDevelopmentEnvironments()) {
				try {

					sprintDAO.insertDevelopmentEnvInaSprint(companyDrivenFactors.getProjectId(),
							companyDrivenFactors.getSprintId(), eachEnvironment.getEnvId());

				} catch (Exception e) {
					System.out.println(e);
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			}

			SprintIdProjectId sprintDetails = new SprintIdProjectId();
			sprintDetails.setProjectId(companyDrivenFactors.getProjectId());
			sprintDetails.setSprintId(companyDrivenFactors.getSprintId());
			return Response.ok(sprintDetails).build();

		} else {
			ErrorMessage error = new ErrorMessage();
			error.setError("This error occured due to refreshing the web page");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
	}

	@GET
	@Path("/getCompanyDrivenFactors/{sprintId}/{projectId}")
	public Response getCompanyDrivenFactors(@PathParam("sprintId") Integer sprintId,
			@PathParam("projectId") Integer projectId) {
		SprintCompanyDrivenFactors factorsOfThisSprint = new SprintCompanyDrivenFactors();
		try {
			factorsOfThisSprint
					.setDevelopmentEnvironments(sprintDAO.getDevelopmentEnvironmentsOfaSprint(projectId, sprintId));
			List<CompanyDrivenFactorsNames> listOfFactors = sprintDAO.getCompanyDrivenFactorsOfSprint(projectId,
					sprintId);
			for (CompanyDrivenFactorsNames eachFactor : listOfFactors) {

				factorsOfThisSprint.setProjectId(projectId);
				factorsOfThisSprint.setSprintId(sprintId);
				factorsOfThisSprint.setCoachingPotential(eachFactor.getCoachingPotential());
				factorsOfThisSprint.setTeamKnowledgeDiversity(eachFactor.getTeamKnowledgeDiversity());
			}
			return Response.ok(factorsOfThisSprint).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

	}

	@POST
	@Path("/insertEnvironment")
	public Response insertDevelopmentEnvironment(String newDevelopmentEnvironment) {
		if (newDevelopmentEnvironment != "") {

			try {
				sprintDAO.insertSprintDevelopmentEnvDB(newDevelopmentEnvironment);
			} catch (Exception e1) {
				return Response.status(Status.BAD_REQUEST).entity(e1).build();
			}

			SprintDevelopmentEnvironment sampleDevelopmentEnvironment = new SprintDevelopmentEnvironment();
			sampleDevelopmentEnvironment.setEnvId(0);
			sampleDevelopmentEnvironment.setEnvName(newDevelopmentEnvironment);

			return Response.ok(sampleDevelopmentEnvironment).build();
		}

		return null;
	}

	@POST
	@Path("/insertRequirement")
	public Response insertRequirement(String newRequirement) {
		if (newRequirement != "") {

			try {
				sprintDAO.insertNewSprintRequirement(newRequirement);

				return Response.ok(sprintDAO.getRequirementId(newRequirement)).build();
			} catch (Exception e1) {
				System.out.println(e1);
				return Response.status(Status.BAD_REQUEST).entity(e1).build();
			}
		}

		return Response.status(Status.BAD_REQUEST).entity("error").build();
	}

	@PUT
	@Path("/searchRequirement")
	public List<SprintRequirementsTabsTemplate> searchSkill(String enteredRequirement) {
		if (enteredRequirement != "") {
			// System.out.println("the "+ enteredSkill);
			List<SprintRequirement> requirements = sprintDAO.getRequirementId("%" + enteredRequirement + "%");
			List<SprintRequirementsTabsTemplate> samplerequirement = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintRequirement requirement : requirements) {
				// System.out.println("the skill is "+ skill.getSkillName());
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(requirement.getSprintRequirementName());
				sample.setDisplay(requirement.getSprintRequirementName());
				sample.setExistingDescription(requirement.getSprintRequirementDescription());
				samplerequirement.add(sample);
			}
			return samplerequirement;
		} else {
			List<SprintRequirement> requirements = sprintDAO.getAllRequirements();
			List<SprintRequirementsTabsTemplate> sampleRequirement = new ArrayList<SprintRequirementsTabsTemplate>();
			for (SprintRequirement requirement : requirements) {
				SprintRequirementsTabsTemplate sample = new SprintRequirementsTabsTemplate();
				sample.setValue(requirement.getSprintRequirementName());
				sample.setDisplay(requirement.getSprintRequirementName());
				sample.setExistingDescription(requirement.getSprintRequirementDescription());
				sampleRequirement.add(sample);
			}
			return sampleRequirement;

		}
	}

	@GET
	@Path("/getRequirementsSelectedInSprint/{sprintId}/{projectId}")
	public Response getRequirementsSelectedInSprint(@PathParam("sprintId") Integer sprintId,
			@PathParam("projectId") Integer projectId) {
		try {
			List<SprintRequirementNameLevel> listOfRequirements = sprintDAO.getRequirementsSelectedInSprint(projectId,
					sprintId);
			return Response.ok(listOfRequirements).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/getQuestionnaire/{sprintId}/{projectId}")
	public Response getQuestionnaire(@PathParam("sprintId") Integer sprintId,
			@PathParam("projectId") Integer projectId) {

		try {
			List<SprintQuestionnaireTemplate> existingQuestionnaireData = sprintDAO.getSprintQuestionnaire(projectId,
					sprintId);

			return Response.ok(existingQuestionnaireData).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

	}

	@POST
	@Path("/updateQuestionnaire")
	public Response updateQuestionnaire(SprintQuestionnaireTemplate questionnaireList) {

		Long timeStamp = Calendar.getInstance().getTimeInMillis();
		Date date = new Date(timeStamp);
		questionnaireList.setLastUpdate(date);

		if (sprintDAO.IfSprintQuestionnaireAnswered(questionnaireList.getProjectId(),
				questionnaireList.getSprintId()) == false) {

			try {
				sprintDAO.insertSprintQuestionnaire(questionnaireList);
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}
		} else {

			try {
				sprintDAO.updateSprintQuestionnaire(questionnaireList);
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}
		}

		SprintIdProjectId sprintDetailsForResponse = new SprintIdProjectId();
		sprintDetailsForResponse.setProjectId(questionnaireList.getProjectId());
		sprintDetailsForResponse.setSprintId(questionnaireList.getSprintId());

		return Response.ok(sprintDetailsForResponse).build();
	}

	@POST
	@Path("/updateRequirementsSelection")
	public Response updateRequirementsSelection(List<SprintRequirementUpdate> requirementsList) {
		Long timeStamp = Calendar.getInstance().getTimeInMillis();
		for (SprintRequirementUpdate eachenteredRequirement : requirementsList) {

			Timestamp timestamp = new Timestamp(timeStamp);
			List<SprintRequirement> listOfRequirements = sprintDAO
					.getRequirementId(eachenteredRequirement.getSprintRequirementName());
			for (SprintRequirement thisRequirement : listOfRequirements) {
				try {
					SelectedSprintRequirement sampleRequirement = new SelectedSprintRequirement();
					sampleRequirement.setProjectId(eachenteredRequirement.getProjectId());
					sampleRequirement.setSprintId(eachenteredRequirement.getSprintId());
					sampleRequirement.setRequirementLevel(eachenteredRequirement.getRequirementLevel());
					sampleRequirement.setUpdatedBy(eachenteredRequirement.getUpdatedBy());
					sampleRequirement.setSprintRequirementId(thisRequirement.getSprintRequirementId());
					sampleRequirement.setOutcome(eachenteredRequirement.getOutcome());
					sampleRequirement.setLastUpdate(timestamp);
					if (sprintDAO.ifRequirementSelected(eachenteredRequirement.getProjectId(),
							eachenteredRequirement.getSprintId(), thisRequirement.getSprintRequirementId())) {
						sprintDAO.updateSelectedSprintRequirement(sampleRequirement);
					} else {
						sprintDAO.insertSelectedSprintRequirement(sampleRequirement);
					}
				} catch (Exception e) {

					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}

				try {

					if (eachenteredRequirement.getSprintRequirementDescription() != null) {
						System.out.println(eachenteredRequirement);
						sprintDAO.updateSprintRequirementDescription(
								eachenteredRequirement.getSprintRequirementDescription(),
								thisRequirement.getSprintRequirementId());
					}

				} catch (Exception e) {
					System.out.println(e);
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			}

		}
		SprintIdProjectId sprintDetailsForResponse = new SprintIdProjectId();

		for (SprintRequirementUpdate eachenteredRequirement : requirementsList) {
			sprintDetailsForResponse.setProjectId(eachenteredRequirement.getProjectId());
			sprintDetailsForResponse.setSprintId(eachenteredRequirement.getSprintId());
		}
		return Response.ok(sprintDetailsForResponse).build();

	}

	@PUT
	@Path("/insertRequirementsSelection")
	public Response insertRequirementsSelection(List<SprintRequirementNameValueDescriptionUser> requirementsList) {
		Long timeStamp = Calendar.getInstance().getTimeInMillis();
		for (SprintRequirementNameValueDescriptionUser eachenteredRequirement : requirementsList) {

			Timestamp timestamp = new Timestamp(timeStamp);
			List<SprintRequirement> listOfRequirements = sprintDAO
					.getRequirementId(eachenteredRequirement.getSprintRequirementName());
			for (SprintRequirement thisRequirement : listOfRequirements) {
				try {
					SelectedSprintRequirement sampleRequirement = new SelectedSprintRequirement();
					sampleRequirement.setProjectId(eachenteredRequirement.getProjectId());
					sampleRequirement.setSprintId(eachenteredRequirement.getSprintId());
					sampleRequirement.setRequirementLevel(eachenteredRequirement.getRequirementLevel());
					sampleRequirement.setUpdatedBy(eachenteredRequirement.getUpdatedBy());
					sampleRequirement.setSprintRequirementId(thisRequirement.getSprintRequirementId());
					sampleRequirement.setLastUpdate(timestamp);
					sprintDAO.insertSelectedSprintRequirement(sampleRequirement);
				} catch (Exception e) {

					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}

				try {
					System.out.println("huu");
					if (eachenteredRequirement.getSprintRequirementDescription() != null) {
						System.out.println(eachenteredRequirement);
						sprintDAO.updateSprintRequirementDescription(
								eachenteredRequirement.getSprintRequirementDescription(),
								thisRequirement.getSprintRequirementId());
					}

				} catch (Exception e) {
					System.out.println(e);
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			}

		}
		SprintIdProjectId sprintDetailsForResponse = new SprintIdProjectId();

		for (SprintRequirementNameValueDescriptionUser eachenteredRequirement : requirementsList) {
			sprintDetailsForResponse.setProjectId(eachenteredRequirement.getProjectId());
			sprintDetailsForResponse.setSprintId(eachenteredRequirement.getSprintId());
		}
		return Response.ok(sprintDetailsForResponse).build();

	}

	@PUT
	@Path("/sprintBriefSummary")
	public Response getSprintBriefSummary(SprintIdProjectId sprint) {
		try {

			List<SprintBriefSummary> sprintSummary = sprintDAO.getSprintBriefSummary(sprint.getProjectId(),
					sprint.getSprintId());
			for (SprintBriefSummary eachSprint : sprintSummary) {

				return Response.ok(eachSprint).build();
			}

		} catch (Exception e) {
			System.out.println(e);
			ErrorMessage error = new ErrorMessage();
			error.setError(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		return Response.status(Status.BAD_REQUEST).entity("error").build();
	}

	@DELETE
	@Path("/deleteSprintParticipants/{sprintId}/{projectId}")
	public Response deleteSprintParticipants(@PathParam("sprintId") Integer sprintId,
			@PathParam("projectId") Integer projectId) throws RedmineException, SQLException {

		try {
			if (sprintDAO.ifSprintParticipationExists(projectId, sprintId) == true) {
				sprintDAO.deleteSprintParticipants(projectId, sprintId);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
		SuccessMessage success = new SuccessMessage();
		success.setSuccess("delete successful");
		return Response.ok(success).build();
	}

	@GET
	@Path("/{id}")
	public Sprint get(@PathParam("id") Integer id) {
		return sprintDAO.findBySprintId(id);
	}
}
