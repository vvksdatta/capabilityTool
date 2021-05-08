package se.bth.didd.wiptool.resources;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.github.rkmk.container.FoldingList;
import com.taskadapter.redmineapi.MembershipManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.TimeEntryManager;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.ProjectFactory;
import com.taskadapter.redmineapi.bean.Role;
import com.taskadapter.redmineapi.bean.RoleFactory;
import com.taskadapter.redmineapi.bean.TimeEntry;

import io.dropwizard.auth.Auth;
import se.bth.didd.wiptool.api.Projects;
import se.bth.didd.wiptool.api.ProjectsList;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeople;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.api.ErrorMessage;
import se.bth.didd.wiptool.api.NewProject;
import se.bth.didd.wiptool.api.NumberOfRolesInProject;
import se.bth.didd.wiptool.api.ProjectIdName;
import se.bth.didd.wiptool.api.ProjectIdProjectNameIssueId;
import se.bth.didd.wiptool.api.ProjectParticipants;
import se.bth.didd.wiptool.api.ProjectSummary;
import se.bth.didd.wiptool.db.ProjectDAO;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/*
 * This servers as the main API resource for projects and projectsSumary
 * entities
 */
public class ProjectResource {

	private String redmineUrl;
	private ProjectDAO projectDAO;

	/*
	 * This constructor is invoked in the run method of the main application and
	 * it maps the redmine configuration values to the variables 'redmineUrl'
	 * and 'apiAcessKey'.
	 */
	public ProjectResource(ProjectDAO projectDAO, String redmineUrl) {
		this.projectDAO = projectDAO;
		this.redmineUrl = redmineUrl;
	}

	
	@GET
	@Path("/summary")

	public List<ProjectSummary> getSummary(@Auth User user) throws RedmineException {
		/*
		 * As specified in jdbi-folder, whenever we need results to be folded,
		 * we need to use FoldingList instead of List. Result will be folded.
		 * Just call getValues on FoldingList will give List<?> values.
		 */
		FoldingList<ProjectSummary> foldedResult = projectDAO.getSummary();
		List<ProjectSummary> projectSummary = foldedResult.getValues();
		// List<String> projectHeiarchyDetails = new ArrayList<String>();
		Map<Integer, List<String>> projectHeiarchyDetails = new HashMap<Integer, List<String>>();

		boolean restart = false;
		int sampleParentProjectId = 0;

		for (int i = 0; i < projectSummary.size(); i++) {
			if (restart) {
				i = 0;
			}

			// parentprojectid for a project
			if (projectSummary.get(i).getParentProjectId() != null) {
				if (restart == false) {
					sampleParentProjectId = projectSummary.get(i).getParentProjectId();
				}

				restart = false;
				for (ProjectSummary eachProject : new ArrayList<>(projectSummary)) {
					if (eachProject.getProjectId().equals(sampleParentProjectId)) {
						// project details of parentProject matched
						List<String> array;
						if (projectHeiarchyDetails.get(projectSummary.get(i).getProjectId()) != null
								&& projectHeiarchyDetails.get(projectSummary.get(i).getProjectId()).size() >= 0) {
							array = projectHeiarchyDetails.get(projectSummary.get(i).getProjectId());
						} else {
							array = new ArrayList<>();
						}

						if (array.size() >= 0) {

							// push parentprojecft name to arry of a project
							array.add(eachProject.getProjectName());
							projectHeiarchyDetails.put(projectSummary.get(i).getProjectId(), array);

							// restart = true;
							// break;
						}
						if (eachProject.getParentProjectId() != null) {
							sampleParentProjectId = eachProject.getParentProjectId();
						}
						/*
						 * if(projectHeiarchyDetails.containsKey(projectSummary.
						 * get(i).getParentProjectId())){ for(Integer
						 * key:projectHeiarchyDetails.keySet()){
						 * if(key.equals(projectSummary.get(i).
						 * getParentProjectId())){ List<String> alreadyEntered=
						 * projectHeiarchyDetails.get(key);
						 * if(alreadyEntered.contains(eachProject.getProjectName
						 * ()) == false){ List<String> array2=
						 * projectHeiarchyDetails.get(key);
						 * array2.add(eachProject.getProjectName());
						 * projectHeiarchyDetails.put(projectSummary.get(i).
						 * getProjectId(),array2 ); } } } }
						 */

					}
				}
			}
		}
		return projectSummary;
	}

