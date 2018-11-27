package se.bth.didd.wiptool.db;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import com.github.rkmk.container.FoldingList;
import se.bth.didd.wiptool.api.CompanyDrivenFactorsNames;
import se.bth.didd.wiptool.api.RolesOfPeopleSprint;
import se.bth.didd.wiptool.api.SelectedSprintRequirement;
import se.bth.didd.wiptool.api.Sprint;
import se.bth.didd.wiptool.api.SprintAsset;
import se.bth.didd.wiptool.api.SprintBriefSummary;
import se.bth.didd.wiptool.api.SprintDevelopmentEnvironment;
import se.bth.didd.wiptool.api.SprintDomain;
import se.bth.didd.wiptool.api.SprintQuestionnaireTemplate;
import se.bth.didd.wiptool.api.SprintRequirement;
import se.bth.didd.wiptool.api.SprintRequirementNameLevel;
import se.bth.didd.wiptool.api.SprintSummary;

public interface SprintDAO {

	@SqlUpdate("create table if not exists SPRINTS (sprintId int, projectid int REFERENCES PROJECTS(projectId), "
			+ "sprintName varchar(30), sprintDescription text, sprintStartDate date, sprintEndDate date, "
			+ "sprintEstimatedEffort int, sprintActualEffort int, "
			+ "sprintProgress int, sprintStatus varchar(20), sprintPhase text, sprintLastUpdate timestamp, "
			+ "sprintRedmineUpdate timestamp with time zone, sprintUpdatedBy varchar(20), expectedTeamKnowledgeDiversity "
			+ "varchar(10), expectedCoachingPotential varchar(20), redmineProjectIdentifier varchar(10), redmineSprintIdentifier varchar(10), PRIMARY KEY (projectid,sprintid))")
	void createSprintTable();

	@SqlUpdate("create table if not exists SprintQuestionnaire (sprintId int, projectId int REFERENCES PROJECTS(projectId), "
			+ "question1 text, question2 text, question3 text, question4 text,question5 text, question6 text, question7 text, question8 text, question1Comment text, question2Comment text, question3Comment text, question4Comment text, question5Comment text, question6Comment text, question7Comment text, question8Comment text, other text, lastUpdate timestamp, updatedBy varchar(20),redmineprojectIdentifier varchar(10), redmineSprintIdentifier varchar(10),"
			+ " PRIMARY KEY (projectId,sprintId))")
	void createSprintQuestionnaireTable();

	@SqlUpdate("create table if not exists SPRINTPARTICIPATION (projectId int , sprintId int, personId int REFERENCES PEOPLE(personId), personExists int, redmineSprintIdentifier varchar(10), redmineProjectIdentifier varchar(10), redminePersonIdentifier varchar(10),"
			+ "FOREIGN KEY (projectId, sprintId) REFERENCES SPRINTS(projectId, sprintId), PRIMARY KEY(projectId,sprintId,personId))")
	void createSprintParticipationTable();

	@SqlUpdate("create table if not exists SPRINTDOMAINSDB (domainId serial primary key , domainName varchar(30))")
	void createSprintDomainsDbTable();

	@SqlUpdate("create table if not exists SPRINTASSETSDB (assetId serial primary key , assetName varchar(30))")
	void createSprintAssetsDbTable();

	@SqlUpdate("create table if not exists SPRINTDEVELOPMENTENVDB (envId serial primary key , envName varchar(30))")
	void createSprintDevelopmentEnvDbTable();

	@SqlUpdate("create table if not exists DOMAINSINASPRINT (projectId int REFERENCES PROJECTS(projectId), sprintId int, domainId int "
			+ " REFERENCES SPRINTDOMAINSDB(domainId), redmineSprintIdentifier varchar(10), redmineProjectIdentifier varchar(10), PRIMARY KEY (projectId,sprintId,domainId))")
	void createDomainsInaSprintTable();

	@SqlUpdate("create table if not exists ASSETSINASPRINT (projectId int REFERENCES PROJECTS(projectId), sprintId int, assetId int"
			+ " REFERENCES SPRINTASSETSDB(assetId), redmineSprintIdentifier varchar(10), redmineProjectIdentifier varchar(10), PRIMARY KEY (projectId,sprintId,assetId))")
	void createAssetsInaSprintTable();

