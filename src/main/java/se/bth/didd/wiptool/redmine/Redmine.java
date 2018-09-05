package se.bth.didd.wiptool.redmine;

import java.math.BigDecimal;
import java.nio.charset.Charset;
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
import se.bth.didd.wiptool.api.Projects;
import se.bth.didd.wiptool.api.Roles;
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
		 * Fetch list of project participants from each project in redmine. If
		 * person is not already added, add as a new person.Else if the details
		 * are modified, update the details of person
		 */
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiAccessKey);

		List<User> people = redmineManager.getUserManager().getUsers();

		byte[] array = new byte[5];
		new Random().nextBytes(array);
		String generatedRandomString = new String(array, Charset.forName("UTF-8"));

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
				redmineDAO.updatePersonStatus(person.getId(), generatedRandomString);
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
				System.out.println("Inserted new roles ");

			} else {
				/* Handling modifications to role names on Redmine */
				if (redmineDAO.ifRoleExists(role.getId(), role.getName()) != true) {
					Roles newRoles = new Roles();
					newRoles.setRoleId(role.getId());
					newRoles.setRoleName(role.getName());
					redmineDAO.updateRolesDB(newRoles);
					System.out.println("Updated roles database");
				}
			}
			/*
			 * Setting an identifier for each role. This is to check and remove
			 * roles that have been deleted from Redmine.
			 */
			redmineDAO.updateRoleStatus(role.getId(), generatedRandomString);
		}

		/* update projects,sprints, issues etc */
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
						System.out.println("Updated new sprint details to database");
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
							System.out.println("Updated RolesofPeople table");
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
								System.out.println("Updated ProjectParticipation table");
							}
						}
						redmineDAO.updateRedmineProjectIdentifierInParticipationTable(redmineProject.getId(),
								projectParticipant.getUserId(), role.getId(), generatedRandomString);

					}

				}

				/*
				 * for each project, we use the projectidentifier field to fetch
				 * all the issues listed in that project
				 */

				/* Redmine taskadpater java api specific parameter */
				Integer include = null;

				List<Issue> issues = redmineManager.getIssueManager().getIssues(redmineProject.getIdentifier(),
						include);
				for (Issue issue : issues) {
					/*
					 * check whether the current issue (check by using issueId
					 * and projectId) is already associated with current project
					 * and exists in issues Table
					 */
					if (redmineDAO.ifIssueExistsInProject(issue.getProjectId(), issue.getId()) != true) {
						/*
						 * the current issue hasn't been previously associated
						 * with current project
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
						newIssue.setIssueEstimatedTime(issue.getEstimatedHours());
						newIssue.setIssueDone(issue.getDoneRatio());
						newIssue.setIssueLastUpdate(timestamp);
						newIssue.setRedmineLastUpdate(issue.getUpdatedOn());
						/*
						 * retrieve a list of issues from SPRINTCOMPRISINGISSUES
						 * table to check whether the current issue(issueId) is
						 * already associated with some other sprint
						 */
						List<SprintComprisingIssues> sprintDetails = redmineDAO
								.getSprintAssociatedWithIssue(issue.getId());
						for (SprintComprisingIssues eachIssue : sprintDetails) {
							if (issue.getTargetVersion() != null) {
								/*
								 * if the issue(issueId) is associated with some
								 * other sprint(in SPRINTCOMPRISINGISSUES table)
								 * other than the current sprint linked to
								 * issue, delete the entry from table
								 */
								if (eachIssue.getSprintId().equals(issue.getTargetVersion()) == false) {
									redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
								}
							} else {
								/*
								 * Here, the current issue is not associated to
								 * any Sprint. But, there exists an entry(entry
								 * with sprintId and issueId) in
								 * SPRINTCOMPRISINGISSUES table, meaning that
								 * the issue was previously associated with
								 * another sprint. So, in this case, delete such
								 * records from database table
								 */
								redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
							}
						}
						/*
						 * retrieve a list of issues from ISSUES table to check
						 * whether the current issue(issueId) is already
						 * associated with some other project
						 */

						List<IssueUpdateTemplate> projectDetails = redmineDAO
								.getProjectAssocitaedWithIssue(issue.getId());
						for (IssueUpdateTemplate project : projectDetails) {
							/*
							 * On Redmine each issue should be associated with a
							 * project. If the issue(issueId) is associated with
							 * some other project(projectId in ISSUES table)
							 * other than the current project linked to issue,
							 * delete the entry from table
							 */
							if (project.getProjectId().equals(issue.getProjectId()) == false) {
								redmineDAO.deleteIssueAlreadyAllocatedToOProject(issue.getId());
							}
						}
						/*
						 * Now add the current issue to the ISSUES Table.This
						 * pushes a new entry to the table together with the
						 * latest project(projectId) the issue is currently
						 * associated with
						 */
						redmineDAO.insertIntoIssuesTable(newIssue);
						System.out.println("Added new issues to the project");
					} else {
						/*
						 * The current issue has already been previously
						 * associated with current project. i.e, a database
						 * entry already exists in ISSUES table with the current
						 * issueId and projectId
						 */

						/*
						 * check whether the issue has been modified since last
						 * update on database table (by comparing the latest
						 * updated time from issue and the existing LastUpdate
						 * time record on ISSUES table in database)
						 */
						if (redmineDAO.ifIssueUnModified(issue.getProjectId(), issue.getId(),
								issue.getUpdatedOn()) == false) {
							/*
							 * Here the issue is modified in comparison to last
							 * saved state on ISSUES table, so update the
							 * database record
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
							updateIssue.setPersonId(issue.getAssigneeId());
							updateIssue.setIssueEstimatedTime(issue.getEstimatedHours());
							updateIssue.setIssueDone(issue.getDoneRatio());
							updateIssue.setIssueLastUpdate(timestamp);
							updateIssue.setRedmineLastUpdate(issue.getUpdatedOn());

							/*
							 * retrieve a list of issues from
							 * SPRINTCOMPRISINGISSUES table to check whether the
							 * current issue(issueId) is already associated with
							 * some other sprint within the same project
							 */

							List<SprintComprisingIssues> sprintDetails = redmineDAO
									.getSprintAssociatedWithIssue(issue.getId());
							for (SprintComprisingIssues eachIssue : sprintDetails) {
								if (issue.getTargetVersion() != null) {
									/*
									 * if the issue(issueId) is associated with
									 * some other sprint(in
									 * SPRINTCOMPRISINGISSUES table) other than
									 * the current sprint linked to issue,
									 * delete the entry from table
									 */
									if (eachIssue.getSprintId().equals(issue.getTargetVersion()) == false) {
										redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
									}
								} else {
									/*
									 * Here, the current issue is not associated
									 * to any Sprint. But, there exists an
									 * entry(entry with sprintId and issueId) in
									 * SPRINTCOMPRISINGISSUES table, meaning
									 * that the issue was previously associated
									 * with another sprint within the same
									 * project. So, in this case, delete such
									 * records from database table
									 */
									redmineDAO.deleteIssueAlreadyAllocatedToOtherSprint(issue.getId());
								}
							}

							/*
							 * Update the existing database entry(in ISSUES
							 * table) with latest modifications
							 */
							redmineDAO.updateIssuesModifications(updateIssue);
						}
					}
					/*
					 * Next, use the TargetVersion of an issue and record to
					 * which sprint the issue belongs. Add a new entry to
					 * SprintComprisingIssues Table
					 */
					if (issue.getTargetVersion() != null) {
						Version issueTargetVersion = issue.getTargetVersion();
						if (redmineDAO.ifIssueExistsInSprint(issue.getProjectId(), issueTargetVersion.getId(),
								issue.getId()) != true) {
							redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProjectId(),
									issueTargetVersion.getId(), issue.getId());
							System.out.println("Adeed the isses to SprintComprisingIssues table");
						}
					}
					/*
					 * condition to check whether the issue is assigned to a
					 * person. If an issue has an 'assignee', then update the
					 * personId in ISSUES table and insert a record in
					 * sprintParticipation table. This is because an issue can
					 * be a part of project even without being assigned to a
					 * person
					 */
					if (issue.getAssigneeId() != null) {
						IssueUpdateTemplate newIssue = new IssueUpdateTemplate();
						newIssue.setProjectId(issue.getProjectId());
						newIssue.setIssueId(issue.getId());
						newIssue.setPersonId(issue.getAssigneeId());
						newIssue.setIssueDone(issue.getDoneRatio());
						newIssue.setRedmineLastUpdate(issue.getUpdatedOn());
						newIssue.setIssueLastUpdate(issue.getUpdatedOn());

						redmineDAO.updateIssuesTable(newIssue);
						System.out.println("Updated the assignee details in Issues table");
						/*
						 * Further, on Redmine, if a person is assigned an
						 * issue, that would implicitly mean that the person is
						 * also part of the sprint. So, this block can be
						 * further used to add a record in the
						 * SprintParticiaption Table
						 */
						if (issue.getTargetVersion() != null) {
							Version issueTargetVersion = issue.getTargetVersion();
							if (redmineDAO.ifPersonParticipatesInSprint(issue.getProjectId(),
									issueTargetVersion.getId(), issue.getAssigneeId()) != true) {

								redmineDAO.InsertIntoSprintParticipationTable(issue.getProjectId(),
										issueTargetVersion.getId(), issue.getAssigneeId());
								System.out.println("Updated the sprint participation table");
							}
						}
					}
				}

			} else {
				/*
				 * The current project is already added as a record on projects
				 * table of local database
				 */
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());

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
						System.out.println("Updated new sprint details to database");
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
						System.out.println("Modified sprint details");

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

							if (redmineDAO.ifIssueExistsInSprint(issue.getProjectId(), issueTargetVersion.getId(),
									issue.getId()) != true) {
								redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProjectId(),
										issueTargetVersion.getId(), issue.getId());
								System.out.println("Added the isses to SprintComprisingIssues table");
							}

							if (issue.getAssigneeId() != null) {
								if (redmineDAO.ifPersonParticipatesInSprint(issue.getProjectId(),
										issueTargetVersion.getId(), issue.getAssigneeId()) != true) {

									redmineDAO.InsertIntoSprintParticipationTable(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getAssigneeId());
									System.out.println("Updated the sprint participation table");
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

								if (redmineDAO.ifIssueExistsInSprint(issue.getProjectId(), issueTargetVersion.getId(),
										issue.getId()) != true) {
									redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getId());
									System.out.println("Added the isses to SprintComprisingIssues table");
								}

							}
						}
						if (issue.getAssigneeId() != null && issue.getTargetVersion() != null) {
							redmineDAO.updateIdentifiersInSprintparticipation(issue.getProjectId(),
									issue.getTargetVersion().getId(), issue.getAssigneeId(), generatedRandomString,
									generatedRandomString);
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

						/*
						 * insert the issue details as new entry to issues table
						 */
						redmineDAO.insertIntoIssuesTable(newIssue);

						if (issue.getTargetVersion() != null) {
							Version issueTargetVersion = issue.getTargetVersion();

							if (redmineDAO.ifIssueExistsInSprint(issue.getProjectId(), issueTargetVersion.getId(),
									issue.getId()) != true) {
								redmineDAO.InsertIntoSprintComprisingIssuesTable(issue.getProjectId(),
										issueTargetVersion.getId(), issue.getId());

							}

							if (issue.getAssigneeId() != null) {
								if (redmineDAO.ifPersonParticipatesInSprint(issue.getProjectId(),
										issueTargetVersion.getId(), issue.getAssigneeId()) != true) {

									redmineDAO.InsertIntoSprintParticipationTable(issue.getProjectId(),
											issueTargetVersion.getId(), issue.getAssigneeId());

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
				}
			}
			/* Setting an identifier to tag all exisiting projects on redmine */
			redmineDAO.updateRedmineProjectIdentifier(redmineProject.getId(), generatedRandomString);

		}

		/*
		 * Deleting the people who haven't been assigned the current(random
		 * string for this iteration) status identifier i.e. the people who have
		 * been removed from Redmine
		 */
		redmineDAO.deletePeopleWhoNoLongerExist(generatedRandomString);

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