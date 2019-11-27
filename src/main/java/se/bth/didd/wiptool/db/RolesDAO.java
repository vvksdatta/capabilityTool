package se.bth.didd.wiptool.db;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeople;

public interface RolesDAO {

	@SqlUpdate("create table if not exists ROLESDB (roleId int primary key, roleName varchar(30), roleStatus varchar(10))")
	void createRolesDBTable();

	@SqlUpdate("create table if not exists RolesOfPeople (personId int REFERENCES PEOPLE(personId),"
			+ " roleId int REFERENCES ROLESDB(roleId),status varchar(10), PRIMARY KEY(personId, roleId))")
	void createRolesOfPeopleTable();

	@SqlQuery("select exists( select 1 from ROLESDB where roleId= :roleId and roleName = :roleName)")
	boolean ifRoleExists(@Bind("roleId") int roleId, @Bind("roleName") String roleName);

	@SqlQuery("select exists( select 1 from RolesOfPeople where roleId= :roleId and personId = :personId)")
	boolean ifRoleOfPersonExists(@Bind("roleId") int roleId, @Bind("personId") int personId);

	@SqlUpdate("insert into ROLESDB (roleId, roleName, roleStatus) values (:roleId, :roleName, 'new')")
	void insert(@BindBean Roles roles);

	@SqlUpdate("insert into RolesOfPeople (personId ,roleId, status) values (:personId, :roleId, 'new' )")
	void insertIntoRolesOfPeopleTable(@Bind("personId") int personId, @Bind("roleId") int roleId);

	@SqlQuery("select * from ROLESDB")
	List<Roles> getRolesList();

	@SqlQuery("select * from ROLESDB where roleId= :roleId")
	List<Roles> getRoleName(@Bind("roleId") int roleId);

	@SqlQuery("select * from ROLESDB where roleName= :roleName")
	List<Roles> getRoleId(@Bind("roleName") String roleName);

	@SqlQuery("select TableB.personId, TableB.roleId, TableB.roleName, PEOPLE.personName from PEOPLE RIGHT JOIN (select TableA.personId, TableA.roleId, ROLESDB.roleName from ROLESDB RIGHT JOIN "
			+ "(select * from ROLESOFPEOPLE ) AS TableA ON TableA.roleId = ROLESDB.roleId ) AS TableB ON TableB.personId = PEOPLE.personId ")
	List<RolesOfPeople> getRolesOfPeople();
	
	@SqlUpdate("Delete from ROLESDB where roleId= 5")
	void deleteReporterRole();
	
	@SqlUpdate("Delete from RolesOfPeople where roleId= 5")
	void deleteReporterRolesOfPeople();
}
