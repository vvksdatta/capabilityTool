package se.bth.didd.wiptool.db;

import java.util.Date;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import se.bth.didd.wiptool.api.IssueTemplate;
import se.bth.didd.wiptool.api.IssueUpdateTemplate;
import se.bth.didd.wiptool.api.ProjectIdName;
import se.bth.didd.wiptool.api.ProjectLeaderId;
import se.bth.didd.wiptool.api.Projects;
import se.bth.didd.wiptool.api.RedminePersonIdentifier;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeopleSprint;
import se.bth.didd.wiptool.api.SharedSprint;
import se.bth.didd.wiptool.api.Sprint;
import se.bth.didd.wiptool.api.SprintComprisingIssues;
import se.bth.didd.wiptool.api.SprintNameProgress;

public interface RedmineDAO {
	@SqlUpdate("insert into PROJECTS (projectId, parentProjectId, projectName, projectDescription, projectStartDate, projectEndDate, "
			+ "projectLeader, projectEstimatedEffort, projectActualEffort, projectStatus, projectLastUpdate, redmineLastUpdate, projectUpdatedBy, redmineProjectIdentifier, projectLeaderIdentifier)"
			+ " values (:projectId, :parentProjectId, :projectName, :projectDescription, :projectStartDate, :projectEndDate, :projectLeader, "
			+ ":projectEstimatedEffort, :projectActualEffort, :projectStatus, :projectLastUpdate, :redmineLastUpdate, :projectUpdatedBy, 'new', 'new')")
	void insertIntoProjects(@BindBean Projects project);

	@SqlUpdate("insert into PROJECTPARTICIPATION (projectId, personId, roleId,redmineProjectIdentifier, redminePersonIdentifier ) values (:projectId, :personId, :roleId, 'new', 'new')")
	void insertIntoProjectParticipation(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("roleId") int roleId);

	@SqlUpdate("insert into SPRINTS (sprintId, projectId, sprintName, sprintDescription, sprintStartDate, sprintEndDate, "
			+ "sprintEstimatedEffort, sprintActualEffort, sprintProgress, sprintStatus,"
			+ "sprintPhase, sprintLastUpdate, sprintRedmineUpdate, sprintUpdatedBy, expectedTeamKnowledgeDiversity, expectedCoachingPotential, redmineProjectIdentifier, redmineSprintIdentifier)"
			+ " values (:sprintId, :projectId, :sprintName, :sprintDescription, :sprintStartDate, :sprintEndDate, "
			+ ":sprintEstimatedEffort, :sprintActualEffort,  :sprintProgress, :sprintStatus,"
			+ ":sprintPhase, :sprintLastUpdate, :sprintRedmineUpdate, :sprintUpdatedBy, :expectedTeamKnowledgeDiversity, :expectedCoachingPotential, 'new', 'new')")
	void insertIntoSprints(@BindBean Sprint sprint);

	@SqlUpdate("update SPRINTS set  sprintName = :sprintName, sprintDescription = :sprintDescription, sprintEndDate = :sprintEndDate,  sprintStatus = "
			+ ":sprintStatus, sprintLastUpdate = :sprintLastUpdate, sprintRedmineUpdate = :sprintRedmineUpdate where projectId = :projectId and sprintId = :sprintId")
	void updateSprintModifications(@BindBean Sprint sprint);

	@SqlUpdate("insert into ISSUES (issueId, projectId, personId, issueName, issueStartDate, issueDueDate, issueCategory, issuePriority, securityRiskAnalysis, issueDescription, "
			+ "securityLevel, issueEstimatedTime, issueDone, redmineLastUpdate, issueLastUpdate, redmineProjectIdentifier, redmineIssueIdentifier) values (:issueId, :projectId, :personId, :issueName, :issueStartDate,"
			+ " :issueDueDate, :issueCategory, :issuePriority, :securityRiskAnalysis, :issueDescription, :securityLevel, :issueEstimatedTime, :issueDone, :redmineLastUpdate, "
			+ ":issueLastUpdate, 'new', 'new')")
	void insertIntoIssuesTable(@BindBean IssueTemplate issue);