	@GET
	@Path("/{id}")
	public Projects getProjectById(@Auth User user, @PathParam("id") Integer id) {
		return projectDAO.findById(id);
	}

	@GET
	@Path("/participants/{id}")
	public List<ProjectParticipants> getProjectParticipants(@Auth User user, @PathParam("id") Integer projectId) {

		return projectDAO.getParticipants(projectId);
	}

	@GET
	@Path("/getAllProjects")
	public List<ProjectIdName> getAllProjects(@Auth User user) {

		return projectDAO.getAllProjects();
	}

	public void nestingSubProjectsFunction(List<ProjectsList> currentProjectsList, List<ProjectIdName> childProjects) {

		for (ProjectsList eachSubproject : currentProjectsList) {
			ProjectIdName projectIdName = new ProjectIdName();
			projectIdName.setProjectId(eachSubproject.getProjectId());
			projectIdName.setProjectName(eachSubproject.getProjectName());

			if (projectDAO.subProjectsExist(eachSubproject.getProjectId())) {
				eachSubproject.setProjectList(projectDAO.getSubProjectsList(eachSubproject.getProjectId()));
			}
			if (childProjects.contains(projectIdName)) {
				continue;
			} else {
				if (projectDAO.subProjectsExist(eachSubproject.getProjectId())) {
					currentProjectsList = eachSubproject.getProjectList();
					nestingSubProjectsFunction(currentProjectsList, childProjects);
				}
			}
		}

	}

	@GET
	@Path("/getAllProjectsNested")
	public Response getAllProjectsNested(@Auth User user) {
		/*
		 * For creating a nested list of projects, we use the projectsList class
		 * that has projectId, projectName and a list of projectsList(using same
		 * class). using the initial list of independent projects (i.e. projects
		 * that are not sub projects of any other projects), we fetch the list
		 * of sub projects for each independent project. After setting the
		 * List<ProjectsList> using the fetched list, we then inspect whether
		 * any of the projects in the former list have sub projects. If so, same
		 * as in previous step a list<ProjectsList> is fetched and set. Thus,
		 * this loop runs over all independent projects, until a project matches
		 * the childProject present in childProjects( childProject is the
		 * project that doesn't have any other sub projects.)
		 */
		List<ProjectIdName> independentProjects = projectDAO.getAllIndependentProjects();
		List<ProjectIdName> childProjects = projectDAO.getAllChildProjects();
		List<ProjectsList> listOfProjects = new ArrayList<ProjectsList>();
		for (ProjectIdName eachIndependentProject : independentProjects) {

			ProjectsList projectDetails = new ProjectsList();
			projectDetails.setProjectId(eachIndependentProject.getProjectId());
			projectDetails.setProjectName(eachIndependentProject.getProjectName());

			if (projectDAO.subProjectsExist(eachIndependentProject.getProjectId())) {
				projectDetails.setProjectList(projectDAO.getSubProjectsList(eachIndependentProject.getProjectId()));
				List<ProjectsList> currentProjectsList;
				currentProjectsList = projectDetails.getProjectList();

				if (currentProjectsList.size() > 0) {

					try {
						nestingSubProjectsFunction(currentProjectsList, childProjects);
					} catch (Exception e) {
						System.out.println(e);
						return Response.status(Status.BAD_REQUEST).entity(e).build();
					}

				}

			}
			listOfProjects.add(projectDetails);
		}

		return Response.ok(listOfProjects).build();
	}
	
