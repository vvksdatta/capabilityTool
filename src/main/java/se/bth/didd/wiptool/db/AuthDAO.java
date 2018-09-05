package se.bth.didd.wiptool.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import se.bth.didd.wiptool.api.Login;

/*A simple DAO interface to handle the database operations required for authentication */

public interface AuthDAO {

	@SqlUpdate("create table if not exists LOGINCREDENTIALS (userId serial primary key, userName varchar(30),"
			+ " userFirstName varchar(30),userLastName varchar(30), userMailId varchar(30), password varchar(30))")
	void createLoginCredentialsTable();

	@SqlUpdate("insert into LOGINCREDENTIALS ( userName, userFirstName, userLastName, userMailId, password) values (:userName, :userFirstName, :userLastName, :userMailId, :password)")
	int insertIntoLoginCredentials(@BindBean Login login);

	@SqlQuery("select exists (select 1 from LOGINCREDENTIALS where userMailId = :userMailId  or userName = :userName )")
	boolean ifCredentialsExists(@Bind("userMailId") String userMailId, @Bind("userName") String userName);

	@SqlQuery("select * from LOGINCREDENTIALS where userMailId = :userMailId or userName = :userMailId")
	List<Login> getUser(@Bind("userMailId") String userMailId);

}