	@SqlUpdate("insert into SPRINTPARTICIPATION (projectId, sprintId, personId,redmineProjectIdentifier,redmineSprintIdentifier, redminePersonIdentifier ) values (:projectId, :sprintId, :personId, 'new', 'new', 'new')")
	void InsertIntoSprintParticipationTable(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("personId") int personId);

	@SqlUpdate("insert into SPRINTCOMPRISINGISSUES (projectId, sprintId, issueId, redmineProjectIdentifier,redmineSprintIdentifier, redmineIssueIdentifier ) values (:projectId, :sprintId, :issueId, 'new','new', 'new')")
	void InsertIntoSprintComprisingIssuesTable(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("issueId") int issueId);

	@SqlUpdate("update ISSUES set  personId = :personId, issueDone = :issueDone, redmineLastUpdate = :redmineLastUpdate, issueLastUpdate = :issueLastUpdate"
			+ " where projectId = :projectId and issueId = :issueId")
	void updateIssuesTable(@BindBean IssueUpdateTemplate issue);

	@SqlUpdate("update SPRINTS set sprintProgress = :sprintProgress where projectId = :projectId and sprintId = :sprintId")
	void updateSprintProgress(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("sprintProgress") int sprintProgress);

	@SqlUpdate("update PROJECTS set projectProgress = :projectProgress where projectId = :projectId")
	void updateProjectProgress(@Bind("projectId") int projectId, @Bind("projectProgress") int projectProgress);