	@SqlUpdate("create table if not exists DEVELOPMENT_ENV_IN_A_SPRINT (projectId int REFERENCES PROJECTS(projectId), sprintId int, envId int"
			+ " REFERENCES SPRINTDEVELOPMENTENVDB(envId), redmineSprintIdentifier varchar(10), redmineProjectIdentifier varchar(10), PRIMARY KEY (projectId,sprintId,envId))")
	void createDevelopmentEnvInaSprintTable();

	@SqlUpdate("create table if not exists SPRINTREQUIREMENTSDB (sprintRequirementId serial primary key, sprintRequirementName varchar(30), sprintRequirementDescription text)")
	void createSprintRequirementsDBTable();

	@SqlUpdate("create table if not exists REQUIREMENTSSELECTEDFORSPRINT (sprintRequirementId int REFERENCES SPRINTREQUIREMENTSDB(sprintRequirementId), projectId int REFERENCES "
			+ "PROJECTS(projectId), sprintId int, requirementLevel varchar(20), outcome text, updatedBy varchar(30), lastUpdate timestamp,  redmineSprintIdentifier varchar(10), redmineProjectIdentifier varchar(10))")
	void createRequirementsSelectedForSprintTable();

	@SqlUpdate("create table if not exists SPRINTCOMPRISINGISSUES (projectId int, sprintId int, issueId int, FOREIGN KEY (projectId, sprintId)"
			+ " REFERENCES SPRINTS(projectId, sprintId),FOREIGN KEY (projectId, issueId) REFERENCES ISSUES(projectId, issueId), redmineSprintIdentifier varchar(10), redmineProjectIdentifier varchar(10), redmineIssueIdentifier varchar(10), PRIMARY "
			+ "KEY(projectId,sprintId,issueId))")
	void createSprintComprisingIssuesTable();

	@SqlUpdate("insert into SPRINTS (sprintId, projectId, sprintName, sprintDescription, sprintStartDate, sprintEndDate, "
			+ "sprintEstimatedEffort, sprintActualEffort, sprintProgress, sprintStatus,"
			+ "sprintPhase, sprintLastUpdate, sprintRedmineUpdate, sprintUpdatedBy, expectedTeamKnowledgeDiversity, expectedCoachingPotential, redmineProjectIdentifier, redmineSprintIdentifier)"
			+ " values (:sprintId, :projectId, :sprintName, :sprintDescription, :sprintStartDate, :sprintEndDate, "
			+ ":sprintEstimatedEffort, :sprintActualEffort, :sprintProgress, :sprintStatus,"
			+ ":sprintPhase, :sprintLastUpdate, :sprintRedmineUpdate, :sprintUpdatedBy, :expectedTeamKnowledgeDiversity, :expectedCoachingPotential, 'new', 'new')")
	void insert(@BindBean Sprint sprint);

	@SqlUpdate("insert into SPRINTDOMAINSDB (domainName) values(:domainName)")
	void insertSprintDomainsDB(@Bind("domainName") String domainName);

	@SqlUpdate("insert into SPRINTASSETSDB (assetName) values(:assetName)")
	void insertSprintAssetsDB(@Bind("assetName") String assetName);

	@SqlUpdate("insert into SPRINTREQUIREMENTSDB (sprintRequirementName) values(:sprintRequirementName)")
	void insertNewSprintRequirement(@Bind("sprintRequirementName") String sprintRequirementName);

	@SqlUpdate("insert into SprintQuestionnaire (projectId, sprintId, question1, question2, question3, question4, question5, question6, question7, question8,"
			+ " question1Comment, question2Comment, question3Comment, question4Comment, question5Comment, question6Comment, question7Comment, question8Comment, other,"
			+ " lastUpdate, updatedBy, redmineProjectIdentifier, redmineSprintIdentifier) values (:projectId, :sprintId, :question1, :question2,"
			+ " :question3, :question4, :question5, :question6, :question7, :question8, :question1Comment, :question2Comment, :question3Comment, :question4Comment, :question5Comment, :question6Comment, :question7Comment, :question8Comment, :other, :lastUpdate, :updatedBy, 'new', 'new')")
	void insertSprintQuestionnaire(@BindBean SprintQuestionnaireTemplate sprintQuestionnaire);

