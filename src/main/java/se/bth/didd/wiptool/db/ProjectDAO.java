package se.bth.didd.wiptool.db;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import com.github.rkmk.container.FoldingList;
import se.bth.didd.wiptool.api.Projects;
import se.bth.didd.wiptool.api.ProjectsList;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeople;
import se.bth.didd.wiptool.api.ProjectIdName;
import se.bth.didd.wiptool.api.ProjectParticipants;
import se.bth.didd.wiptool.api.ProjectSummary;

/*A simple DAO interface to handle the database operations required to manipulate projects and projectsSummary entities */

public interface ProjectDAO {

	@SqlUpdate("create table if not exists PROJECTS (projectId int primary key, parentProjectId int, projectName varchar(30), "
			+ "projectDescription text, projectStartDate date, projectEndDate date, "
			+ "projectLeader int REFERENCES PEOPLE(personId), projectEstimatedEffort int, projectProgress int,"
			+ "projectActualEffort int, projectStatus varchar(20), projectLastUpdate timestamp, redmineLastUpdate "
			+ "timestamp with time zone, projectUpdatedBy varchar(20), redmineProjectIdentifier varchar(10), projectLeaderIdentifier varchar(10))")
	void createProjectTable();

	@SqlUpdate("create table if not exists PROJECTPARTICIPATION (projectId int REFERENCES PROJECTS(projectId),"
			+ " personId int REFERENCES PEOPLE(personId), roleId int, redmineProjectIdentifier varchar(10), redminePersonIdentifier varchar(10), PRIMARY KEY(projectId, personId, roleId))")
	void createProjectParticipationTable();

	@SqlUpdate("insert into PROJECTS (projectId, parentProjectId, projectName, projectDescription, projectStartDate, projectEndDate, "
			+ "projectLeader, projectEstimatedEffort,  projectStatus, projectLastUpdate, redmineLastUpdate, projectUpdatedBy, redmineProjectIdentifier, projectLeaderIdentifier)"
			+ " values (:projectId, :parentProjectId, :projectName, :projectDescription, :projectStartDate, :projectEndDate, :projectLeader, "
			+ ":projectEstimatedEffort,  :projectStatus, :projectLastUpdate, :redmineLastUpdate, :projectUpdatedBy, 'new', 'new')")
	void createProject(@BindBean Projects project);

	@SqlUpdate("insert into PROJECTPARTICIPATION (projectId, personId, roleId, redmineProjectIdentifier, redminePersonIdentifier) values (:projectId, :personId, :roleId, 'new', 'new')")
	void insertIntoProjectParticipation(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("roleId") int roleId);

	@SqlQuery("select TableA.personId, TableA.roleId, ROLESDB.roleName from ROLESDB RIGHT JOIN "
			+ "(select * from PROJECTPARTICIPATION where projectId = :projectId) AS TableA ON TableA.roleId = ROLESDB.roleId ")
	List<RolesOfPeople> getRolesOfPeopleInProject(@Bind("projectId") int projectId);

	@SqlQuery("select TableA.roleId, rolesDB.roleName from rolesDB RIGHT JOIN (select roleId from projectparticipation where projectId = :projectId) AS TableA ON TableA.roleId = rolesDB.roleId ")
	List<Roles> getNumberofParticipants(@Bind("projectId") int projectId);

	@SqlQuery("select TableA.roleId, rolesDB.roleName from rolesDB RIGHT JOIN (select DISTINCT roleId from projectparticipation where projectId =:projectId) AS TableA ON TableA.roleId = rolesDB.roleId")
	List<Roles> getDistinctRolesInProject(@Bind("projectId") int projectId);

	@SqlUpdate("update PROJECTS set parentProjectId = :parentProjectId, projectName = :projectName, projectDescription = :projectDescription,"
			+ " projectEndDate = :projectEndDate, projectStartDate = :projectStartDate, projectLeader = :projectLeader, "
			+ " projectEstimatedEffort = :projectEstimatedEffort, projectActualEffort = :projectActualEffort, projectStatus = :projectStatus,"
			+ " projectLastUpdate = :projectLastUpdate, projectUpdatedBy = :projectUpdatedBy  where projectId = :projectId")
	void updateProject(@BindBean Projects project);

	@SqlQuery("select * from PROJECTS where projectId = :id")
	Projects findById(@Bind("id") int id);

	@SqlQuery("select projectId, projectName from PROJECTS")
	List<ProjectIdName> getAllProjects();

	@SqlQuery("select projectId, projectName from PROJECTS where parentProjectId IS NULL or parentProjectId = 0")
	List<ProjectIdName> getAllIndependentProjects();

	@SqlQuery("select projectId, projectName from PROJECTS where parentProjectId = :parentProjectId")
	List<ProjectsList> getSubProjectsList(@Bind("parentProjectId") int parentProjectId);