	@SqlUpdate("update PROJECTS set redmineProjectIdentifier = :redmineProjectIdentifier where projectId = :projectId")
	void updateRedmineProjectIdentifier(@Bind("projectId") int projectId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("update PROJECTS set projectLeaderIdentifier = :projectLeaderIdentifier where projectId = :projectId")
	void updateProjectLeaderIdentifier(@Bind("projectId") int projectId,
			@Bind("projectLeaderIdentifier") String projectLeaderIdentifier);

	@SqlUpdate("update PROJECTPARTICIPATION set redmineProjectIdentifier = :redmineProjectIdentifier where projectId = :projectId and personId = :personId and roleId = :roleId")
	void updateRedmineProjectIdentifierInParticipationTable(@Bind("projectId") int projectId,
			@Bind("personId") int personId, @Bind("roleId") int roleId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("update PROJECTPARTICIPATION set redminePersonIdentifier = :redminePersonIdentifier where projectId = :projectId and personId = :personId and roleId = :roleId")
	void updateRedminePersonIdentifierInParticipationTable(@Bind("projectId") int projectId,
			@Bind("personId") int personId, @Bind("roleId") int roleId,
			@Bind("redminePersonIdentifier") String redminePersonIdentifier);

	@SqlUpdate("update ISSUES set redmineProjectIdentifier = :redmineProjectIdentifier, redmineIssueIdentifier = :redmineIssueIdentifier where projectId = :projectId and issueId = :issueId")
	void updateIdentifiersInIssues(@Bind("projectId") int projectId, @Bind("issueId") int issueId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineIssueIdentifier") String redmineIssueIdentifier);

	@SqlUpdate("update SHAREDSPRINTS set parentProjectIdentifier = :parentProjectIdentifier, associatedProjectIdentifier = :associatedProjectIdentifier,redmineSprintIdentifier = :redmineSprintIdentifier "
			+ " where parentProjectId = :parentProjectId and sprintId = :sprintId and associatedProjectId =:associatedProjectId ")
	void updateIdentifiersInSharedSprint(@Bind("parentProjectId") int parentProjectId, @Bind("sprintId") int sprintId,
			@Bind("associatedProjectId") int associatedProjectId,
			@Bind("associatedProjectIdentifier") String associatedProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier,
			@Bind("parentProjectIdentifier") String parentProjectIdentifier);

	@SqlUpdate("update SPRINTCOMPRISINGISSUES set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier, redmineIssueIdentifier = :redmineIssueIdentifier where projectId = :projectId and sprintId = :sprintId and issueId = :issueId")
	void updateIdentifiersInSprintComprisingIssues(@Bind("projectId") int projectId, @Bind("issueId") int issueId,
			@Bind("sprintId") int sprintId, @Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier,
			@Bind("redmineIssueIdentifier") String redmineIssueIdentifier);

	@SqlUpdate("update Sprints set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier where projectId = :projectId and sprintId = :sprintId")
	void updateIdentifiersInSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("update SprintQuestionnaire set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier where projectId = :projectId and sprintId = :sprintId")
	void updateIdentifiersInSprintQuestionnaire(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("update REQUIREMENTSSELECTEDFORSPRINT set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier where projectId = :projectId and sprintId = :sprintId")
	void updateIdentifiersInSprintRequirement(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("update ASSETSINASPRINT set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier where projectId = :projectId and sprintId = :sprintId")
	void updateIdentifiersInSprintAssets(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("update DOMAINSINASPRINT set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier where projectId = :projectId and sprintId = :sprintId")
	void updateIdentifiersInSprintDomains(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("update SPRINTPARTICIPATION set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier, redminePersonIdentifier = :redminePersonIdentifier where projectId = :projectId and sprintId = :sprintId and personId = :personId")
	void updateIdentifiersInSprintparticipation(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("personId") int personId, @Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier,
			@Bind("redminePersonIdentifier") String redminePersonIdentifier);

	@SqlUpdate("update DEVELOPMENT_ENV_IN_A_SPRINT set redmineProjectIdentifier = :redmineProjectIdentifier, redmineSprintIdentifier = :redmineSprintIdentifier where projectId = :projectId and sprintId = :sprintId")
	void updateIdentifiersInSprintDevEnvironments(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier,
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("update RolesOfPeople set status =:status where personId = :personId and roleId = :roleId ")
	void updateidentifierInRolesOfPeople(@Bind("personId") int personId, @Bind("roleId") int roleId,
			@Bind("status") String status);

	@SqlUpdate("update RolesOfPeople set status =:status where personId = :personId ")
	void updateidentifierForAllRolesInRolesOfPeople(@Bind("personId") int personId, @Bind("status") String status);

	@SqlUpdate("update ASSESSMENTOFCAPABILITIES set status =:status where personId = :personId ")
	void updateidentifierInAssessmentOfCapabilities(@Bind("personId") int personId, @Bind("status") String status);

	@SqlUpdate("update ASSESSMENTOFSKILLS set status =:status where personId = :personId ")
	void updateidentifierInAssessmentOfSkills(@Bind("personId") int personId, @Bind("status") String status);

	@SqlUpdate("insert into ROLESDB (roleId, roleName) values (:roleId, :roleName)")
	void insertIntoRolesDB(@BindBean Roles roles);

	@SqlUpdate("insert into SHAREDSPRINTS (sprintId, parentProjectId, associatedProjectId) values(:sprintId, :parentProjectId, :associatedProjectId )")
	void insertSharedSprints(@BindBean SharedSprint sharedSprint);

	@SqlUpdate("update ROLESDB set roleName = :roleName where roleId = :roleId")
	void updateRolesDB(@BindBean Roles roles);

	@SqlUpdate("update ROLESDB set roleStatus = :roleStatus where roleId = :roleId")
	void updateRoleStatus(@Bind("roleId") int roleId, @Bind("roleStatus") String roleStatus);

	@SqlUpdate("insert into RolesOfPeople (personId ,roleId, status) values (:personId, :roleId, 'new' )")
	void insertIntoRolesOfPeopleTable(@Bind("personId") int personId, @Bind("roleId") int roleId);

	@SqlUpdate("insert into PEOPLE (personId, personName, firstName, lastName, emailId, redminePersonIdentifier) values (:personId, :personName, :firstName, :lastName, :emailId, :redminePersonIdentifier)")
	void insertIntoPeopleTable(@Bind("personId") int personId, @Bind("personName") String personName,
			@Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("emailId") String emailId,
			@Bind("redminePersonIdentifier") String redminePersonIdentifier);

	@SqlUpdate("update PEOPLE set personName =:personName, firstName =:firstName, lastName =:lastName, emailId =:emailId where personId=:personId")
	void updatePeopleTable(@Bind("personId") int personId, @Bind("personName") String personName,
			@Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("emailId") String emailId);

	// separate query as this identifier is updated each time the Redmine
	// synchronization button is clicked
	@SqlUpdate("update PEOPLE set redminePersonIdentifier = :redminePersonIdentifier where personId=:personId")
	void updatePersonIdentifier(@Bind("personId") int personId,
			@Bind("redminePersonIdentifier") String redminePersonIdentifier);

	@SqlUpdate("update PROJECTS set parentProjectId = :parentProjectId, projectName = :projectName, projectDescription = :projectDescription, redmineLastUpdate = :redmineLastUpdate,"
			+ " projectLastUpdate = :projectLastUpdate  where projectId = :projectId")
	void updateProject(@Bind("projectId") int projectId, @Bind("parentProjectId") int parentProjectId,
			@Bind("projectName") String projectName, @Bind("projectDescription") String projectDescription,
			@Bind("redmineLastUpdate") Date redmineLastUpdate, @Bind("projectLastUpdate") Date projectLastUpdate);

	@SqlUpdate("update PROJECTS set  projectName = :projectName, projectDescription = :projectDescription, redmineLastUpdate = :redmineLastUpdate,"
			+ " projectLastUpdate = :projectLastUpdate  where projectId = :projectId")
	void updateProjectWithoutParentProject(@Bind("projectId") int projectId, @Bind("projectName") String projectName,
			@Bind("projectDescription") String projectDescription, @Bind("redmineLastUpdate") Date redmineLastUpdate,
			@Bind("projectLastUpdate") Date projectLastUpdate);

	@SqlUpdate("update ISSUES set personId = :personId, issueName = :issueName, issueStartDate = :issueStartDate, issueDueDate = :issueDueDate, issueCategory = :issueCategory, "
			+ "issuePriority = :issuePriority, issueDescription = :issueDescription, issueEstimatedTime = :issueEstimatedTime, "
			+ " issueDone = :issueDone, redmineLastUpdate = :redmineLastUpdate, issueLastUpdate = :issueLastUpdate"
			+ " where projectId = :projectId and issueId = :issueId")
	void updateIssuesModifications(@BindBean IssueTemplate issue);

	@SqlUpdate("update PROJECTS set projectLeader = null where projectLeaderIdentifier='delete'")
	void deleteNonExistingPeopleFromProjectsTable();

	@SqlUpdate("update PROJECTS set projectLeaderIdentifier = null where projectLeaderIdentifier='delete'")
	void resetProjectLeaderIdentifier();

	@SqlQuery("Select * from SPRINTCOMPRISINGISSUES where issueId = :issueId")
	List<SprintComprisingIssues> getSprintAssociatedWithIssue(@Bind("issueId") int issueId);

	@SqlQuery("select apiKey from LOGINCREDENTIALS where userId = :userId")
	List<String> getApiKeyOfUser(@Bind("userId") int userId);

	@SqlQuery("select exists (select 1 from SPRINTS where projectId= :projectId and sprintId = :sprintId and sprintRedmineUpdate = :sprintRedmineUpdate)")
	boolean ifSprintDetailsUnModified(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("sprintRedmineUpdate") Date sprintRedmineUpdate);

	@SqlQuery("select exists (select 1 from PEOPLE where personId=:personId and  personName =:personName and firstName =:firstName and lastName =:lastName and emailId =:emailId)")
	boolean ifPersonDetailsModified(@Bind("personId") int personId, @Bind("personName") String personName,
			@Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("emailId") String emailId);

	@SqlQuery("select exists (select 1 from PROJECTS where projectId= :projectId and redmineLastUpdate = :redmineLastUpdate)")
	boolean ifProjectDetailsUnModified(@Bind("projectId") int projectId,
			@Bind("redmineLastUpdate") Date redmineLastUpdate);

	@SqlQuery("select exists (select 1 from PROJECTS where projectId= :projectId)")
	boolean ifProjectExists(@Bind("projectId") int projectId);

	@SqlQuery("select exists (select projectLeader from PROJECTS where projectId= :projectId and projectLeader is not null)")
	boolean ifProjectLeaderExists(@Bind("projectId") int projectId);

	@SqlQuery("select exists( select 1 from PROJECTPARTICIPATION where projectId = :projectId and personId = :personId and roleId= :roleId)")
	boolean ifPersonParticipatesInProject(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("roleId") int roleId);

	@SqlQuery("select exists( select 1 from PROJECTPARTICIPATION where projectId = :projectId and personId = :personId)")
	boolean ifPersonExistsInProject(@Bind("projectId") int projectId, @Bind("personId") int personId);

	@SqlQuery("select exists (select 1 from PEOPLE where personId = :personId and personName = :personName)")
	boolean ifPersonExists(@Bind("personId") int personId, @Bind("personName") String personName);

	@SqlQuery("select exists( select 1 from sharedsprints where parentProjectId = :parentProjectId and sprintId = :sprintId and associatedprojectid = :associatedprojectid)")
	boolean IfsprintAssociationExists(@Bind("parentProjectId") int parentProjectId, @Bind("sprintId") int sprintId,
			@Bind("associatedprojectid") int associatedprojectid);

	@SqlQuery("select exists (select 1 from PEOPLE where personId = :personId)")
	boolean ifPersonIdExists(@Bind("personId") int personId);

	@SqlQuery("select exists( select 1 from SPRINTS where projectId= :projectId and sprintId = :sprintId)")
	boolean ifSprintExists(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select exists(select 1 from SPRINTCOMPRISINGISSUES where projectId= :projectId and sprintId = :sprintId and issueId = :issueId )")
	boolean ifIssueExistsInSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("issueId") int issueId);

	@SqlQuery("select exists(select 1 from SPRINTCOMPRISINGISSUES where projectId= :projectId and  issueId = :issueId )")
	boolean ifIssueAlreadyAllocatedToOtherSprint(@Bind("projectId") int projectId, @Bind("issueId") int issueId);

	@SqlQuery("select exists( select 1 from ROLESDB where roleId= :roleId)")
	boolean ifRoleIdExists(@Bind("roleId") int roleId);

	@SqlQuery("select exists( select 1 from ROLESDB where roleId= :roleId and roleName = :roleName)")
	boolean ifRoleExists(@Bind("roleId") int roleId, @Bind("roleName") String roleName);

	@SqlQuery("select exists( select 1 from RolesOfPeople where roleId= :roleId and personId = :personId)")
	boolean ifRoleOfPersonExists(@Bind("roleId") int roleId, @Bind("personId") int personId);

	@SqlQuery("select exists( select 1 from ISSUES where projectId = :projectId and issueId = :issueId)")
	boolean ifIssueExistsInProject(@Bind("projectId") int projectId, @Bind("issueId") int issueId);

	@SqlQuery("select exists( select 1 from ISSUES where projectId = :projectId and issueId = :issueId and redmineLastUpdate = :redmineLastUpdate)")
	boolean ifIssueUnModified(@Bind("projectId") int projectId, @Bind("issueId") int issueId,
			@Bind("redmineLastUpdate") Date redmineLastUpdate);

	@SqlQuery("select exists( select 1 from SPRINTPARTICIPATION where projectId = :projectId and sprintId = :sprintId and personId = :personId)")
	boolean ifPersonParticipatesInSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("personId") int personId);

	@SqlQuery("select redminePersonIdentifier from PEOPLE where personId = :personId")
	List<RedminePersonIdentifier> IdentifierOfPerson(@Bind("personId") int personId);

	@SqlQuery("select * from SPRINTCOMPRISINGISSUES where projectId = :projectId and sprintId = :sprintId")
	List<IssueTemplate> selectIssuesInSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select exists (select 1 from SPRINTCOMPRISINGISSUES where projectId = :projectId and sprintId = :sprintId)")
	boolean ifIssuesExistInSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select projectId, projectName from PROJECTS")
	List<ProjectIdName> getListOfProjects();

	@SqlQuery("select projectLeader from PROJECTS where projectId = :projectId")
	List<ProjectLeaderId> getProjectLeader(@Bind("projectId") int projectId);

	@SqlQuery("select sprintId, sprintProgress from SPRINTS where projectId = :projectId")
	List<SprintNameProgress> selectSprintByprojectId(@Bind("projectId") int projectId);

	@SqlQuery("select sprintId, projectId from SPRINTS")
	List<Sprint> selectSprintIds();

	@SqlQuery("select TableC.personId, PEOPLE.personName, TableC.roleName from PEOPLE RIGHT JOIN (select  TableB.projectId, TableB.sprintId, TableB.personId,ROLESDB.roleName from "
			+ "ROLESDB RIGHT JOIN(select PROJECTPARTICIPATION.roleId, TableA.projectId, TableA.sprintId, TableA.personId from PROJECTPARTICIPATION RIGHT JOIN (select * from SPRINTPARTICIPATION"
			+ " where sprintId= :sprintId and projectId = :projectId)AS TableA ON TableA.projectId = PROJECTPARTICIPATION.projectId and TableA.personId = PROJECTPARTICIPATION.personId) AS TableB ON "
			+ "TableB.roleId = ROLESDB.roleId) AS TableC ON TableC.personId = PEOPLE.personId ")
	List<RolesOfPeopleSprint> getSprintParticipants(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("Select * from ISSUES where issueId = :issueId")
	List<IssueUpdateTemplate> getProjectAssocitaedWithIssue(@Bind("issueId") int issueId);

	@SqlQuery("select * from ISSUES where projectId = :projectId and issueId = :issueId")
	List<IssueTemplate> selectSpecificIssueInSprint(@Bind("projectId") int projectId, @Bind("issueId") int issueId);

	@SqlQuery("SELECT * from ISSUES ORDER BY redmineLastUpdate DESC LIMIT 1")
	List<IssueTemplate> selectLastUpdatedTime();

	@SqlUpdate("Delete from SPRINTCOMPRISINGISSUES where  issueId = :issueId")
	void deleteIssueAlreadyAllocatedToOtherSprint(@Bind("issueId") int issueId);

	@SqlUpdate("Delete from ISSUES where issueId = :issueId")
	void deleteIssueAlreadyAllocatedToOProject(@Bind("issueId") int issueId);

	@SqlUpdate("DELETE FROM PEOPLE WHERE redminePersonIdentifier NOT IN (:currentStatusIdentifier)")
	void deletePeopleWhoNoLongerExist(@Bind("currentStatusIdentifier") String currentStatusIdentifier);

	@SqlUpdate("DELETE FROM ROLESDB WHERE roleStatus NOT IN (:currentStatusIdentifier)")
	void deleteNonExistingRoles(@Bind("currentStatusIdentifier") String currentStatusIdentifier);

	@SqlUpdate("DELETE FROM PROJECTS WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjects(@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM PROJECTPARTICIPATION  WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromParticipationTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM Sprints  WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromSprintsTable(@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM ASSETSINASPRINT  WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromAssetsInSprintTable(
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM SHAREDSPRINTS  WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSharedSprintsFromSharedSprintsTable(
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM SHAREDSPRINTS  WHERE associatedProjectIdentifier NOT IN (:associatedProjectIdentifier)")
	void deleteNonExistingAssociationsFromSharedSprintsTable(
			@Bind("associatedProjectIdentifier") String associatedProjectIdentifier);

	@SqlUpdate("DELETE FROM SHAREDSPRINTS  WHERE parentProjectIdentifier NOT IN (:parentProjectIdentifier)")
	void deleteNonExistingProjectsFromSharedSprintsTable(
			@Bind("parentProjectIdentifier") String parentProjectIdentifier);

	@SqlUpdate("DELETE FROM REQUIREMENTSSELECTEDFORSPRINT  WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromSprintRequirementsTable(
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM SprintQuestionnaire  WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromSprintQuestionnaireTable(
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM SPRINTPARTICIPATION  WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromSprintParticipationTable(
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM DEVELOPMENT_ENV_IN_A_SPRINT  WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromSprintDevEnvTable(@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM ISSUES WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromissuesTable(@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM SPRINTCOMPRISINGISSUES WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromSprintComprisingIssuesTable(
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM SPRINTCOMPRISINGISSUES WHERE projectId < :projectCutOff")
	void deleteIssuesInSprintComprisingIssuesTablebelowProjectCutOffId(@Bind("projectCutOff") int projectCutOff);

	@SqlUpdate("DELETE FROM SPRINTCOMPRISINGISSUES WHERE issueId < :issueIdCutOff")
	void deleteIssuesInSprintComprisingIssuesTablebelowIssueCutOffId(@Bind("issueIdCutOff") int issueIdCutOff);

	@SqlUpdate("DELETE FROM ISSUES WHERE projectId < :projectCutOff")
	void deleteIssuesbelowProjectCutOffId(@Bind("projectCutOff") int projectCutOff);

	@SqlUpdate("DELETE FROM ISSUES WHERE issueId < :issueIdCutOff")
	void deleteIssuesbelowIssueCutOffId(@Bind("issueIdCutOff") int issueIdCutOff);

	@SqlUpdate("DELETE FROM DOMAINSINASPRINT WHERE redmineSprintIdentifier NOT IN (:redmineSprintIdentifier)")
	void deleteNonExistingSprintsFromDomainsInSprintTable(
			@Bind("redmineSprintIdentifier") String redmineSprintIdentifier);

	@SqlUpdate("DELETE FROM Sprints  WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromSprintsTable(@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM ASSETSINASPRINT  WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromAssetsInSprintTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM REQUIREMENTSSELECTEDFORSPRINT  WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromSprintRequirementsTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM SprintQuestionnaire  WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromSprintQuestionnaireTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM SPRINTPARTICIPATION  WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromSprintParticipationTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM SPRINTPARTICIPATION  WHERE redminePersonIdentifier NOT IN (:redminePersonIdentifier)")
	void deleteNonExistingPeopleFromSprintParticipationTable(
			@Bind("redminePersonIdentifier") String redminePersonIdentifier);

	@SqlUpdate("DELETE FROM DEVELOPMENT_ENV_IN_A_SPRINT  WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromSprintDevEnvTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM SPRINTCOMPRISINGISSUES WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromSprintComprisingIssuesTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM DOMAINSINASPRINT WHERE redmineProjectIdentifier NOT IN (:redmineProjectIdentifier)")
	void deleteNonExistingProjectsFromDomainsInSprintTable(
			@Bind("redmineProjectIdentifier") String redmineProjectIdentifier);

	@SqlUpdate("DELETE FROM ISSUES WHERE redmineIssueIdentifier NOT IN (:redmineIssueIdentifier)")
	void deleteNonExistingIssuesFromissuesTable(@Bind("redmineIssueIdentifier") String redmineIssueIdentifier);

	@SqlUpdate("DELETE FROM SPRINTCOMPRISINGISSUES WHERE redmineIssueIdentifier NOT IN (:redmineIssueIdentifier)")
	void deleteNonExistingIssuesFromSprintComprisingIssuesTable(
			@Bind("redmineIssueIdentifier") String redmineIssueIdentifier);

	@SqlUpdate("DELETE FROM PROJECTPARTICIPATION  WHERE redminePersonIdentifier NOT IN (:redminePersonIdentifier)")
	void deleteNonExistingPeopleFromProjectParticipationTable(
			@Bind("redminePersonIdentifier") String redminePersonIdentifier);

	@SqlUpdate("DELETE FROM RolesOfPeople WHERE status NOT IN (:status)")
	void deleteNonExistingPeopleFromRolesOfPeopleTable(@Bind("status") String status);

	@SqlUpdate("DELETE FROM ASSESSMENTOFCAPABILITIES WHERE status NOT IN (:status)")
	void deleteNonExistingPeopleFromAssessmentOfCapabilitiesTable(@Bind("status") String status);

	@SqlUpdate("DELETE FROM ASSESSMENTOFSKILLS WHERE status NOT IN (:status)")
	void deleteNonExistingPeopleFromAssessmentOfSkillsTable(@Bind("status") String status);

}
