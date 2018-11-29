package se.bth.didd.wiptool.redmine;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Role;
import com.taskadapter.redmineapi.bean.User;
import com.taskadapter.redmineapi.bean.Version;
import se.bth.didd.wiptool.api.IssueTemplate;
import se.bth.didd.wiptool.api.IssueUpdateTemplate;
import se.bth.didd.wiptool.api.ProjectIdName;
import se.bth.didd.wiptool.api.ProjectLeaderId;
import se.bth.didd.wiptool.api.Projects;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeopleSprint;
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
	private String apiAccessKey;

	public Redmine(RedmineDAO redmineDAO, String redmineUrl, String apiAccessKey) {
		this.redmineDAO = redmineDAO;
		this.redmineUrl = redmineUrl;
		this.apiAccessKey = apiAccessKey;
	}

	public Redmine() {

	}

	@GET
	public Response synchronizingWithRedmine() throws RedmineException {

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

		/*
		 * Fetch list of project participants from each project in redmine. If
		 * person is not already added, add as a new person.Else if the details
		 * are modified, update the details of person
		 */
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiAccessKey);

		List<User> people = redmineManager.getUserManager().getUsers();

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
			if (redmineDAO.ifRoleIdExists(role.getId()) != true) {

				Roles newRoles = new Roles();
				newRoles.setRoleId(role.getId());
				newRoles.setRoleName(role.getName());
				redmineDAO.insertIntoRolesDB(newRoles);
				//System.out.println("Inserted new roles ");

			} else {
				/* Handling modifications to role names on Redmine */
				if (redmineDAO.ifRoleExists(role.getId(), role.getName()) != true) {
					Roles newRoles = new Roles();
					newRoles.setRoleId(role.getId());
					newRoles.setRoleName(role.getName());
					redmineDAO.updateRolesDB(newRoles);
					//System.out.println("Updated roles database");
				}
			}
			/*
			 * Setting an identifier for each role. This is to check and remove
			 * roles that have been deleted from Redmine.
			 */
			redmineDAO.updateRoleStatus(role.getId(), generatedRandomString);
		}

		/* update projects,sprints etc */
		List<Project> projects = redmineManager.getProjectManager().getProjects();
		for (Project redmineProject : projects) {
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
				 * for each project, fetch the list of sprints associated with
				 * the project
				 */
				List<Version> sprints = redmineManager.getProjectManager().getVersions(redmineProject.getId());
				for (Version sprint : sprints) {
					/*
					 * check if a sprint is already added to the list of
					 * sprints. If false, add as a new sprint
					 */

					if (redmineDAO.ifSprintExists(sprint.getProjectId(), sprint.getId()) != true) {

						/*
						 * A sprint can sometimes be hierarchical i.e the
						 * sprints of sub projects can be shared with parent
						 * project. To handle this, add project details if they
						 * don't exist in projects table
						 */
						if (redmineDAO.ifProjectExists(sprint.getProjectId()) != true) {
							Project project = redmineManager.getProjectManager().getProjectById(sprint.getProjectId());
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
						//System.out.println("Updated new sprint details to database");
					}
				}

				/* Retrieve the list of project participants */
				List<Membership> projectParticipants = redmineManager.getMembershipManager()
						.getMemberships(redmineProject.getId());
				for (Membership projectParticipant : projectParticipants) {
					/*
					 * for each project participant first we retrieve the list
					 * of roles he/she is allocated on each project
					 */
					Collection<Role> rolesOfProjectParticipant = projectParticipant.getRoles();
					for (Role role : rolesOfProjectParticipant) {

						/*
						 * check whether the person is already added to People
						 * table. If the person is not added but exists in the
						 * project participation list, it indicates that the
						 * person account is locked. This person is first added
						 * to list of people and then added to other tables.
						 */
						if (redmineDAO.ifPersonIdExists(projectParticipant.getUserId()) != true) {

							User lockedUser = redmineManager.getUserManager()
									.getUserById(projectParticipant.getUserId());
							redmineDAO.insertIntoPeopleTable(lockedUser.getId(), lockedUser.getFullName(),
									lockedUser.getFirstName(), lockedUser.getLastName(), lockedUser.getMail(),
									generatedRandomString);

						}

						/*
						 * check if the role is already added to the table that
						 * displays the list of roles each person is capable of
						 * taking up (RolesOfPeople Table)
						 */
						if (redmineDAO.ifRoleOfPersonExists(role.getId(), projectParticipant.getUserId()) != true) {

							/*
							 * If there is no registered record in database,
							 * insert a new record to the table with the roleId
							 * along with personId
							 */
							redmineDAO.insertIntoRolesOfPeopleTable(projectParticipant.getUserId(), role.getId());
							//System.out.println("Updated RolesofPeople table");

						}

						/*
						 * check if the project participation of the person is
						 * already added to the database (ProjectParticipation
						 * Table)
						 */

						/*
						 * Since in each project a person can be assigned only a
						 * single role, here we check whether a person is
						 * already added to project participation Table. If
						 * person already added, no new roles will be assigned
						 * to the person in the current project.
						 */

						/*
						 * Note a possible scenario: Consider two projects
						 * 'project1' and 'project2'. Say 'project2' consists of
						 * several issues tagged under special category and say
						 * an issue('issue1') among these is assigned to
						 * 'person1'. Now considering that 'person1' is not a
						 * member of 'project1', if we move the 'issue1' from
						 * 'project2' to 'project1' and assign 'issue1' to a
						 * target version in 'project1', then the 'person1' will
						 * be a member of a sprint without actually being a
						 * member of 'project1'. In such case, even Redmine
						 * doesn't represent the person to be part of the
						 * project. Thus, this person doesn't bear a role within
						 * this project. So, if a sprint shows people with no
						 * role, it indicates that an issue that was allocated
						 * to a person was moved from one project to another.
						 */
						if (redmineDAO.ifPersonExistsInProject(redmineProject.getId(),
								projectParticipant.getUserId()) != true) {
							if (redmineDAO.ifPersonParticipatesInProject(redmineProject.getId(),
									projectParticipant.getUserId(), role.getId()) != true) {
								/*
								 * If there is no registered record in database,
								 * insert a new record to the table with the
								 * projectId along with personId and roleId
								 */
								redmineDAO.insertIntoProjectParticipation(redmineProject.getId(),
										projectParticipant.getUserId(), role.getId());
								//System.out.println("Updated ProjectParticipation table");
							}
						}
						redmineDAO.updateRedmineProjectIdentifierInParticipationTable(redmineProject.getId(),
								projectParticipant.getUserId(), role.getId(), generatedRandomString);

						redmineDAO.updateRedminePersonIdentifierInParticipationTable(redmineProject.getId(),
								projectParticipant.getUserId(), role.getId(), generatedRandomString);

						redmineDAO.updateidentifierInRolesOfPeople(projectParticipant.getUserId(), role.getId(),
								generatedRandomString);
						redmineDAO.updatePersonIdentifier(projectParticipant.getUserId(), generatedRandomString);
						redmineDAO.updateidentifierInAssessmentOfCapabilities(projectParticipant.getUserId(),
								generatedRandomString);
						redmineDAO.updateidentifierInAssessmentOfSkills(projectParticipant.getUserId(),
								generatedRandomString);

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
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		for (Project redmineProject : projects) {
			/* check if project already added to database */
			if (redmineDAO.ifProjectExists(redmineProject.getId()) != false) {

				/*
				 * The current project is already added as a record on projects
				 * table of local database
				 */

				/*
				 * check whether project leader is still a registered user on
				 * Redmine. Remove the project leader details of a project if
				 * the person is removed from redmine.
				 */

				if (redmineDAO.ifProjectLeaderExists(redmineProject.getId()) == true) {
					List<ProjectLeaderId> currentProjectLeader = redmineDAO.getProjectLeader(redmineProject.getId());
					for (ProjectLeaderId leader : currentProjectLeader) {
						/*
						 * If the person registered on local database is not
						 * found on Redmine, update projectLeaderIdentifier for
						 * that project and remove project leader details for
						 * that project. This is done by trying to access the
						 * user details on Redmine and if the details are not
						 * found, then the catch bloc executes
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
				 * Check whether the current project details have been modified
				 * since last recorded entry on database(lastUpdate time stamp
				 * in projects table)
				 */

				if (redmineDAO.ifProjectDetailsUnModified(redmineProject.getId(),
						redmineProject.getUpdatedOn()) == false) {
					if (redmineProject.getParentId() == null) {
						redmineDAO.updateProjectWithoutParentProject(redmineProject.getId(), redmineProject.getName(),
								redmineProject.getDescription(), redmineProject.getUpdatedOn(), timestamp);
					} else {
						redmineDAO.updateProject(redmineProject.getId(), redmineProject.getParentId(),
								redmineProject.getName(), redmineProject.getDescription(),
								redmineProject.getUpdatedOn(), timestamp);
					}

				}

				List<Version> sprints = redmineManager.getProjectManager().getVersions(redmineProject.getId());
				for (Version sprint : sprints) {

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
						//System.out.println("Updated new sprint details to database");
					}
					/*
					 * Check whether the current sprint details on Redmine have
					 * been modified since last recorded entry on
					 * database(lastUpdate time stamp in sprints table)
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
						//System.out.println("Modified sprint details");

					}

					redmineDAO.updateIdentifiersInSprint(sprint.getProjectId(), sprint.getId(), generatedRandomString,
							generatedRandomString);
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

				/* Retrieve the list of project participants */
				List<Membership> projectParticipants = redmineManager.getMembershipManager()
						.getMemberships(redmineProject.getId());

				for (Membership projectParticipant : projectParticipants) {

					/*
					 * for each project participant first we retrieve the list
					 * of roles he/she is allocated on each project
					 */
					Collection<Role> rolesOfProjectParticipant = projectParticipant.getRoles();
					for (Role role : rolesOfProjectParticipant) {

						/*
						 * check whether the person is already added to People
						 * table. If the person is not added but exists in the
						 * project participation list, it indicates that the
						 * person account is locked. This person is first added
						 * to list of people and then added to other tables.
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
							redmineDAO.updateidentifierInAssessmentOfSkills(lockedUser.getId(), generatedRandomString);
						}

						/*
						 * check if the role is already added to the table that
						 * displays the list of roles each person is capable of
						 * taking up (RolesOfPeople Table)
						 */
						if (redmineDAO.ifRoleOfPersonExists(role.getId(), projectParticipant.getUserId()) != true) {

							/*
							 * If there is no registered record in database,
							 * insert a new record to the table with the roleId
							 * along with personId
							 */
							redmineDAO.insertIntoRolesOfPeopleTable(projectParticipant.getUserId(), role.getId());

						}

						/*
						 * check if the project participation of the person is
						 * already added to the database (ProjectParticipation
						 * Table)
						 */
						if (redmineDAO.ifPersonExistsInProject(redmineProject.getId(),
								projectParticipant.getUserId()) != true) {

							if (redmineDAO.ifPersonParticipatesInProject(redmineProject.getId(),
									projectParticipant.getUserId(), role.getId()) != true) {
								/*
								 * If there is no registered record in database,
								 * insert a new record to the table with the
								 * projectId along with personId and roleId
								 */
								redmineDAO.insertIntoProjectParticipation(redmineProject.getId(),
										projectParticipant.getUserId(), role.getId());
							}
						}
						redmineDAO.updateRedmineProjectIdentifierInParticipationTable(redmineProject.getId(),
								projectParticipant.getUserId(), role.getId(), generatedRandomString);

						redmineDAO.updateRedminePersonIdentifierInParticipationTable(redmineProject.getId(),
								projectParticipant.getUserId(), role.getId(), generatedRandomString);

						redmineDAO.updateidentifierInRolesOfPeople(projectParticipant.getUserId(), role.getId(),
								generatedRandomString);

						redmineDAO.updatePersonIdentifier(projectParticipant.getUserId(), generatedRandomString);
						redmineDAO.updateidentifierInAssessmentOfCapabilities(projectParticipant.getUserId(),
								generatedRandomString);
						redmineDAO.updateidentifierInAssessmentOfSkills(projectParticipant.getUserId(),
								generatedRandomString);

					}

				}

				Integer include = null;

				List<Issue> issues = redmineManager.getIssueManager().getIssues(redmineProject.getIdentifier(),
						include);
				for (Issue issue : issues) {

					if (redmineDAO.ifIssueExistsInProject(issue.getProjectId(), issue.getId()) == true) {
						/*
						 * here the current issue is already associated with the
						 * current project and an entry exists in database
						 */

						IssueTemplate updateIssue = new IssueTemplate();
						updateIssue.setProjectId(issue.getProjectId());
						updateIssue.setIssueId(issue.getId());
						updateIssue.setIssueName(issue.getSubject());
						updateIssue.setIssueStartDate(issue.getStartDate());
						updateIssue.setIssueDueDate(issue.getDueDate());
						updateIssue.setIssueDescription(issue.getDescription());
						/*
						 * Check whether the issue is marked as a special
						 * category issue and retrieve the category name
						 */
						if (issue.getCategory() != null) {
							IssueCategory retrievedCategory = issue.getCategory();
							updateIssue.setIssueCategory(retrievedCategory.getName());
						}
						updateIssue.setIssuePriority(issue.getPriorityText());

						if (issue.getAssigneeId() != null) {

							/*
							 * check whether the assignee is already added to
							 * the People Table. If the person does not exist in
							 * the People table, it means that the person's
							 * account is locked. Thus, this person will be
							 * first added to People table and then the assignee
							 * ID for the issue will be updated.
							 */
							if (redmineDAO.ifPersonIdExists(issue.getAssigneeId()) != true) {

								User lockedUser = redmineManager.getUserManager().getUserById(issue.getAssigneeId());
								redmineDAO.insertIntoPeopleTable(lockedUser.getId(), lockedUser.getFullName(),
										lockedUser.getFirstName(), lockedUser.getLastName(), lockedUser.getMail(),
										generatedRandomString);
							}

							updateIssue.setPersonId(issue.getAssigneeId());
						}

						updateIssue.setIssueEstimatedTime(issue.getEstimatedHours());
						updateIssue.setIssueDone(issue.getDoneRatio());
						updateIssue.setIssueLastUpdate(timestamp);
						updateIssue.setRedmineLastUpdate(issue.getUpdatedOn());

						// if issue already allocated to another
						// sprint..delete it

						List<SprintComprisingIssues> sprintDetails = redmineDAO
								.getSprintAssociatedWithIssue(issue.getId());
						for (SprintComprisingIssues eachIssue : sprintDetails) {
							if (issue.getTargetVersion() != null) {
								if (eachIssue.getSprintId().equals(issue.getTargetVersion()) == false) {
									redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
								}
							} else {
								redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
							}
						}
						/*
						 * if issue is allocated to another project, delete it
						 */
						List<IssueUpdateTemplate> projectDetails = redmineDAO
								.getProjectAssocitaedWithIssue(issue.getId());
						for (IssueUpdateTemplate project : projectDetails) {
							if (!project.getProjectId().equals(issue.getProjectId())) {
								redmineDAO.deleteIssueAlreadyAllocatedToOProject(issue.getId());
							}
						}
						/* update the issue with latest details */
						redmineDAO.updateIssuesModifications(updateIssue);

						if (issue.getTargetVersion() != null) {
							Version issueTargetVersion = issue.getTargetVersion();
							if (redmineDAO.ifSprintExists(issue.getProjectId(), issueTargetVersion.getId()) == true) {
								if (redmineDAO.ifIssueExistsInSprint(issue.getProjectId(), issueTargetVersion.getId(),
										issue.getId()) != true) {

									redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getId());
									//System.out.println("Added the isses to SprintComprisingIssues table");
								}

								if (issue.getAssigneeId() != null) {
									if (redmineDAO.ifPersonParticipatesInSprint(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getAssigneeId()) != true) {

										redmineDAO.InsertIntoSprintParticipationTable(issue.getProjectId(),
												issueTargetVersion.getId(), issue.getAssigneeId());
										//System.out.println("Updated the sprint participation table");
									}
								}
							}
						}

						else {
							/*
							 * here, although issue is not modified, check
							 * whether a new sprint is created for the project
							 * and if the current issue is associated with the
							 * sprint, add a new entry to the
							 * sprintComprisingIssues table
							 */
							if (issue != null && issue.getTargetVersion() != null) {

								Version issueTargetVersion = issue.getTargetVersion();
								if (redmineDAO.ifSprintExists(issue.getProjectId(),
										issueTargetVersion.getId()) == true) {
									if (redmineDAO.ifIssueExistsInSprint(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getId()) != true) {

										redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProjectId(),
												issueTargetVersion.getId(), issue.getId());
										//System.out.println("Added the isses to SprintComprisingIssues table");
									}
								}
							}
						}

						if (issue.getTargetVersion() != null) {
							redmineDAO.updateIdentifiersInSprintComprisingIssues(issue.getProjectId(), issue.getId(),
									issue.getTargetVersion().getId(), generatedRandomString, generatedRandomString,
									generatedRandomString);
						}
						if (issue.getAssigneeId() != null && issue.getTargetVersion() != null) {
							redmineDAO.updateIdentifiersInSprintparticipation(issue.getProjectId(),
									issue.getTargetVersion().getId(), issue.getAssigneeId(), generatedRandomString,
									generatedRandomString, generatedRandomString);
						}
					} else {
						/*
						 * here the current issue details haven't been recorded
						 * on issues table and its association with project
						 * (projectId) hasn't been updated yet
						 */
						IssueTemplate newIssue = new IssueTemplate();
						newIssue.setProjectId(issue.getProjectId());
						newIssue.setIssueId(issue.getId());
						newIssue.setIssueName(issue.getSubject());
						newIssue.setIssueStartDate(issue.getStartDate());
						newIssue.setIssueDueDate(issue.getDueDate());
						newIssue.setIssueDescription(issue.getDescription());
						/*
						 * Check whether the issue is marked as a special
						 * category issue and retrieve the category name
						 */
						if (issue.getCategory() != null) {
							IssueCategory retrievedCategory = issue.getCategory();
							newIssue.setIssueCategory(retrievedCategory.getName());
						}
						newIssue.setIssuePriority(issue.getPriorityText());
						if (issue.getAssigneeId() != null) {
							/*
							 * check whether the assignee is already added to
							 * the People Table. If the person does not exist in
							 * the People table, it means that the person's
							 * account is locked. Thus, this person will be
							 * first added to People table and then the assignee
							 * ID for the issue will be updated.
							 */
							if (redmineDAO.ifPersonIdExists(issue.getAssigneeId()) != true) {

								User lockedUser = redmineManager.getUserManager().getUserById(issue.getAssigneeId());
								redmineDAO.insertIntoPeopleTable(lockedUser.getId(), lockedUser.getFullName(),
										lockedUser.getFirstName(), lockedUser.getLastName(), lockedUser.getMail(),
										generatedRandomString);

							}

							newIssue.setPersonId(issue.getAssigneeId());
						}
						newIssue.setIssueEstimatedTime(issue.getEstimatedHours());
						newIssue.setIssueDone(issue.getDoneRatio());
						newIssue.setIssueLastUpdate(timestamp);
						newIssue.setRedmineLastUpdate(issue.getUpdatedOn());

						// if issue already allocated to another sprint..delete
						// it and
						// reassign
						List<SprintComprisingIssues> sprintDetails = redmineDAO
								.getSprintAssociatedWithIssue(issue.getId());
						for (SprintComprisingIssues eachIssue : sprintDetails) {
							if (issue.getTargetVersion() != null) {
								if (eachIssue.getSprintId().equals(issue.getTargetVersion()) == false) {
									redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
									//System.out.println("issue already allocated to other sprint. so deleted");
								}
							} else {
								redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
								//System.out.println("issue already allocated to other sprint. so deleted as target version is null");
							}
						}
						/*
						 * if issue is allocated to another project, delete it
						 */
						List<IssueUpdateTemplate> projectDetails = redmineDAO
								.getProjectAssocitaedWithIssue(issue.getId());
						for (IssueUpdateTemplate project : projectDetails) {
							if (!project.getProjectId().equals(issue.getProjectId())) {
								redmineDAO.deleteIssueAlreadyAllocatedToOProject(issue.getId());
								//System.out.println("issue already allocated to other project. so deleted");
							}
						}

						/*
						 * insert the issue details as new entry to issues table
						 */
						redmineDAO.insertIntoIssuesTable(newIssue);
						//System.out.println("Added new  isses to  project " + redmineProject.getName());

						if (issue.getTargetVersion() != null) {
							Version issueTargetVersion = issue.getTargetVersion();
							if (redmineDAO.ifSprintExists(issue.getProjectId(), issueTargetVersion.getId()) == true) {
								if (redmineDAO.ifIssueExistsInSprint(issue.getProjectId(), issueTargetVersion.getId(),
										issue.getId()) != true) {

									redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getId());
									//System.out.println("Added the isses to SprintComprisingIssues table for project "+ redmineProject.getName());

								}

								if (issue.getAssigneeId() != null) {
									if (redmineDAO.ifPersonParticipatesInSprint(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getAssigneeId()) != true) {

										redmineDAO.InsertIntoSprintParticipationTable(issue.getProjectId(),
												issueTargetVersion.getId(), issue.getAssigneeId());
										//System.out.println("Added to the sprint participation table for project "+ redmineProject.getName());

									}
								}
							}
						}

					}
					redmineDAO.updateIdentifiersInIssues(issue.getProjectId(), issue.getId(), generatedRandomString,
							generatedRandomString);
					if (issue.getTargetVersion() != null) {
						redmineDAO.updateIdentifiersInSprintComprisingIssues(issue.getProjectId(), issue.getId(),
								issue.getTargetVersion().getId(), generatedRandomString, generatedRandomString,
								generatedRandomString);
					}

					if (issue.getAssigneeId() != null && issue.getTargetVersion() != null) {
						redmineDAO.updateIdentifiersInSprintparticipation(issue.getProjectId(),
								issue.getTargetVersion().getId(), issue.getAssigneeId(), generatedRandomString,
								generatedRandomString, generatedRandomString);
					}
				}
			}
			/* Setting an identifier to tag all exisiting projects on redmine */
			redmineDAO.updateRedmineProjectIdentifier(redmineProject.getId(), generatedRandomString);

		}

		/*
		 * After updating the projects and issuses, check whether all the
		 * participants in sprintparticipation table still remain as valid users
		 * on Redmine, If a user is removed from Redmine, the identifier for
		 * that person will not be updated and hence that person will be deleted
		 * from sprint participation table.
		 */

		for (Project redmineProject : projects) {

			List<Version> sprints = redmineManager.getProjectManager().getVersions(redmineProject.getId());
			for (Version sprint : sprints) {
				List<RolesOfPeopleSprint> sprintParticipants = redmineDAO.getSprintParticipants(redmineProject.getId(),
						sprint.getId());

				/*
				 * for each existing sprint participant (from
				 * sprintparticipation table), check whether the
				 * redminePersonIdentifier matches the current generated string.
				 * An active person's redminePersonIdentifier is updated in the
				 * above loops while updating projects and sprints. So, if a
				 * person is active, then the person should have same
				 * redminePersonIdentifier as the current generatedRandomString.
				 */

				for (RolesOfPeopleSprint eachSprintParticipant : sprintParticipants) {
					if (redmineDAO.IdentifierOfPerson(eachSprintParticipant.getPersonId()).get(0)
							.getRedminePersonIdentifier().equals(generatedRandomString)) {
						redmineDAO.updateIdentifiersInSprintparticipation(redmineProject.getId(), sprint.getId(),
								eachSprintParticipant.getPersonId(), generatedRandomString, generatedRandomString,
								generatedRandomString);
					}

				}
			}
		}

		// Deleting roles that no longer exist on Redmine
		redmineDAO.deleteNonExistingRoles(generatedRandomString);

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
		redmineDAO.deleteNonExistingProjectsFromSprintComprisingIssuesTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromSprintsTable(generatedRandomString);
		redmineDAO.deleteNonExistingProjectsFromissuesTable(generatedRandomString);

		// deleting the projects removed from Redmine
		redmineDAO.deleteNonExistingProjects(generatedRandomString);

		// Deleting all the sprints that were deleted on Redmine
		redmineDAO.deleteNonExistingSprintsFromSprintQuestionnaireTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintRequirementsTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromAssetsInSprintTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromDomainsInSprintTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintParticipationTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintDevEnvTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintComprisingIssuesTable(generatedRandomString);
		redmineDAO.deleteNonExistingIssuesFromSprintComprisingIssuesTable(generatedRandomString);
		redmineDAO.deleteNonExistingIssuesFromissuesTable(generatedRandomString);
		redmineDAO.deleteNonExistingSprintsFromSprintsTable(generatedRandomString);

		// Deleting all the people removed from Redmine.

		// redmineDAO.deletePeopleWhoNoLongerExistFromAssessmentOfCapabilities(generatedRandomString);
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

	public static float round(float number, int decimalPlace) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
}