	@GET
	@Path("getDetailsOfProjectsWithTimeLogs/{userId}")
	public Response getDeatilsOfProjectsWithTimeLogs(@Auth User user, @PathParam("userId") int userId) {
		String apiKey;
		try {
			 apiKey = projectDAO.getApiKeyOfUser(userId).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
		TimeEntryManager timeEntryManager = redmineManager.getTimeEntryManager();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2); // to get previous 2 years 
		java.util.Date dateTwoYearsBack = cal.getTime();
		try {
			List<TimeEntry> allTimeEntryDetails = timeEntryManager.getTimeEntries();
			ArrayList<ProjectIdProjectNameIssueId> listOfIssues = new ArrayList<ProjectIdProjectNameIssueId>();
		for(TimeEntry eachTimeEntry : allTimeEntryDetails) {
			if(eachTimeEntry.getUpdatedOn().after(dateTwoYearsBack)) {
				ProjectIdProjectNameIssueId eachIssueDetail = new ProjectIdProjectNameIssueId( eachTimeEntry.getProjectId(),eachTimeEntry.getProjectName(), eachTimeEntry.getIssueId());
				listOfIssues.add(eachIssueDetail);
			}
		}
		return Response.ok(listOfIssues).build();
		
		} catch (RedmineException e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
	}

	@GET
	@Path("getNumberofParticipants/{id}")
	public Response getNumberofParticipants(@Auth User user, @PathParam("id") Integer projectId) {
		List<NumberOfRolesInProject> numberOfRoles = new ArrayList<NumberOfRolesInProject>();
		try {
			List<Roles> exisitingRoles = projectDAO.getNumberofParticipants(projectId);
			List<Roles> distinctRoles = projectDAO.getDistinctRolesInProject(projectId);

			for (Roles eachDistinctRole : distinctRoles) {
				int count = 0;
				NumberOfRolesInProject sampleNumber = new NumberOfRolesInProject();
				sampleNumber.setRoleId(eachDistinctRole.getRoleId());
				sampleNumber.setRoleName(eachDistinctRole.getRoleName());
				for (Roles eachRole : exisitingRoles) {
					if (eachDistinctRole.getRoleId().equals(eachRole.getRoleId())) {
						count = (count + 1);
					}
				}
				sampleNumber.setNumberOfPeople(count);
				numberOfRoles.add(sampleNumber);
			}
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		return Response.ok(numberOfRoles).build();
	}

	@RolesAllowed({ UserRoles.ROLE_ONE})
	@PUT
	@Path("/newproject")
	public Response newProject(@Auth User user, NewProject newproject) throws RedmineException, SQLException {
		/*
		 * This method aims at creating a new project on redmine using the
		 * details provided from the frontend application. Besides updating
		 * redmine, the details of new project are also updated in the database.
		 */
		String apiKey;
		try {
			 apiKey = projectDAO.getApiKeyOfUser(newproject.getUserId()).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
		Project project = ProjectFactory.create();
		Long timeStamp = Calendar.getInstance().getTimeInMillis();
		String key = newproject.getProjectName().toLowerCase().replaceAll("\\s+", "") + timeStamp;
		project.setIdentifier(key);
		project.setName(newproject.getProjectName());
		project.setDescription(newproject.getProjectDescription());
		project.setParentId(newproject.getParentProjectId());
		Project createdProject;
		try {
			createdProject = redmineManager.getProjectManager().createProject(project);
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
		Projects createNewProject = new Projects();

		createNewProject.setProjectName(newproject.getProjectName());
		createNewProject.setProjectDescription(newproject.getProjectDescription());
		createNewProject.setProjectStartDate(newproject.getProjectStartDate());
		createNewProject.setProjectEndDate(newproject.getProjectEndDate());
		createNewProject.setProjectUpdatedBy(newproject.getProjectUpdatedBy());
		createNewProject.setProjectLeader(newproject.getProjectLeader());
		createNewProject.setProjectEstimatedEffort(newproject.getProjectEstimatedEffort());
		createNewProject.setProjectId(createdProject.getId());
		createNewProject.setParentProjectId(newproject.getParentProjectId());
		createNewProject.setProjectLastUpdate(createdProject.getUpdatedOn());
		/*
		 * project status 'true' indicates an active project. By default, each
		 * newly created project is shown to be active
		 */
		createNewProject.setProjectStatus("true");
		// createNewProject.setProjectUpdatedBy("userOne");
		createNewProject.setRedmineLastUpdate(createdProject.getUpdatedOn());
		projectDAO.createProject(createNewProject);
		return Response.ok().entity(createNewProject).build();
	}

	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/setProjectParticipants/{projectId}/{userId}")
	public Response setProjectParticipants(@Auth User user, List<RolesOfPeople> rolesOfPeople,
			@PathParam("projectId") Integer projectId, @PathParam("userId") Integer userId) throws RedmineException, SQLException {
		String apiKey;
		try {
			 apiKey = projectDAO.getApiKeyOfUser(userId).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
		MembershipManager membershipManager = redmineManager.getMembershipManager();
		for (RolesOfPeople person : rolesOfPeople) {
			try {

				List<Roles> role = projectDAO.getRoleName(person.getRoleId());
				for (Roles roleOfPerson : role) {

					Collection<Role> allRoles = Arrays
							.asList(new Role[] { RoleFactory.create(roleOfPerson.getRoleId()), });

					try {
						membershipManager.createMembershipForUser(projectId, person.getPersonId(), allRoles);
					} catch (Exception e) {
						return Response.status(Status.BAD_REQUEST).entity(e).build();
					}
				}

				projectDAO.insertIntoProjectParticipation(projectId, person.getPersonId(), person.getRoleId());
			} catch (Exception e) {
				System.out.println(e.toString());
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}
		}
		return Response.ok().build();
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/updateProjectParticipants/{projectId}/{userId}")
	public Response updateProjectParticipants(@Auth User user, List<RolesOfPeople> rolesOfPeople,
			@PathParam("projectId") Integer projectId, @PathParam("userId") Integer userId)
			throws RedmineException, SQLException {
		String apiKey;
		try {
			apiKey = projectDAO.getApiKeyOfUser(userId).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
		MembershipManager membershipManager = redmineManager.getMembershipManager();
		try {
			List<Membership> members = membershipManager.getMemberships(projectId);
			for (Membership member : members) {
				try {
					membershipManager.delete(member);
				} catch (Exception e) {
					continue;
				}
			}
			projectDAO.deleteAllParticipants(projectId);
			for (RolesOfPeople person : rolesOfPeople) {
				Collection<Role> allRoles = Arrays.asList(new Role[] { RoleFactory.create(person.getRoleId()), });
				try {
					membershipManager.createMembershipForUser(projectId, person.getPersonId(), allRoles);

					if (projectDAO.ifPersonParticipatesInProject(projectId, person.getPersonId(),
							person.getRoleId()) == false) {
						projectDAO.insertIntoProjectParticipation(projectId, person.getPersonId(), person.getRoleId());
					}

				} catch (Exception e) {
					// System.out.println(e);
					// e.printStackTrace();
					if (projectDAO.ifPersonParticipatesInProject(projectId, person.getPersonId(),
							person.getRoleId()) == false) {
						projectDAO.insertIntoProjectParticipation(projectId, person.getPersonId(), person.getRoleId());
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

		SuccessMessage success = new SuccessMessage();
		success.setSuccess("update successful");
		return Response.ok(success).build();
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/rolesOfPeopleInProject")
	public Response rolesOfPeopleInProject(@Auth User user, Integer projectId) throws RedmineException {
		try {
			List<RolesOfPeople> rolesOfPeople = projectDAO.getRolesOfPeopleInProject(projectId);
			Collections.sort(rolesOfPeople, new Comparator<RolesOfPeople>() {
			    public int compare(RolesOfPeople v1, RolesOfPeople v2) {
			        return v1.getPersonName().compareTo(v2.getPersonName());
			    }
			});
			return Response.ok(rolesOfPeople).build();
		} catch (Exception e) {
			System.out.println(e);
			ErrorMessage error = new ErrorMessage();
			error.setError(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/updateProject")
	public Response newProject(@Auth User user, Projects updateProject) throws RedmineException {
		/*
		 * This method aims at updaing a project using the details provided from
		 * the frontend application. Besides updating redmine, the details of
		 * project are also updated in the database.
		 */
		String apiKey;
		try {
			 apiKey = projectDAO.getApiKeyOfUser(updateProject.getUserId()).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
		Project project = redmineManager.getProjectManager().getProjectById(updateProject.projectId);
		Long timeStamp = Calendar.getInstance().getTimeInMillis();
		project.setName(updateProject.getProjectName());
		project.setDescription(updateProject.getProjectDescription());
		project.setParentId(updateProject.getParentProjectId());
		try {
			redmineManager.getProjectManager().update(project);

		} catch (RedmineException updateException) {
			Date date = new Date(timeStamp);
			updateProject.setProjectLastUpdate(date);
			projectDAO.updateProject(updateProject);
			return Response.status(Status.BAD_REQUEST).entity(updateException).build();
		}
		Project updatedProject = redmineManager.getProjectManager().getProjectById(updateProject.projectId);

		updateProject.setProjectLastUpdate(updatedProject.getUpdatedOn());
		// createNewProject.setProjectUpdatedBy("userOne");
		updateProject.setRedmineLastUpdate(updatedProject.getUpdatedOn());
		projectDAO.updateProject(updateProject);
		return Response.ok().build();

	}

}
