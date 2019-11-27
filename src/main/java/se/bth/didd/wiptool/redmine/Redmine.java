package se.bth.didd.wiptool.redmine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Role;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;
import se.bth.didd.wiptool.api.IssueTemplate;
import se.bth.didd.wiptool.api.IssueUpdateTemplate;
import se.bth.didd.wiptool.api.IssuesTemplateForRedmineAPI;
import se.bth.didd.wiptool.api.ProjectIdName;
import se.bth.didd.wiptool.api.ProjectLeaderId;
import se.bth.didd.wiptool.api.Projects;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeopleSprint;
import se.bth.didd.wiptool.api.SharedSprint;
import se.bth.didd.wiptool.api.Sprint;
import se.bth.didd.wiptool.api.SprintComprisingIssues;
import se.bth.didd.wiptool.api.SprintNameProgress;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.db.RedmineDAO;

/*
 * The Redmine class contains methods for creating and updating various entities like projects, sprints and issues on Redmine. 
 * Further, it also holds methods for retrieving data from Redmine and updating the database.
 
*/
@Path("/redmine")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class Redmine {

	RedmineDAO redmineDAO;

	private String redmineUrl;
	private String projectIdCutOff;
	private String issueIdCutOff;

	public Redmine(RedmineDAO redmineDAO, String redmineUrl, String projectIdCutOff, String issueIdCutOff) {
		this.redmineDAO = redmineDAO;
		this.redmineUrl = redmineUrl;
		this.projectIdCutOff = projectIdCutOff;
		this.issueIdCutOff = issueIdCutOff;
	}

	public Redmine() {

	}

	@Path("/{userId}")
	@GET
	public Response synchronizingWithRedmine(@PathParam("userId") Integer userId)
			throws RedmineException, IOException, JSONException {

		/*
		 * Generating a random string for updating the identifiers on each
		 * iteration. These identifiers are used to remove the entities from
		 * database that no longer exist on Redmine
		 */

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		String generatedRandomString = "";
		Random random = new Random();
		int randomLen = 5;
		for (int i = 0; i < randomLen; i++) {
			char c = alphabet.charAt(random.nextInt(26));
			generatedRandomString += c;
		}
		/* fetch the unique API access key of the user using userId. */
		String apiKey;
		try {
			apiKey = redmineDAO.getApiKeyOfUser(userId).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}

		/*
		 * Fetch list of project participants from each project in redmine. If
		 * person is not already added, add as a new person.Else if the details
		 * are modified, update the details of person
		 */
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);

		/*
		 * Setting the limit for number of objects to be retrieved per page. The
		 * default value is only 25
		 */
		redmineManager.setObjectsPerPage(100);

		List<User> people = null;
		try {
			people = redmineManager.getUserManager().getUsers();
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}

		for (User person : people) {

			if (redmineDAO.ifPersonIdExists(person.getId()) != true) {
				redmineDAO.insertIntoPeopleTable(person.getId(), person.getFullName(), person.getFirstName(),
						person.getLastName(), person.getMail(), generatedRandomString);
				// * person.getFirstName() person.getLastName()
			} else {
				if (redmineDAO.ifPersonDetailsModified(person.getId(), person.getFullName(), person.getFirstName(),
						person.getLastName(), person.getMail()) == false) {
					redmineDAO.updatePeopleTable(person.getId(), person.getFullName(), person.getFirstName(),
							person.getLastName(), person.getMail());
				}
				/*
				 * Setting a random status identifier for each existing person
				 * on redmine. This is to check and maintatin the same people on
				 * local database also. This is helpful in deleting the
				 * people(deleting from local database) who have been removed
				 * from Redmine.
				 */
				redmineDAO.updatePersonIdentifier(person.getId(), generatedRandomString);
				redmineDAO.updateidentifierForAllRolesInRolesOfPeople(person.getId(), generatedRandomString);
				redmineDAO.updateidentifierInAssessmentOfCapabilities(person.getId(), generatedRandomString);
				redmineDAO.updateidentifierInAssessmentOfSkills(person.getId(), generatedRandomString);
			}
		}

		/*
		 * fetch list of roles and add them to database. Add a new role if it is
		 * not present in ROLESDB table
		 */
		List<Role> roles = redmineManager.getUserManager().getRoles();
		for (Role role : roles) {
			if(role.getId() != 5){
			if (redmineDAO.ifRoleIdExists(role.getId()) != true)  {

				Roles newRoles = new Roles();
				newRoles.setRoleId(role.getId());
				newRoles.setRoleName(role.getName());
				redmineDAO.insertIntoRolesDB(newRoles);
				// System.out.println("Inserted new roles ");

			} else {
				/* Handling modifications to role names on Redmine */
				if (redmineDAO.ifRoleExists(role.getId(), role.getName()) != true) {
					Roles newRoles = new Roles();
					newRoles.setRoleId(role.getId());
					newRoles.setRoleName(role.getName());
					redmineDAO.updateRolesDB(newRoles);
					// System.out.println("Updated roles database");
				}
			}
			/*
			 * Setting an identifier for each role. This is to check and remove
			 * roles that have been deleted from Redmine.
			 */
		}
			redmineDAO.updateRoleStatus(role.getId(), generatedRandomString);
		}

		/* update projects,sprints etc */
		int projectCutOff = Integer.parseInt(projectIdCutOff);
		int issueCutOff = Integer.parseInt(issueIdCutOff);
		List<Project> projects = redmineManager.getProjectManager().getProjects();
		for (Project redmineProject : projects) {
			if (redmineProject.getId() >= projectCutOff) {

				/* check if project already added to database */
				if (redmineDAO.ifProjectExists(redmineProject.getId()) != true) {
					/*
					 * The current project doesn't exist on local database
					 */ Projects newProject = new Projects();
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					newProject.setProjectId(redmineProject.getId());
					newProject.setProjectName(redmineProject.getName());
					newProject.setProjectDescription(redmineProject.getDescription());
					newProject.setProjectStatus("true");
					newProject.setProjectLastUpdate(timestamp);
					newProject.setRedmineLastUpdate(redmineProject.getUpdatedOn());
					newProject.setParentProjectId(redmineProject.getParentId());
					redmineDAO.insertIntoProjects(newProject);

					/*
					 * for each project, fetch the list of sprints associated
					 * with the project
					 */
					List<Version> sprints = redmineManager.getProjectManager().getVersions(redmineProject.getId());
					for (Version sprint : sprints) {
						if (sprint.getProjectId() >= projectCutOff) {
							/*
							 * check if a sprint is already added to the list of
							 * sprints. If false, add as a new sprint
							 */
							
							if (redmineDAO.ifSprintExists(sprint.getProjectId(), sprint.getId()) != true) {
								
								/*
								 * A sprint can sometimes be hierarchical i.e
								 * the sprints of sub projects can be shared
								 * with parent project. To handle this, add
								 * project details if they don't exist in
								 * projects table
								 */
								if (redmineDAO.ifProjectExists(sprint.getProjectId()) != true) {
									Project project = redmineManager.getProjectManager()
											.getProjectById(sprint.getProjectId());
									newProject.setProjectId(project.getId());
									newProject.setProjectName(project.getName());
									newProject.setProjectDescription(project.getDescription());
									newProject.setProjectStatus("true");
									newProject.setProjectLastUpdate(timestamp);
									newProject.setRedmineLastUpdate(project.getUpdatedOn());
									newProject.setParentProjectId(project.getParentId());
									redmineDAO.insertIntoProjects(newProject);
								}

								Sprint newSprint = new Sprint();
								newSprint.setProjectId(sprint.getProjectId());
								newSprint.setSprintId(sprint.getId());
								newSprint.setSprintName(sprint.getName());
								newSprint.setSprintDescription(sprint.getDescription());
								newSprint.setSprintEndDate(sprint.getDueDate());
								newSprint.setSprintStatus(sprint.getStatus());
								newSprint.setSprintLastUpdate(timestamp);
								newSprint.setSprintRedmineUpdate(sprint.getUpdatedOn());
								redmineDAO.insertIntoSprints(newSprint);
								// System.out.println("Updated new sprint
								// details to
								// database");
							}
							
							if(redmineDAO.IfsprintAssociationExists(sprint.getProjectId(), sprint.getId(), redmineProject.getId()) != true){
								SharedSprint sharedSprint = new SharedSprint();
								sharedSprint.setParentProjectId(sprint.getProjectId());
								sharedSprint.setSprintId(sprint.getId());
								sharedSprint.setAssociatedProjectId(redmineProject.getId());
								redmineDAO.insertSharedSprints(sharedSprint);
							}
						}
					}

					/* Retrieve the list of project participants */
					List<Membership> projectParticipants = redmineManager.getMembershipManager()
							.getMemberships(redmineProject.getId());
					for (Membership projectParticipant : projectParticipants) {
						/*
						 * The userId for a group is null. The list of
						 * 'projectParticipants' presents all the participants
						 * including the ones in a group. Thus, we ignore the
						 * details of group
						 */
						if (projectParticipant.getUserId() != null) {
							/*
							 * for each project participant first we retrieve
							 * the list of roles he/she is allocated on each
							 * project
							 */
							Collection<Role> rolesOfProjectParticipant = projectParticipant.getRoles();
							for (Role role : rolesOfProjectParticipant) {
								if(role.getId() != 5){
								
								/*
								 * check whether the person is already added to
								 * People table. If the person is not added but
								 * exists in the project participation list, it
								 * indicates that the person account is locked.
								 * This person is first added to list of people
								 * and then added to other tables.
								 */
								if (redmineDAO.ifPersonIdExists(projectParticipant.getUserId()) != true) {

									User lockedUser = redmineManager.getUserManager()
											.getUserById(projectParticipant.getUserId());
									redmineDAO.insertIntoPeopleTable(lockedUser.getId(), lockedUser.getFullName(),
											lockedUser.getFirstName(), lockedUser.getLastName(), lockedUser.getMail(),
											generatedRandomString);

								}

								/*
								 * check if the role is already added to the
								 * table that displays the list of roles each
								 * person is capable of taking up (RolesOfPeople
								 * Table)
								 */
								if (redmineDAO.ifRoleOfPersonExists(role.getId(),
										projectParticipant.getUserId()) != true) {

									/*
									 * If there is no registered record in
									 * database, insert a new record to the
									 * table with the roleId along with personId
									 */
									redmineDAO.insertIntoRolesOfPeopleTable(projectParticipant.getUserId(),
											role.getId());
									// System.out.println("Updated RolesofPeople
									// table");

								}

								/*
								 * check if the project participation of the
								 * person is already added to the database
								 * (ProjectParticipation Table)
								 */

								/*
								 * Since in each project a person can be
								 * assigned only a single role, here we check
								 * whether a person is already added to project
								 * participation Table. If person already added,
								 * no new roles will be assigned to the person
								 * in the current project.
								 */

								/*
								 * Note a possible scenario: Consider two
								 * projects 'project1' and 'project2'. Say
								 * 'project2' consists of several issues tagged
								 * under special category and say an
								 * issue('issue1') among these is assigned to
								 * 'person1'. Now considering that 'person1' is
								 * not a member of 'project1', if we move the
								 * 'issue1' from 'project2' to 'project1' and
								 * assign 'issue1' to a target version in
								 * 'project1', then the 'person1' will be a
								 * member of a sprint without actually being a
								 * member of 'project1'. In such case, even
								 * Redmine doesn't represent the person to be
								 * part of the project. Thus, this person
								 * doesn't bear a role within this project. So,
								 * if a sprint shows people with no role, it
								 * indicates that an issue that was allocated to
								 * a person was moved from one project to
								 * another.
								 */
								if (redmineDAO.ifPersonExistsInProject(redmineProject.getId(),
										projectParticipant.getUserId()) != true) {
									if (redmineDAO.ifPersonParticipatesInProject(redmineProject.getId(),
											projectParticipant.getUserId(), role.getId()) != true) {
										/*
										 * If there is no registered record in
										 * database, insert a new record to the
										 * table with the projectId along with
										 * personId and roleId
										 */
										redmineDAO.insertIntoProjectParticipation(redmineProject.getId(),
												projectParticipant.getUserId(), role.getId());
										// System.out.println("Updated
										// ProjectParticipation table");
									}
								}
								 }
								redmineDAO.updateRedmineProjectIdentifierInParticipationTable(redmineProject.getId(),
										projectParticipant.getUserId(), generatedRandomString);

								redmineDAO.updateRedminePersonIdentifierInParticipationTable(redmineProject.getId(),
										projectParticipant.getUserId(), role.getId(), generatedRandomString);

								redmineDAO.updateidentifierInRolesOfPeople(projectParticipant.getUserId(), role.getId(),
										generatedRandomString);
								redmineDAO.updatePersonIdentifier(projectParticipant.getUserId(),
										generatedRandomString);
								redmineDAO.updateidentifierInAssessmentOfCapabilities(projectParticipant.getUserId(),
										generatedRandomString);
								redmineDAO.updateidentifierInAssessmentOfSkills(projectParticipant.getUserId(),
										generatedRandomString);

							}

						}
					}
				}
			}
		}

		/*
		 * The loop for inserting new projects has been isolated from the loop
		 * for inserting new issues. The reason for this can be understood from
		 * a simple example. consider a project (project1) which has a sub
		 * project (child1). Further, consider the case where this 'child1'
		 * project has another subproject (grancChild1). When an issue is
		 * created in this 'grandChild1' project, on Redmine, the same issue
		 * will be listed automatically under both the parent projects, i.e.
		 * Redmine lists the issue under both 'child1' and 'project1' projects.
		 * So, if issues are updated in the same loop where new projects are
		 * inserted, there is a chance for foreign key conflicts when a
		 * 'grandChildproject1' details are not yet registered to the database.
		 */
		List<IssueTemplate> redmineLastUpdatedTime = redmineDAO.selectLastUpdatedTime();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);

		// This parameter should be adjusted based on the time-zone to which the
		// Redmine server is set
		formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		for (Project redmineProject : projects) {
			if (redmineProject.getId() >= projectCutOff) {
				/* check if project already added to database */
				if(redmineProject.getId() != 179){
				if (redmineDAO.ifProjectExists(redmineProject.getId()) != false) {

					/*
					 * The current project is already added as a record on
					 * projects table of local database
					 */

					/*
					 * check whether project leader is still a registered user
					 * on Redmine. Remove the project leader details of a
					 * project if the person is removed from redmine.
					 */

					if (redmineDAO.ifProjectLeaderExists(redmineProject.getId()) == true) {
						List<ProjectLeaderId> currentProjectLeader = redmineDAO
								.getProjectLeader(redmineProject.getId());
						for (ProjectLeaderId leader : currentProjectLeader) {
							/*
							 * If the person registered on local database is not
							 * found on Redmine, update projectLeaderIdentifier
							 * for that project and remove project leader
							 * details for that project. This is done by trying
							 * to access the user details on Redmine and if the
							 * details are not found, then the catch bloc
							 * executes
							 */

							try {
								redmineManager.getUserManager().getUserById(leader.getProjectLeader());
								redmineDAO.updatePersonIdentifier(leader.getProjectLeader(), generatedRandomString);
								redmineDAO.updateidentifierInAssessmentOfCapabilities(leader.getProjectLeader(),
										generatedRandomString);
								redmineDAO.updateidentifierInAssessmentOfSkills(leader.getProjectLeader(),
										generatedRandomString);
								redmineDAO.updateidentifierForAllRolesInRolesOfPeople(leader.getProjectLeader(),
										generatedRandomString);

							} catch (Exception e) {
								redmineDAO.updateProjectLeaderIdentifier(redmineProject.getId(), "delete");
							}

						}
					}

					/*
					 * Check whether the current project details have been
					 * modified since last recorded entry on database(lastUpdate
					 * time stamp in projects table)
					 */

					if (redmineDAO.ifProjectDetailsUnModified(redmineProject.getId(),
							redmineProject.getUpdatedOn()) == false) {
						if (redmineProject.getParentId() == null) {
							redmineDAO.updateProjectWithoutParentProject(redmineProject.getId(),
									redmineProject.getName(), redmineProject.getDescription(),
									redmineProject.getUpdatedOn(), timestamp);
						} else {
							redmineDAO.updateProject(redmineProject.getId(), redmineProject.getParentId(),
									redmineProject.getName(), redmineProject.getDescription(),
									redmineProject.getUpdatedOn(), timestamp);
						}

					}
					List<Version> sprints = redmineManager.getProjectManager().getVersions(redmineProject.getId());
					for (Version sprint : sprints) {
						if (sprint.getProjectId() >= projectCutOff) {

							/*
							 * check if a sprint is already added to the list of
							 * sprints. If false, add as a new sprint
							 */
							if (redmineDAO.ifSprintExists(sprint.getProjectId(), sprint.getId()) != true) {
								Sprint newSprint = new Sprint();
								newSprint.setProjectId(sprint.getProjectId());
								newSprint.setSprintId(sprint.getId());
								newSprint.setSprintName(sprint.getName());
								newSprint.setSprintDescription(sprint.getDescription());
								newSprint.setSprintEndDate(sprint.getDueDate());
								newSprint.setSprintStatus(sprint.getStatus());
								newSprint.setSprintLastUpdate(timestamp);
								newSprint.setSprintRedmineUpdate(sprint.getUpdatedOn());
								redmineDAO.insertIntoSprints(newSprint);
								// System.out.println("Updated new sprint
								// details to
								// database");
							}
							
							if(redmineDAO.IfsprintAssociationExists(sprint.getProjectId(), sprint.getId(), redmineProject.getId()) != true){
								SharedSprint sharedSprint = new SharedSprint();
								sharedSprint.setParentProjectId(sprint.getProjectId());
								sharedSprint.setSprintId(sprint.getId());
								sharedSprint.setAssociatedProjectId(redmineProject.getId());
								redmineDAO.insertSharedSprints(sharedSprint);
							}
							/*
							 * Check whether the current sprint details on
							 * Redmine have been modified since last recorded
							 * entry on database(lastUpdate time stamp in
							 * sprints table)
							 */
							if (redmineDAO.ifSprintDetailsUnModified(sprint.getProjectId(), sprint.getId(),
									sprint.getUpdatedOn()) == false) {

								Sprint newSprint = new Sprint();
								newSprint.setProjectId(sprint.getProjectId());
								newSprint.setSprintId(sprint.getId());
								newSprint.setSprintName(sprint.getName());
								newSprint.setSprintDescription(sprint.getDescription());
								newSprint.setSprintEndDate(sprint.getDueDate());
								newSprint.setSprintStatus(sprint.getStatus());
								newSprint.setSprintLastUpdate(timestamp);
								newSprint.setSprintRedmineUpdate(sprint.getUpdatedOn());
								redmineDAO.updateSprintModifications(newSprint);

							}

							redmineDAO.updateIdentifiersInSprint(sprint.getProjectId(), sprint.getId(),
									generatedRandomString, generatedRandomString);
							redmineDAO.updateIdentifiersInSharedSprint(sprint.getProjectId(), sprint.getId(),redmineProject.getId(),
									generatedRandomString, generatedRandomString, generatedRandomString);
							redmineDAO.updateIdentifiersInSprintQuestionnaire(sprint.getProjectId(), sprint.getId(),
									generatedRandomString, generatedRandomString);
							redmineDAO.updateIdentifiersInSprintRequirement(sprint.getProjectId(), sprint.getId(),
									generatedRandomString, generatedRandomString);
							redmineDAO.updateIdentifiersInSprintAssets(sprint.getProjectId(), sprint.getId(),
									generatedRandomString, generatedRandomString);
							redmineDAO.updateIdentifiersInSprintDomains(sprint.getProjectId(), sprint.getId(),
									generatedRandomString, generatedRandomString);
							redmineDAO.updateIdentifiersInSprintDevEnvironments(sprint.getProjectId(), sprint.getId(),
									generatedRandomString, generatedRandomString);

						}
					}

					/* Retrieve the list of project participants */
					List<Membership> projectParticipants = redmineManager.getMembershipManager()
							.getMemberships(redmineProject.getId());

					for (Membership projectParticipant : projectParticipants) {

						/*
						 * The userId for a group is null. The list of
						 * 'projectParticipants' presents all the participants
						 * including the ones in a group. Thus, we ignore the
						 * details of group
						 */
						if (projectParticipant.getUserId() != null) {
							/*
							 * for each project participant first we retrieve
							 * the list of roles he/she is allocated on each
							 * project
							 */
							Collection<Role> rolesOfProjectParticipant = projectParticipant.getRoles();
							for (Role role : rolesOfProjectParticipant) {
								if(role.getId() != 5){
								/*
								 * check whether the person is already added to
								 * People table. If the person is not added but
								 * exists in the project participation list, it
								 * indicates that the person account is locked.
								 * This person is first added to list of people
								 * and then added to other tables.
								 */

								if (redmineDAO.ifPersonIdExists(projectParticipant.getUserId()) != true) {

									User lockedUser = redmineManager.getUserManager()
											.getUserById(projectParticipant.getUserId());
									redmineDAO.insertIntoPeopleTable(lockedUser.getId(), lockedUser.getFullName(),
											lockedUser.getFirstName(), lockedUser.getLastName(), lockedUser.getMail(),
											generatedRandomString);
									redmineDAO.updatePersonIdentifier(lockedUser.getId(), generatedRandomString);
									redmineDAO.updateidentifierInAssessmentOfCapabilities(lockedUser.getId(),
											generatedRandomString);
									redmineDAO.updateidentifierInAssessmentOfSkills(lockedUser.getId(),
											generatedRandomString);
								}

								/*
								 * check if the role is already added to the
								 * table that displays the list of roles each
								 * person is capable of taking up (RolesOfPeople
								 * Table)
								 */
								if (redmineDAO.ifRoleOfPersonExists(role.getId(),
										projectParticipant.getUserId()) != true) {

									/*
									 * If there is no registered record in
									 * database, insert a new record to the
									 * table with the roleId along with personId
									 */
									redmineDAO.insertIntoRolesOfPeopleTable(projectParticipant.getUserId(),
											role.getId());

								}

								/*
								 * check if the project participation of the
								 * person is already added to the database
								 * (ProjectParticipation Table)
								 */
								if (redmineDAO.ifPersonExistsInProject(redmineProject.getId(),
										projectParticipant.getUserId()) != true) {

									if (redmineDAO.ifPersonParticipatesInProject(redmineProject.getId(),
											projectParticipant.getUserId(), role.getId()) != true) {
										/*
										 * If there is no registered record in
										 * database, insert a new record to the
										 * table with the projectId along with
										 * personId and roleId
										 */
										redmineDAO.insertIntoProjectParticipation(redmineProject.getId(),
												projectParticipant.getUserId(), role.getId());
									}
								}
								}
								redmineDAO.updateRedmineProjectIdentifierInParticipationTable(redmineProject.getId(),
										projectParticipant.getUserId(), generatedRandomString);

								redmineDAO.updateRedminePersonIdentifierInParticipationTable(redmineProject.getId(),
										projectParticipant.getUserId(), role.getId(), generatedRandomString);

								redmineDAO.updateidentifierInRolesOfPeople(projectParticipant.getUserId(), role.getId(),
										generatedRandomString);

								redmineDAO.updatePersonIdentifier(projectParticipant.getUserId(),
										generatedRandomString);
								redmineDAO.updateidentifierInAssessmentOfCapabilities(projectParticipant.getUserId(),
										generatedRandomString);
								redmineDAO.updateidentifierInAssessmentOfSkills(projectParticipant.getUserId(),
										generatedRandomString);

							}

						}
					}

					/*
					 * fetch all the issues using custom params and add
					 * to the list of issues
					 */
					List<IssuesTemplateForRedmineAPI> listOfIssues = new ArrayList<IssuesTemplateForRedmineAPI>();
					if (redmineLastUpdatedTime.size() != 0) {
						String redmineLastUpdatedTimeStamp = formatDate
								.format(redmineLastUpdatedTime.get(0).getRedmineLastUpdate());
						String url = redmineUrl + "issues.json?updated_on=%3E%3D" + redmineLastUpdatedTimeStamp
								+ "&project_id=" + redmineProject.getId() + "&limit=200&&status_id=*";
						URL obj = new URL(url);
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();
						con.setRequestMethod("GET");
						// add request header
						con.setRequestProperty("User-Agent", "CAST API");
						con.setRequestProperty("Content-Type", "application/json");
						con.setRequestProperty("X-Redmine-API-Key", apiKey);
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						JSONObject myResponse = new JSONObject(response.toString());
						ArrayList<String> list = new ArrayList<String>();
						Gson gson = new Gson();
						JSONArray issuesFetched = myResponse.getJSONArray("issues");
						if (issuesFetched != null) {
							int len = issuesFetched.length();
							for (int i = 0; i < len; i++) {
								list.add(issuesFetched.get(i).toString());
							}
						}
						for (String eachIssue : list) {
							IssuesTemplateForRedmineAPI issue = gson.fromJson(eachIssue,
									IssuesTemplateForRedmineAPI.class);
							listOfIssues.add(issue);
						}
					} else {
						String url = redmineUrl + "issues.json?project_id=" + redmineProject.getId()
								+ "&limit=200&&status_id=*";
						URL obj = new URL(url);
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();
						con.setRequestMethod("GET");
						// add request header
						con.setRequestProperty("User-Agent", "CAST API");
						con.setRequestProperty("Content-Type", "application/json");
						con.setRequestProperty("X-Redmine-API-Key", apiKey);
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						JSONObject myResponse = new JSONObject(response.toString());
						ArrayList<String> list = new ArrayList<String>();
						Gson gson = new Gson();
						JSONArray issuesFetched = myResponse.getJSONArray("issues");
						if (issuesFetched != null) {
							int len = issuesFetched.length();
							for (int i = 0; i < len; i++) {
								list.add(issuesFetched.get(i).toString());
							}
						}
						for (String eachIssue : list) {
							IssuesTemplateForRedmineAPI issue = gson.fromJson(eachIssue,
									IssuesTemplateForRedmineAPI.class);
							listOfIssues.add(issue);
						}
					}

					for (IssuesTemplateForRedmineAPI issue : listOfIssues) {
						
						if (issue.getId() >= issueCutOff) {

							if (redmineDAO.ifIssueExistsInProject(issue.getProject().getId(), issue.getId()) == true) {
								/*
								 * here the current issue is already associated
								 * with the current project and an entry exists
								 * in database
								 */
								IssueTemplate updateIssue = new IssueTemplate();
								updateIssue.setProjectId(issue.getProject().getId());
								updateIssue.setIssueId(issue.getId());
								updateIssue.setIssueName(issue.getSubject());
								updateIssue.setIssueStartDate(issue.getStart_date());
								updateIssue.setIssueDueDate(issue.getDue_date());
								updateIssue.setIssueDescription(issue.getDescription());
								/*
								 * Check whether the issue is marked as a
								 * special category issue and retrieve the
								 * category name
								 */
								if (issue.getCategory() != null) {

									updateIssue.setIssueCategory(issue.getCategory().getName());
								}
								updateIssue.setIssuePriority(issue.getPriority().getName());

								if (issue.getAssigned_to() != null) {

									/*
									 * check whether the assignee is already
									 * added to the People Table. If the person
									 * does not exist in the People table, it
									 * means that the person's account is
									 * locked. Thus, this person will be first
									 * added to People table and then the
									 * assignee ID for the issue will be
									 * updated.
									 */
									if (redmineDAO.ifPersonIdExists(issue.getAssigned_to().getId()) != true) {

										User lockedUser = redmineManager.getUserManager()
												.getUserById(issue.getAssigned_to().getId());
										redmineDAO.insertIntoPeopleTable(lockedUser.getId(), lockedUser.getFullName(),
												lockedUser.getFirstName(), lockedUser.getLastName(),
												lockedUser.getMail(), generatedRandomString);
									}

									updateIssue.setPersonId(issue.getAssigned_to().getId());
								}

								updateIssue.setIssueEstimatedTime(issue.getEstimated_hours());
								updateIssue.setIssueDone(issue.getDone_ratio());
								updateIssue.setIssueLastUpdate(issue.getUpdated_on());
								updateIssue.setRedmineLastUpdate(timestamp);

								// if issue already allocated to another
								// sprint..delete it

								List<SprintComprisingIssues> sprintDetails = redmineDAO
										.getSprintAssociatedWithIssue(issue.getId());
								for (SprintComprisingIssues eachIssue : sprintDetails) {
									if (issue.getFixed_version() != null) {
										if (eachIssue.getSprintId().equals(issue.getFixed_version().getId()) == false) {
											redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
										}
									} else {
										redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
									}
								}
								/*
								 * if issue is allocated to another project,
								 * delete it
								 */
								List<IssueUpdateTemplate> projectDetails = redmineDAO
										.getProjectAssocitaedWithIssue(issue.getId());
								for (IssueUpdateTemplate project : projectDetails) {
									if (!project.getProjectId().equals(issue.getProject().getId())) {
										redmineDAO.deleteIssueAlreadyAllocatedToOProject(issue.getId());
									}
								}
								/* update the issue with latest details */
								redmineDAO.updateIssuesModifications(updateIssue);

								if (issue.getFixed_version() != null) {
									se.bth.didd.wiptool.api.Status issueTargetVersion = issue.getFixed_version();
									if (redmineDAO.ifSprintExists(issue.getProject().getId(),
											issueTargetVersion.getId()) == true) {
										if (redmineDAO.ifIssueExistsInSprint(issue.getProject().getId(),
												issueTargetVersion.getId(), issue.getId()) != true) {

											redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProject().getId(),
													issueTargetVersion.getId(), issue.getId());
											// System.out.println("Added the
											// isses
											// to
											// SprintComprisingIssues table");
										}

										if (issue.getAssigned_to() != null) {
											if (redmineDAO.ifPersonParticipatesInSprint(issue.getProject().getId(),
													issueTargetVersion.getId(),
													issue.getAssigned_to().getId()) != true) {

												redmineDAO.InsertIntoSprintParticipationTable(
														issue.getProject().getId(), issueTargetVersion.getId(),
														issue.getAssigned_to().getId());
												// System.out.println("Updated
												// the
												// sprint participation table");
											}
										}
									}
								}

								else {
									/*
									 * here, although issue is not modified,
									 * check whether a new sprint is created for
									 * the project and if the current issue is
									 * associated with the sprint, add a new
									 * entry to the sprintComprisingIssues table
									 */
									if (issue != null && issue.getFixed_version() != null) {

										se.bth.didd.wiptool.api.Status issueTargetVersion = issue.getFixed_version();
										if (redmineDAO.ifSprintExists(issue.getProject().getId(),
												issueTargetVersion.getId()) == true) {
											if (redmineDAO.ifIssueExistsInSprint(issue.getProject().getId(),
													issueTargetVersion.getId(), issue.getId()) != true) {

												redmineDAO.InsertIntoSprintComprisingIssuesTable(
														issue.getProject().getId(), issueTargetVersion.getId(),
														issue.getId());
												// System.out.println("Added the
												// isses
												// to SprintComprisingIssues
												// table");
											}
										}
									}
								}

								if (issue.getFixed_version() != null) {
									redmineDAO.updateIdentifiersInSprintComprisingIssues(issue.getProject().getId(),
											issue.getId(), issue.getFixed_version().getId(), generatedRandomString,
											generatedRandomString, generatedRandomString);
								}
								if (issue.getAssigned_to() != null && issue.getFixed_version() != null) {
									redmineDAO.updateIdentifiersInSprintparticipation(issue.getProject().getId(),
											issue.getFixed_version().getId(), issue.getAssigned_to().getId(),
											generatedRandomString, generatedRandomString, generatedRandomString);
								}
							} else {
								/*
								 * here the current issue details haven't been
								 * recorded on issues table and its association
								 * with project (projectId) hasn't been updated
								 * yet
								 */
								IssueTemplate newIssue = new IssueTemplate();
								newIssue.setProjectId(issue.getProject().getId());
								newIssue.setIssueId(issue.getId());
								newIssue.setIssueName(issue.getSubject());
								newIssue.setIssueStartDate(issue.getStart_date());
								newIssue.setIssueDueDate(issue.getDue_date());
								newIssue.setIssueDescription(issue.getDescription());
								/*
								 * Check whether the issue is marked as a
								 * special category issue and retrieve the
								 * category name
								 */
								if (issue.getCategory() != null) {
									newIssue.setIssueCategory(issue.getCategory().getName());
								}
								newIssue.setIssuePriority(issue.getPriority().getName());
								if (issue.getAssigned_to() != null) {
									/*
									 * check whether the assignee is already
									 * added to the People Table. If the person
									 * does not exist in the People table, it
									 * means that the person's account is
									 * locked. Thus, this person will be first
									 * added to People table and then the
									 * assignee ID for the issue will be
									 * updated.
									 */
									if (redmineDAO.ifPersonIdExists(issue.getAssigned_to().getId()) != true) {

										User lockedUser = redmineManager.getUserManager()
												.getUserById(issue.getAssigned_to().getId());
										redmineDAO.insertIntoPeopleTable(lockedUser.getId(), lockedUser.getFullName(),
												lockedUser.getFirstName(), lockedUser.getLastName(),
												lockedUser.getMail(), generatedRandomString);

									}

									newIssue.setPersonId(issue.getAssigned_to().getId());
								}
								newIssue.setIssueEstimatedTime(issue.getEstimated_hours());
								newIssue.setIssueDone(issue.getDone_ratio());
								newIssue.setIssueLastUpdate(timestamp);
								newIssue.setRedmineLastUpdate(issue.getUpdated_on());

								// if issue already allocated to another
								// sprint..delete
								// it and
								// reassign
								List<SprintComprisingIssues> sprintDetails = redmineDAO
										.getSprintAssociatedWithIssue(issue.getId());
								for (SprintComprisingIssues eachIssue : sprintDetails) {
									if (issue.getFixed_version() != null) {
										if (eachIssue.getSprintId().equals(issue.getFixed_version()) == false) {
											redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
											// System.out.println("issue already
											// allocated to other sprint. so
											// deleted");
										}
									} else {
										redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
										// System.out.println("issue already
										// allocated
										// to other sprint. so deleted as target
										// version
										// is null");
									}
								}
								/*
								 * if issue is allocated to another project,
								 * delete it
								 */
								List<IssueUpdateTemplate> projectDetails = redmineDAO
										.getProjectAssocitaedWithIssue(issue.getId());
								for (IssueUpdateTemplate project : projectDetails) {
									if (!project.getProjectId().equals(issue.getProject().getId())) {
										redmineDAO.deleteIssueAlreadyAllocatedToOProject(issue.getId());
										// System.out.println("issue already
										// allocated
										// to other project. so deleted");
									}
								}

								/*
								 * insert the issue details as new entry to
								 * issues table
								 */
								redmineDAO.insertIntoIssuesTable(newIssue);
								// System.out.println("Added new isses to
								// project "
								// +
								// redmineProject.getName());

								if (issue.getFixed_version() != null) {
									se.bth.didd.wiptool.api.Status issueTargetVersion = issue.getFixed_version();
									if (redmineDAO.ifSprintExists(issue.getProject().getId(),
											issueTargetVersion.getId()) == true) {
										if (redmineDAO.ifIssueExistsInSprint(issue.getProject().getId(),
												issueTargetVersion.getId(), issue.getId()) != true) {

											redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProject().getId(),
													issueTargetVersion.getId(), issue.getId());
										}

										if (issue.getAssigned_to() != null) {
											if (redmineDAO.ifPersonParticipatesInSprint(issue.getProject().getId(),
													issueTargetVersion.getId(),
													issue.getAssigned_to().getId()) != true) {

												redmineDAO.InsertIntoSprintParticipationTable(
														issue.getProject().getId(), issueTargetVersion.getId(),
														issue.getAssigned_to().getId());
												// System.out.println("Added to
												// the
												// sprint participation table
												// for
												// project "+
												// redmineProject.getName());

											}
										}
									}
								}

							}
							redmineDAO.updateIdentifiersInIssues(issue.getProject().getId(), issue.getId(),
									generatedRandomString, generatedRandomString);
							if (issue.getFixed_version() != null) {
								redmineDAO.updateIdentifiersInSprintComprisingIssues(issue.getProject().getId(),
										issue.getId(), issue.getFixed_version().getId(), generatedRandomString,
										generatedRandomString, generatedRandomString);
							}

							if (issue.getAssigned_to() != null && issue.getFixed_version() != null) {
								redmineDAO.updateIdentifiersInSprintparticipation(issue.getProject().getId(),
										issue.getFixed_version().getId(), issue.getAssigned_to().getId(),
										generatedRandomString, generatedRandomString, generatedRandomString);
							}
						}
					}
				}
				/*
				 * Setting an identifier to tag all exisiting projects on
				 * redmine
				 */
				redmineDAO.updateRedmineProjectIdentifier(redmineProject.getId(), generatedRandomString);
			}
		}
	}
		/*
		 * After updating the projects and issuses, check whether all the
		 * participants in sprintparticipation table still remain as valid users
		 * on Redmine, If a user is removed from Redmine, the identifier for
		 * that person will not be updated and hence that person will be deleted
		 * from sprint participation table.
		 */

		for (Project redmineProject : projects) {
			if (redmineProject.getId() >= projectCutOff) {
				List<Version> sprints = redmineManager.getProjectManager().getVersions(redmineProject.getId());
				for (Version sprint : sprints) {
					if (sprint.getProjectId() >= projectCutOff) {
						List<RolesOfPeopleSprint> sprintParticipants = redmineDAO
								.getSprintParticipants(redmineProject.getId(), sprint.getId());

						/*
						 * for each existing sprint participant (from
						 * sprintparticipation table), check whether the
						 * redminePersonIdentifier matches the current generated
						 * string. An active person's redminePersonIdentifier is
						 * updated in the above loops while updating projects
						 * and sprints. So, if a person is active, then the
						 * person should have same redminePersonIdentifier as
						 * the current generatedRandomString.
						 */

						for (RolesOfPeopleSprint eachSprintParticipant : sprintParticipants) {
							if (redmineDAO.IdentifierOfPerson(eachSprintParticipant.getPersonId()).get(0)
									.getRedminePersonIdentifier().equals(generatedRandomString)) {
								redmineDAO.updateIdentifiersInSprintparticipation(redmineProject.getId(),
										sprint.getId(), eachSprintParticipant.getPersonId(), generatedRandomString,
										generatedRandomString, generatedRandomString);
							}

						}
					}
				}
			}
		}

		// Deleting roles that no longer exist on Redmine
		redmineDAO.deleteNonExistingRoles(generatedRandomString);
		
		// Deleting all issues that are below cuttoff
		redmineDAO.deleteIssuesInSprintComprisingIssuesTablebelowProjectCutOffId(projectCutOff);
		redmineDAO.deleteIssuesInSprintComprisingIssuesTablebelowIssueCutOffId(issueCutOff);
		redmineDAO.deleteIssuesbelowProjectCutOffId(projectCutOff);
		redmineDAO.deleteIssuesbelowIssueCutOffId(issueCutOff);
		
		/*
		 * * Deleting all projects from local database, that does not exist on
		 * Redmine anymore
		 */
		redmineDAO.deleteNonExistingProjectsFromParticipationTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromSprintQuestionnaireTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromSprintRequirementsTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromAssetsInSprintTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromDomainsInSprintTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromSprintParticipationTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromSprintDevEnvTable(generatedRandomString);
		// redmineDAO.deleteNonExistingProjectsFromSprintComprisingIssuesTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromSharedSprintsTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromSprintsTable(generatedRandomString);
		// redmineDAO.deleteNonExistingProjectsFromissuesTable(generatedRandomString);

		// deleting the projects removed from Redmine
		redmineDAO.deleteNonExistingProjects(generatedRandomString);

		// Deleting all the sprints that were deleted on Redmine
		redmineDAO.deleteNonExistingSprintsFromSprintQuestionnaireTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintRequirementsTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromAssetsInSprintTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromDomainsInSprintTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintParticipationTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintDevEnvTable(generatedRandomString);
		// redmineDAO.deleteNonExistingSprintsFromSprintComprisingIssuesTable(generatedRandomString);
		// redmineDAO.deleteNonExistingIssuesFromSprintComprisingIssuesTable(generatedRandomString);
		// redmineDAO.deleteNonExistingIssuesFromissuesTable(generatedRandomString);
		redmineDAO.deleteNonExistingSharedSprintsFromSharedSprintsTable(generatedRandomString);
		redmineDAO.deleteNonExistingAssociationsFromSharedSprintsTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintsTable(generatedRandomString);

		// Deleting all the people removed from Redmine.

		/*
		 * Deleting the people who haven't been assigned the current(random
		 * string for this iteration) status identifier i.e. the people who have
		 * been removed from Redmine
		 */
		redmineDAO.deleteNonExistingPeopleFromAssessmentOfCapabilitiesTable(generatedRandomString);
		redmineDAO.deleteNonExistingPeopleFromAssessmentOfSkillsTable(generatedRandomString);
		redmineDAO.deleteNonExistingPeopleFromRolesOfPeopleTable(generatedRandomString);
		redmineDAO.deleteNonExistingPeopleFromProjectParticipationTable(generatedRandomString);
		redmineDAO.deleteNonExistingPeopleFromSprintParticipationTable(generatedRandomString);
		redmineDAO.deleteNonExistingPeopleFromProjectsTable();
		redmineDAO.deletePeopleWhoNoLongerExist(generatedRandomString);
		redmineDAO.resetProjectLeaderIdentifier();

		/*
		 * Updating the percentage of done for each sprint. This is updated
		 * based on the estimatedTime for each issue and percentage of
		 * completion of each issue
		 */
		List<Sprint> sprintdetails = redmineDAO.selectSprintIds();
		for (Sprint sprintdetail : sprintdetails) {
			if (redmineDAO.ifIssuesExistInSprint(sprintdetail.getProjectId(), sprintdetail.getSprintId()) == true) {

				List<IssueTemplate> sprintIssues = redmineDAO.selectIssuesInSprint(sprintdetail.getProjectId(),
						sprintdetail.getSprintId());
				ArrayList<Integer> count = new ArrayList<Integer>();
				List<Float> countEstimatedTimeOfIssues = new ArrayList<Float>();
				float sumOfPecentageDoneOfissues = 0;
				float sumOfEstimatedTimeOfIssues = 0;
				float percentageDone = 0;
				int percentageDoneforSprint = 0;
				for (IssueTemplate sprintIssue : sprintIssues) {
					/*
					 * for each issue in SPRINTCOMPRISINGISSUES table, fetch the
					 * details of issue from ISSUES table
					 */
					if (sprintIssue.getIssueId() != null) {
						List<IssueTemplate> issue = redmineDAO.selectSpecificIssueInSprint(sprintdetail.getProjectId(),
								sprintIssue.getIssueId());
						for (IssueTemplate eachIssue : issue) {
							if (eachIssue != null) {
								/*
								 * Push percentage of done value for each issue,
								 * to an Array.class This array is later used to
								 * calculate the percentage of done for sprint
								 */
								count.add(eachIssue.getIssueDone());
							}

							if (eachIssue.getIssueEstimatedTime() != null) {
								/*
								 * Similar to percentage of done, we also push
								 * estimatedtime value for each issue, to an
								 * array
								 */
								countEstimatedTimeOfIssues.add(eachIssue.getIssueEstimatedTime());
							}
						}
					}
				}
				/*
				 * calculating the total sum of estimated number of hours for
				 * all the issues in the current sprint
				 */
				for (Float estimatedtimeForEachIssue : countEstimatedTimeOfIssues) {
					sumOfEstimatedTimeOfIssues += estimatedtimeForEachIssue;
				}

				/*
				 * For a sprint with n issues, Redmine requires estimatedTime
				 * for at least (n-1) issues to calculate the percentage of done
				 * at sprint Level. Redmine utilizes the estimatedTime values
				 * and computes the percentage of done for sprint by summing the
				 * following values for each issue: product of percentage of
				 * done for each issue and relative estimated times for each
				 * issue(relative estimated time: estimated time for an issue/
				 * estimated time for all issues within the sprint)
				 */if (countEstimatedTimeOfIssues.size() >= (sprintIssues.size() - 1)) {

					for (IssueTemplate sprintIssue : sprintIssues) {
						List<IssueTemplate> issue = redmineDAO.selectSpecificIssueInSprint(sprintdetail.getProjectId(),
								sprintIssue.getIssueId());
						for (IssueTemplate eachIssue : issue) {
							if (eachIssue.getIssueEstimatedTime() != null && (eachIssue.getIssueDone() != null)
									&& sumOfEstimatedTimeOfIssues != 0) {
								float tmp = ((eachIssue.getIssueDone() * eachIssue.getIssueEstimatedTime())
										/ sumOfEstimatedTimeOfIssues);

								sumOfPecentageDoneOfissues += tmp;
							}
						}
					}

					if (sumOfPecentageDoneOfissues != 0) {
						/* roundoff the float value and save to database */
						percentageDone = round(sumOfPecentageDoneOfissues, 0);
						percentageDoneforSprint = (int) percentageDone;
					}

					redmineDAO.updateSprintProgress(sprintdetail.getProjectId(), sprintdetail.getSprintId(),
							percentageDoneforSprint);
				} else {
					/*
					 * if estimatedTime for at least (n-1) issues isn't
					 * available, an average of percentage of done over all
					 * issues is computed and displayed as percentage of done
					 * for sprint
					 */for (Integer doneForEachIssue : count) {
						sumOfPecentageDoneOfissues += doneForEachIssue;
					}

					if (sumOfPecentageDoneOfissues != 0 && sprintIssues.size() != 0) {
						percentageDone = (sumOfPecentageDoneOfissues / sprintIssues.size());
						/* roundoff the float value and save to database */
						percentageDone = round(percentageDone, 0);
						percentageDoneforSprint = (int) percentageDone;
					}

					redmineDAO.updateSprintProgress(sprintdetail.getProjectId(), sprintdetail.getSprintId(),
							percentageDoneforSprint);
				}

			}

		}
		
		for (Project redmineProject : projects) {
			if (redmineProject.getId() >= projectCutOff) {
				List<Membership> projectParticipants = redmineManager.getMembershipManager()
						.getMemberships(redmineProject.getId());

				for (Membership projectParticipant : projectParticipants) {

					if (projectParticipant.getUserId() != null) {

						Collection<Role> rolesOfProjectParticipant = projectParticipant.getRoles();
						for (Role role : rolesOfProjectParticipant) {

							/*
							 * check if the project participation of the person
							 * is already added to the database
							 * (ProjectParticipation Table)
							 */
							if (redmineDAO.ifPersonExistsInProject(redmineProject.getId(),
									projectParticipant.getUserId()) != true) {

								if (redmineDAO.ifPersonParticipatesInProject(redmineProject.getId(),
										projectParticipant.getUserId(), role.getId()) != true) {

									redmineDAO.insertIntoProjectParticipation(redmineProject.getId(),
											projectParticipant.getUserId(), role.getId());
								}
							}

							redmineDAO.updateRedminePersonIdentifierInParticipationTable(redmineProject.getId(),
									projectParticipant.getUserId(), role.getId(), generatedRandomString);

							redmineDAO.updateidentifierInRolesOfPeople(projectParticipant.getUserId(), role.getId(),
									generatedRandomString);

						}

					}
				}
			}
		}
		/*
		 * Computing the percentage of done at project Level.ALL This is based
		 * on computing an average of percentage of done over all sprints within
		 * a project.
		 */List<ProjectIdName> listOfProjectsOnDatabase = redmineDAO.getListOfProjects();
		for (ProjectIdName savedProject : listOfProjectsOnDatabase) {
			ArrayList<Integer> count = new ArrayList<Integer>();
			float sumOfPecentageDoneOfSprints = 0;
			float percentageDone = 0;
			int percentageDoneforProject = 0;
			List<SprintNameProgress> sprintsInProject = redmineDAO.selectSprintByprojectId(savedProject.getProjectId());
			for (SprintNameProgress sprintdetail : sprintsInProject) {

				if (redmineDAO.ifSprintExists(savedProject.getProjectId(), sprintdetail.getSprintId()) == true) {

					if (sprintdetail.getSprintProgress() != null) {

						count.add(sprintdetail.getSprintProgress());
					}
				}
			}

			for (Integer doneForEachSprint : count) {
				sumOfPecentageDoneOfSprints += doneForEachSprint;
			}
			if (sumOfPecentageDoneOfSprints != 0 && sprintsInProject.size() != 0) {
				percentageDone = (sumOfPecentageDoneOfSprints / sprintsInProject.size());
				percentageDone = round(percentageDone, 0);
				percentageDoneforProject = (int) percentageDone;
			}

			redmineDAO.updateProjectProgress(savedProject.getProjectId(), percentageDoneforProject);
		}

		SuccessMessage success = new SuccessMessage();
		success.setSuccess("Synchronization success");
		return Response.ok(success).build();
	}

	@GET
	@Path("/getRedmineURL")
	public Response getRedmineURL() {
		try {
			SuccessMessage success = new SuccessMessage();
			success.setSuccess(redmineUrl);
			return Response.ok(success).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}
	
	public static float round(float number, int decimalPlace) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
}