	@SqlQuery("select exists (select 1 from PROJECTS where parentProjectId = :parentProjectId)")
	boolean subProjectsExist(@Bind("parentProjectId") int parentProjectId);

	@SqlQuery("select projectId, projectName,parentprojectId from projects where parentprojectId is not null and projectId NOT IN (select ParentProjectId from projects where parentProjectId is not null)")
	List<ProjectIdName> getAllChildProjects();

	@SqlQuery("select * from ROLESDB where roleId= :roleId")
	List<Roles> getRoleName(@Bind("roleId") int roleId);

	@SqlQuery("select * from ROLESDB where roleName= :roleName")
	List<Roles> getRoleId(@Bind("roleName") String roleName);

	@SqlQuery("select exists (select 1 from PROJECTS where projectId= :projectId)")
	boolean ifProjectExists(@Bind("projectId") int projectId);

	@SqlQuery("select exists( select 1 from PROJECTPARTICIPATION where projectId = :projectId and personId = :personId and roleId= :roleId)")
	boolean ifPersonParticipatesInProject(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("roleId") int roleId);

	@SqlQuery("select exists( select 1 from PROJECTPARTICIPATION where projectId = :projectId and personId = :personId)")
	boolean ifPersonExistsInProject(@Bind("projectId") int projectId, @Bind("personId") int personId);

	@SqlQuery("select TableA.personId, PEOPLE.personName, TableA.roleId, TableA.roleName from PEOPLE RIGHT JOIN (select  PROJECTPARTICIPATION.personId,"
			+ " PROJECTPARTICIPATION.roleId, ROLESDB.roleName from PROJECTPARTICIPATION RIGHT JOIN ROLESDB ON "
			+ "PROJECTPARTICIPATION.roleId = ROLESDB.roleId  where projectId = :id ) AS TableA ON TableA.personId = PEOPLE.personId ")
	List<ProjectParticipants> getParticipants(@Bind("id") int id);

	@SqlUpdate("delete from PROJECTPARTICIPATION where projectId = :projectId and personId = :personId and roleId= :roleId")
	void deleteProjectParticipant(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("roleId") int roleId);

	@SqlUpdate("delete from PROJECTPARTICIPATION where projectId = :projectId ")
	void deleteAllParticipants(@Bind("projectId") int projectId);

	/*
	 * jdbi-folder specifies to use "namespace" in the query. That is, we have
	 * to use the same namespace "name" in the result field name followed by $.
	 * For example to access the id of the song, We need to say song$id.
	 * Refer: @see(https://github.com/Manikandan-K/jdbi-folder)
	 */
	@SqlQuery("Select TableE.personName AS projectMembers$personName,TableE.projectId AS projectId,TableE.projectName AS projectName,"
			+ " TableE.parentProjectId AS parentProjectId , TableE.parentProjectName AS parentProjectName,  TableE.projectProgress AS "
			+ "projectProgress, TableE.projectLeader AS projectLeader, TableE.personId projectMembers$personId, TableD.sprintId AS "
			+ "sprints$sprintId, TableD.sprintName AS sprints$sprintName, TableD.sprintProgress AS sprints$sprintProgress from SPRINTS"
			+ " TableD RIGHT JOIN (select TableW.personName, TableW.projectId, TableW.projectProgress, TableW.projectName, TableW.parentProjectId,"
			+ " TableW.parentProjectName, PEOPLE.personName AS projectLeader ,TableW.personId from PEOPLE RIGHT JOIN (select TableC.personName,"
			+ " TableA.projectId, TableA.projectName, TableA.parentProjectId, TableA.parentProjectName, TableA.projectLeader,TableA.personId, "
			+ "TableA.projectProgress from PEOPLE TableC RIGHT JOIN (select  TableX.projectId , TableX.parentProjectId, PROJECTS.projectName "
			+ "AS parentProjectName, TableX.projectName , TableX.projectLeader, TableX.projectProgress, TableX.personId   from PROJECTS RIGHT JOIN "
			+ "(select PROJECTS.projectId , PROJECTS.parentProjectId, PROJECTS.projectName , PROJECTS.projectLeader, PROJECTS.projectProgress,"
			+ "PROJECTPARTICIPATION.personId from PROJECTPARTICIPATION RIGHT JOIN PROJECTS ON PROJECTPARTICIPATION.projectId=PROJECTS.projectId) "
			+ "AS TableX ON TableX.parentProjectId = PROJECTS.projectId )AS TableA ON tablec.personid =TableA.personId ) AS TableW ON TableW.projectLeader"
			+ " = PEOPLE.personId ) AS TableE ON TableD.projectId = TableE.projectId ORDER BY projectId ASC, parentProjectId ASC")
	FoldingList<ProjectSummary> getSummary();

	@SqlUpdate("delete from PROJECTS where projectId = :id")
	void deleteById(@Bind("id") int id);

}