	@SqlUpdate("update SPRINTREQUIREMENTSDB set sprintRequirementDescription = :sprintRequirementDescription where sprintRequirementId = :sprintRequirementId")
	void updateSprintRequirementDescription(@Bind("sprintRequirementDescription") String sprintRequirementDescription,
			@Bind("sprintRequirementId") int sprintRequirementId);

	@SqlUpdate("insert into REQUIREMENTSSELECTEDFORSPRINT (sprintRequirementId, projectId, sprintId, requirementLevel, outcome, updatedBy, lastUpdate,redmineProjectIdentifier, redmineSprintIdentifier ) "
			+ "values(:sprintRequirementId, :projectId, :sprintId, :requirementLevel, :outcome, :updatedBy, :lastUpdate, 'new', 'new')")
	void insertSelectedSprintRequirement(@BindBean SelectedSprintRequirement selectedSprintRequirement);

	@SqlUpdate("update REQUIREMENTSSELECTEDFORSPRINT set requirementLevel = :requirementLevel, outcome = :outcome, updatedBy = :updatedBy, lastUpdate = :lastUpdate where "
			+ "sprintRequirementId = :sprintRequirementId and  projectId = :projectId and sprintId = :sprintId")
	void updateSelectedSprintRequirement(@BindBean SelectedSprintRequirement selectedSprintRequirement);

	@SqlUpdate("insert into SPRINTDEVELOPMENTENVDB (envName) values(:envName)")
	void insertSprintDevelopmentEnvDB(@Bind("envName") String envName);

