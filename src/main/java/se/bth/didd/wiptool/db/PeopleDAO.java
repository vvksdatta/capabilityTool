package se.bth.didd.wiptool.db;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import com.github.rkmk.container.FoldingList;
import se.bth.didd.wiptool.api.ExistingPerson;
import se.bth.didd.wiptool.api.NewPerson;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.PeopleSummary;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.UserPasswordChange;
import se.bth.didd.wiptool.api.UserTemplate;

public interface PeopleDAO {
	@SqlUpdate("create table if not exists PEOPLE (personId int primary key, personName varchar(40), firstName varchar(40), lastName varchar(40), emailId varchar(40), redminePersonIdentifier varchar(10))")
	void createPeopleTable();

	@SqlUpdate("insert into PEOPLE (personId, personName, firstName, lastname, emailId, redminePersonIdentifier) values (:personId, :personName, :firstName, :lastName, :emailId, 'new')")
	void insertIntoPeopleTable(@Bind("personId") int personId, @Bind("personName") String personName,
			@Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("emailId") String emailId);

	@SqlUpdate("update PEOPLE set personName = :personName, firstName = :firstName, lastName = :lastName, emailId = :emailId where personId= :personId")
	void updatePerson(@Bind("personId") int personId, @Bind("personName") String personName,
			@Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("emailId") String emailId );

	@SqlUpdate("insert into RolesOfPeople (personId ,roleId, status) values (:personId, :roleId, 'new' )")
	void insertIntoRolesOfPeopleTable(@Bind("personId") int personId, @Bind("roleId") int roleId);

	@SqlQuery("select exists (select 1 from RolesOfPeople where personId = :personId and roleId = :roleId)")
	boolean ifPersonAssignedRole(@Bind("personId") int personId, @Bind("roleId") int roleId);

	@SqlQuery("select exists (select 1 from LOGINCREDENTIALS where userName = :userName and password = :password  and userId = :userId )")
	boolean ifCurrentPasswordExists(@Bind("userId") Integer userId, @Bind("userName") String userName,
			@Bind("password") String password);

	@SqlQuery("select * from PEOPLE")
	List<People> getAll();

	@SqlQuery("select PEOPLE.personId, PEOPLE.personName from PEOPLE RIGHT JOIN (select * from PROJECTPARTICIPATION where projectId = :projectId) AS TableA ON "
			+ "TableA.personId = PEOPLE.personId")
	List<People> peopleNamesInaProject(@Bind("projectId") int projectId);

	@SqlQuery("select * from PEOPLE where personId = :personId")
	List<People> getPersonDetails(@Bind("personId") int personId);

	@SqlQuery("select userId, userFirstName, userlastName, userMailId, userName, apiKey from LOGINCREDENTIALS")
	List<UserTemplate> getUsersList();

	@SqlQuery("select userId, userFirstName, userlastName, userMailId,  userName, apiKey, role from LOGINCREDENTIALS where userId = :userId")
	List<UserTemplate> getUserDetails(@Bind("userId") int userId);

	@SqlQuery("select apiKey from LOGINCREDENTIALS where userId = :userId")
	List<String> getApiKeyOfUser(@Bind("userId") int userId);

	@SqlUpdate("Update LOGINCREDENTIALS set  userFirstName = :userFirstName, userLastName = :userLastName, usermailId = :usermailId,  userName = :userName, apiKey = :apiKey, role = :role  where userId = :userId")
	void UpdateUserDetails(@BindBean UserTemplate user);

	@SqlUpdate("Update LOGINCREDENTIALS set  userName = :userName, password = :newPassword where userId = :userId")
	void updateUserPassword(@BindBean UserPasswordChange user);

	@SqlQuery("select personId,personName from PEOPLE")
	List<People> getAllPeople();

	@SqlQuery("select * from PEOPLE where personId = :personId")
	List<ExistingPerson> getPerson(@Bind("personId") int personId);

	@SqlQuery("select * from ROLESDB where roleName= :roleName")
	List<Roles> getRoleId(@Bind("roleName") String roleName);

	@SqlQuery("select exists (select 1 from LOGINCREDENTIALS where userId = :userId)")
	boolean ifUserExists(@Bind("userId") int userId);

	@SqlQuery("select exists (select 1 from PEOPLE where personId = :personId and personName = :personName)")
	boolean ifPersonExists(@Bind("personId") int personId, @Bind("personName") String personName);

	@SqlQuery("select exists (select 1 from PEOPLE where personId = :personId)")
	boolean ifPersonIdExists(@Bind("personId") int personId);

	@SqlQuery("select TabA.issueId AS issues$issueId,TabA.projectId AS projects$projectId, TabA.sprintId "
			+ "AS sprints$sprintId, TabB.projectName AS projects$projectName, TabC.personId AS personId, "
			+ "TabD.personName AS personName, TabE.sprintName AS sprints$sprintName from SPRINTCOMPRISINGISSUES "
			+ "TabA JOIN PROJECTS TabB USING(projectId) JOIN ISSUES TabC USING(IssueId) "
			+ "JOIN PEOPLE TabD USING(personId) JOIN SPRINTS TabE USING(sprintId)")
	FoldingList<PeopleSummary> getSummary();

	@SqlQuery("select TabA.issueId AS issues$issueId,TabA.projectId AS projects$projectId, TabA.sprintId "
			+ "AS sprints$sprintId, TabB.projectName AS projects$projectName, TabC.personId AS personId, "
			+ "TabD.personName AS personName, TabE.sprintName AS sprints$sprintName from SPRINTCOMPRISINGISSUES "
			+ "TabA JOIN PROJECTS TabB USING(projectId) JOIN ISSUES TabC USING(IssueId) "
			+ "JOIN PEOPLE TabD USING(personId) JOIN SPRINTS TabE USING(sprintId) where TabA.projectId= :projectId")
	FoldingList<PeopleSummary> summaryOfPeopleInProject(@Bind("projectId") int projectId);

	@SqlQuery("select TabA.firstName AS firstName, TabA.lastName AS lastName, TabA.emailId AS emailId, TabB.roleId AS "
			+ "roles$roleId, TabC.roleName AS roles$roleName from PEOPLE TabA JOIN RolesOfPeople TabB USING(personId)JOIN ROLESDB TabC USING(roleId) where personid= :personId")
	FoldingList<NewPerson> getPersonDetailsRoles(@Bind("personId") int personId);

	@SqlQuery("select exists(select TabA.firstName AS firstName, TabA.lastName AS lastName, TabA.emailId AS emailId, TabB.roleId AS "
			+ "roles$roleId, TabC.roleName AS roles$roleName from PEOPLE TabA JOIN RolesOfPeople TabB USING(personId)JOIN ROLESDB TabC USING(roleId) where personid= :personId)")
	boolean ifPersonRolesexists(@Bind("personId") int personId);

	@SqlUpdate("delete from SPRINTS where projectId = :projectId and sprintId = :sprintId")
	void deleteById(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("delete from  RolesOfPeople where personId = :personId")
	void deleteRolesOfPerson(@Bind("personId") int personId);

	@SqlUpdate("delete from LOGINCREDENTIALS where userId = :userId and userName != 'admin'")
	void deleteUser(@Bind("userId") int userId);

}