	@SqlUpdate("insert into ASSETSINASPRINT (projectId,sprintId,assetId,redmineProjectIdentifier, redmineSprintIdentifier) values(:projectId,:sprintId,:assetId, 'new', 'new')")
	void insertAssetsOfSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("assetId") int assetId);

	@SqlUpdate("insert into DOMAINSINASPRINT (projectId,sprintId,domainId,redmineProjectIdentifier, redmineSprintIdentifier) values(:projectId,:sprintId,:domainId,'new','new')")
	void insertDomainsOfSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("domainId") int domainId);

	@SqlQuery("select TableA.domainId, SPRINTDOMAINSDB.domainName from SPRINTDOMAINSDB RIGHT JOIN (select * from DOMAINSINASPRINT where projectId= :projectId "
			+ "and sprintId = :sprintId) AS TableA ON SPRINTDOMAINSDB.domainId = TableA.domainId")
	List<SprintDomain> getDomainsOfaSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select TableA.assetId, SPRINTASSETSDB.assetName from SPRINTASSETSDB RIGHT JOIN (select * from ASSETSINASPRINT where projectId= :projectId "
			+ "and sprintId = :sprintId) AS TableA ON SPRINTASSETSDB.assetId = TableA.assetId")
	List<SprintAsset> getAssetsOfaSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select TableA.envId, SPRINTDEVELOPMENTENVDB.envName from SPRINTDEVELOPMENTENVDB RIGHT JOIN (select * from DEVELOPMENT_ENV_IN_A_SPRINT where projectId= :projectId "
			+ "and sprintId = :sprintId) AS TableA ON SPRINTDEVELOPMENTENVDB.envId = TableA.envId")
	List<SprintDevelopmentEnvironment> getDevelopmentEnvironmentsOfaSprint(@Bind("projectId") int projectId,
			@Bind("sprintId") int sprintId);

	@SqlQuery("select * from SprintQuestionnaire where projectId = :projectId and sprintId = :sprintId")
	List<SprintQuestionnaireTemplate> getSprintQuestionnaire(@Bind("projectId") int projectId,
			@Bind("sprintId") int sprintId);

	@SqlUpdate("insert into DEVELOPMENT_ENV_IN_A_SPRINT (projectId,sprintId,envId, redmineProjectIdentifier, redmineSprintIdentifier) values(:projectId,:sprintId,:envId, 'new', 'new')")
	void insertDevelopmentEnvInaSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("envId") int envId);

	@SqlUpdate("Delete from  ASSETSINASPRINT where projectId = :projectId and sprintId = :sprintId")
	void DeleteAssetsOfSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("Delete from  DOMAINSINASPRINT where projectId = :projectId and sprintId = :sprintId")
	void DeleteDomainsOfSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("Delete from  DEVELOPMENT_ENV_IN_A_SPRINT where projectId = :projectId and  sprintId  =:sprintId")
	void deleteDevelopmentEnvInaSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select exists( select 1 from  DEVELOPMENT_ENV_IN_A_SPRINT where projectId = :projectId and  sprintId  =:sprintId)")
	boolean ifExistsDevelopmentEnvInaSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("update SPRINTS set  sprintName = :sprintName, sprintDescription = :sprintDescription, sprintStartDate = :sprintStartDate, "
			+ "sprintEndDate = :sprintEndDate, sprintEstimatedEffort = :sprintEstimatedEffort, sprintActualEffort = :sprintActualEffort, "
			+ "sprintProgress = :sprintProgress, sprintStatus = :sprintStatus,"
			+ " sprintPhase = :sprintPhase, sprintLastUpdate = :sprintLastUpdate, sprintUpdatedBy = :sprintUpdatedBy, "
			+ "expectedTeamKnowledgeDiversity = :expectedTeamKnowledgeDiversity, expectedCoachingPotential = :expectedCoachingPotential "
			+ " where projectId = :projectId and sprintId = :sprintId")
	void update(@BindBean Sprint sprint);

	@SqlUpdate("update SPRINTS set  sprintName = :sprintName, sprintDescription = :sprintDescription, sprintStartDate = :sprintStartDate, "
			+ "sprintEndDate = :sprintEndDate, sprintEstimatedEffort = :sprintEstimatedEffort, sprintActualEffort = :sprintActualEffort, "
			+ "sprintStatus = :sprintStatus, sprintPhase = :sprintPhase, sprintLastUpdate = :sprintLastUpdate, sprintUpdatedBy = :sprintUpdatedBy"
			+ " where projectId = :projectId and sprintId = :sprintId")
	void updateSprintById(@BindBean Sprint sprint);

	@SqlUpdate("update SPRINTS set sprintLastUpdate = :sprintLastUpdate, sprintUpdatedBy = :sprintUpdatedBy, "
			+ "expectedTeamKnowledgeDiversity = :expectedTeamKnowledgeDiversity, expectedCoachingPotential = :expectedCoachingPotential "
			+ " where projectId = :projectId and sprintId = :sprintId")
	void updateSprintCompanyDrivenFactors(@BindBean Sprint sprint);

	@SqlUpdate("update SprintQuestionnaire set question1 = :question1, question2 = :question2, question3 = :question3, question4 = :question4, question5 = :question5, question6 = :question6, question7 = :question7,"
			+ "question8 = :question8, question1Comment = :question1Comment, question2Comment = :question2Comment, question3Comment = :question3Comment, question4Comment = :question4Comment, question5Comment = :question5Comment, question6Comment = :question6Comment, question7Comment = :question7Comment, question8Comment = :question8Comment, "
			+ "other =:other, lastUpdate = :lastUpdate, updatedBy = :updatedBy where projectId = :projectId and sprintId = :sprintId")
	void updateSprintQuestionnaire(@BindBean SprintQuestionnaireTemplate sprintQuestionnaire);

	@SqlQuery("select expectedTeamKnowledgeDiversity, expectedCoachingPotential from SPRINTS where projectId = :projectId and sprintId = :sprintId")
	List<CompanyDrivenFactorsNames> getCompanyDrivenFactorsOfSprint(@Bind("projectId") int projectId,
			@Bind("sprintId") int sprintId);

	@SqlQuery("select * from SPRINTS where projectId = :id")
	Sprint findBySprintId(@Bind("id") int id);

	@SqlQuery("select * from SPRINTS where projectId = :projectId and sprintId = :sprintId")
	List<Sprint> findByIds(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select TableA.domainId, SPRINTDOMAINSDB.domainName from SPRINTDOMAINSDB RIGHT JOIN (select * from DOMAINSINASPRINT where projectId "
			+ "= :projectId and sprintId = :sprintId) AS TableA ON TableA.domainId = SPRINTDOMAINSDB.domainId")
	List<SprintDomain> getDomainsOfSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select TableA.assetId, SPRINTASSETSDB.assetName from SPRINTASSETSDB RIGHT JOIN (select * from ASSETSINASPRINT where "
			+ "projectId = :projectId and sprintId = :sprintId) AS TableA ON TableA.assetId = SPRINTASSETSDB.assetId")
	List<SprintAsset> getAssetsOfSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select TableC.personId, PEOPLE.personName, TableC.roleName from PEOPLE RIGHT JOIN (select  TableB.projectId, TableB.sprintId, TableB.personId,ROLESDB.roleName from "
			+ "ROLESDB RIGHT JOIN(select PROJECTPARTICIPATION.roleId, TableA.projectId, TableA.sprintId, TableA.personId from PROJECTPARTICIPATION RIGHT JOIN (select * from SPRINTPARTICIPATION"
			+ " where sprintId= :sprintId and projectId = :projectId)AS TableA ON TableA.projectId = PROJECTPARTICIPATION.projectId and TableA.personId = PROJECTPARTICIPATION.personId) AS TableB ON "
			+ "TableB.roleId = ROLESDB.roleId) AS TableC ON TableC.personId = PEOPLE.personId ")
	List<RolesOfPeopleSprint> getSprintParticipants(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select * from SPRINTS")
	List<Sprint> getAll();

	@SqlQuery("Select  TableA.sprintId,PROJECTS.projectId, TableA.sprintName, PROJECTS.projectName, TableA.sprintDescription, TableA.sprintStartDate, TableA.sprintEndDate,"
			+ " TableA.sprintEstimatedEffort from PROJECTS RIGHT JOIN (select * from SPRINTS where projectId= :projectId and sprintId= :sprintId) AS TableA ON"
			+ " PROJECTS.projectId = TableA.projectId")
	List<SprintBriefSummary> getSprintBriefSummary(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select * from SPRINTDOMAINSDB where domainName like :domainName")
	List<SprintDomain> getDomainId(@Bind("domainName") String domainName);

	@SqlQuery("select  DISTINCT TableA.sprintRequirementId, TableA.outcome, SPRINTREQUIREMENTSDB.sprintRequirementName,  TableA.requirementLevel, SPRINTREQUIREMENTSDB.sprintRequirementDescription from SPRINTREQUIREMENTSDB RIGHT JOIN  "
			+ "(select * from REQUIREMENTSSELECTEDFORSPRINT where sprintId = :sprintId and projectId = :projectId )AS TableA ON TableA.sprintRequirementId = SPRINTREQUIREMENTSDB.sprintRequirementId")
	List<SprintRequirementNameLevel> getRequirementsSelectedInSprint(@Bind("projectId") int projectId,
			@Bind("sprintId") int sprintId);

	@SqlQuery("select * from SPRINTREQUIREMENTSDB where sprintRequirementName like :sprintRequirementName")
	List<SprintRequirement> getRequirementId(@Bind("sprintRequirementName") String sprintRequirementName);

	@SqlQuery("select * from SPRINTDOMAINSDB")
	List<SprintDomain> getAllDomains();

	@SqlQuery("select * from SPRINTREQUIREMENTSDB")
	List<SprintRequirement> getAllRequirements();

	@SqlQuery("select * from SPRINTDEVELOPMENTENVDB where envName like :envName")
	List<SprintDevelopmentEnvironment> getDevelopmentEnvId(@Bind("envName") String envName);

	@SqlQuery("select * from SPRINTDEVELOPMENTENVDB")
	List<SprintDevelopmentEnvironment> getAllDevelopmentEnv();

	@SqlQuery("select assetId, assetName from SPRINTASSETSDB where assetName like :assetName")
	List<SprintAsset> getAssetId(@Bind("assetName") String assetName);

	@SqlQuery("select * from SPRINTASSETSDB")
	List<SprintAsset> getAllAssets();

	@SqlQuery("select TableB.projectId projectId, TableB.projectName projectName, TableB.sprintid sprintId, TableB.sprintName sprintName, "
			+ "TableB.sprintProgress sprintProgress, TableB.personId AS teamMembers$personId, PEOPLE.personName AS teamMembers$personName"
			+ " from PEOPLE RIGHT JOIN (select TableA.projectId, TableA.projectName, TableA.sprintid, TableA.sprintName, TableA.sprintProgress, "
			+ "SPRINTPARTICIPATION.personId from SPRINTPARTICIPATION RIGHT JOIN (select PROJECTS.projectId, PROJECTS.projectName, SPRINTS.sprintid,"
			+ " SPRINTS.sprintName, SPRINTS.sprintProgress from SPRINTS RIGHT JOIN PROJECTS  ON SPRINTS.projectId = PROJECTS.projectId where sprintId IS NOT NULL) AS TableA ON "
			+ "SPRINTPARTICIPATION.sprintId = TableA.sprintId and SPRINTPARTICIPATION.projectId = TableA.projectId) AS TableB ON PEOPLE.personId = "
			+ "TableB.personId")
	FoldingList<SprintSummary> getSummary();

	@SqlQuery("select TableB.projectId projectId, TableB.projectName projectName, TableB.sprintid sprintId, TableB.sprintName sprintName, "
			+ "TableB.sprintProgress sprintProgress, TableB.personId AS teamMembers$personId, PEOPLE.personName AS teamMembers$personName"
			+ " from PEOPLE RIGHT JOIN (select TableA.projectId, TableA.projectName, TableA.sprintid, TableA.sprintName, TableA.sprintProgress, "
			+ "SPRINTPARTICIPATION.personId from SPRINTPARTICIPATION RIGHT JOIN (select PROJECTS.projectId, PROJECTS.projectName, SPRINTS.sprintid,"
			+ " SPRINTS.sprintName, SPRINTS.sprintProgress from SPRINTS RIGHT JOIN PROJECTS  ON SPRINTS.projectId = PROJECTS.projectId where sprintId"
			+ " IS NOT NULL and PROJECTS.projectId = :projectId ) AS TableA ON SPRINTPARTICIPATION.sprintId = TableA.sprintId and SPRINTPARTICIPATION.projectId = "
			+ "TableA.projectId) AS TableB ON PEOPLE.personId = TableB.personId")
	FoldingList<SprintSummary> getListOfSprintsInProject(@Bind("projectId") int projectId);

	@SqlQuery("select exists( select 1 from SPRINTS where projectId= :projectId and sprintId = :sprintId)")
	boolean ifSprintExists(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select exists( select 1 from SPRINTPARTICIPATION where projectId = :projectId and personId = :personId and sprintId= :sprintId)")
	boolean ifPersonParticipatesInSprint(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("sprintId") int sprintId);

	@SqlQuery("select exists( select 1 from SPRINTPARTICIPATION where projectId = :projectId and sprintId= :sprintId)")
	boolean ifSprintParticipationExists(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select exists( select 1 from SprintQuestionnaire where projectId = :projectId and sprintId = :sprintId)")
	boolean IfSprintQuestionnaireAnswered(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("insert into SPRINTPARTICIPATION (projectId, personId, sprintId, redmineProjectIdentifier, redmineSprintIdentifier, redminePersonIdentifier) values (:projectId, :personId, :sprintId, 'new', 'new', 'new')")
	void insertIntoSprintParticipation(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("sprintId") int sprintId);

	@SqlQuery("select exists( select 1 from DOMAINSINASPRINT where projectId= :projectId and sprintId = :sprintId and domainId = :domainId)")
	boolean ifDomainExistsInASprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("domainId") int domainId);

	@SqlQuery("select exists( select 1 from ASSETSINASPRINT where projectId= :projectId and sprintId = :sprintId and assetId = :assetId)")
	boolean ifAssetExistsInASprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("assetId") int assetId);

	@SqlQuery("select exists( select 1 from REQUIREMENTSSELECTEDFORSPRINT where projectId= :projectId and sprintId = :sprintId and sprintRequirementId = :sprintRequirementId)")
	boolean ifRequirementSelected(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("sprintRequirementId") int sprintRequirementId);

	@SqlUpdate("delete from SPRINTS where projectId = :projectId and sprintId = :sprintId")
	void deleteById(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("delete from SPRINTPARTICIPATION where projectId = :projectId and sprintId = :sprintId")
	void deleteSprintParticipants(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("update SPRINTPARTICIPATION set personExists = 1 where projectId = :projectId and sprintId = :sprintId and personId = :personId")
	void updateSprintParticipantExists(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("personId") int personId);

	@SqlUpdate("update SPRINTPARTICIPATION set personExists = 0 where projectId = :projectId and sprintId = :sprintId and personId = :personId")
	void updateSprintParticipantDoesnotExist(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("personId") int personId);

	@SqlUpdate("DELETE FROM sprintparticipation WHERE sprintId=  :sprintId and projectId =:projectId and personId NOT IN (select personId from sprintparticipation where personExists =1)")
	void deletePeopleWhoNoLongerParticipate(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

}
