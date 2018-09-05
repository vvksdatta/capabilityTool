package se.bth.didd.wiptool.db;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;


import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.Capability;
import se.bth.didd.wiptool.api.CapabilityDetailsComparisonGraphs;
import se.bth.didd.wiptool.api.CapabilityIdMeasure;
import se.bth.didd.wiptool.api.CapabilityIdProficiency;
import se.bth.didd.wiptool.api.CapabilityTimelineGraphs;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.CapabilityDetailsforGraphs;

public interface CapabilityDAO {

	@SqlUpdate("create table if not exists CAPABILITYDB (capabilityId serial primary key, capabilityName varchar(50))")
	void createCapabilityDBTable();
	
	@SqlUpdate("create table if not exists CAPABILITYMEASURES (capabilityId int REFERENCES CAPABILITYDB(capabilityId), measure varchar(30))")
	void createCapabilityMeasuresTable();
	
	@SqlUpdate("create table if not exists ASSESSMENTOFCAPABILITIES (personId int REFERENCES PEOPLE(personId), capabilityId int REFERENCES "
			+ "CAPABILITYDB(capabilityId), proficiency varchar(30), updatedBy varchar(30), lastUpdate timestamp)")
	void createCapabilityAssessmentTable();

	@SqlUpdate("insert into CAPABILITYDB (capabilityName) values(:capabilityName)")
	void insertDefuaultValCapabilityDB(@Bind("capabilityName") String capabilityName);
	
	@SqlUpdate("insert into CAPABILITYMEASURES (capabilityId, measure) values(:capabilityId,:measure)")
	void insertDefuaultMeasuresCapabilities(@Bind("capabilityId") int capabilityId, @Bind("measure") String measure);
	
	@SqlUpdate("insert into ASSESSMENTOFCAPABILITIES (personId,capabilityId, proficiency,updatedBy,lastUpdate ) values(:personId, :capabilityId, :proficiency, :updatedBy, :lastUpdate)")
	void insertCapabilityAssessment(@Bind("personId") int personId,@Bind("capabilityId") int capabilityId, @Bind("proficiency") String proficiency, @Bind("updatedBy") String updatedBy, @Bind("lastUpdate") Timestamp timeStamp);
	
	@SqlQuery("select exists( select 1 from CAPABILITYDB where  capabilityName = :capabilityName)")
	boolean ifcapabilityExists(@Bind("capabilityName") String capabilityName );
	
	@SqlQuery("select exists( select 1 from ASSESSMENTOFCAPABILITIES where  personId = :personId and capabilityId = :capabilityId)")
	boolean ifcapabilityAssessed(@Bind("personId") int  personId, @Bind("capabilityId") int  capabilityId );
	
	@SqlQuery("select capabilityId,proficiency from assessmentofcapabilities where personid = :personId ORDER BY lastUpdate DESC LIMIT 8")
	List<CapabilityIdProficiency> capabilitiesOfPerson(@Bind("personId") int  personId);
	
	@SqlQuery("select TableB.capabilityName,PEOPLE.personName, TableB.proficiency, TableB.updatedBy, TableB.lastUpdate from PEOPLE RIGHT JOIN  (select capabilitydb.capabilityName,TableA.personId, TableA.proficiency, TableA.updatedBy, TableA.lastUpdate  from capabilitydb RIGHT JOIN ( select * from assessmentofcapabilities where personId = :personId and capabilityId = :capabilityId ORDER BY lastUpdate DESC LIMIT 1 ) As TableA ON capabilitydb.capabilityId = TableA.capabilityId ) As TableB ON TableB.personId = PEOPLE.personId")
	List<CapabilityDetailsComparisonGraphs> specificCapabilityOfPerson(@Bind("personId") int  personId, @Bind("capabilityId") int  capabilityId);
	
	@SqlQuery("select exists( select 1 from assessmentofcapabilities where personId = :personId and capabilityId = :capabilityId)")
	boolean capabilityOfPersonExists(@Bind("personId") int  personId, @Bind("capabilityId") int  capabilityId);
	
	@SqlQuery("select proficiency, lastUpdate from ASSESSMENTOFCAPABILITIES where personId = :personId and capabilityId = :capabilityId ORDER BY lastUpdate ASC")
	List<CapabilityTimelineGraphs> getDistinctProficienciesOfCapability(@Bind("personId") int  personId, @Bind("capabilityId") int  capabilityId);
	
	@SqlQuery("select capabilitydb.capabilityName, TableA.proficiency, TableA.updatedBy, TableA.lastUpdate  from capabilitydb RIGHT JOIN ( select * from assessmentofcapabilities where personid = :personId ORDER BY lastUpdate DESC LIMIT 8 ) As TableA ON "
			+ "capabilitydb.capabilityId = TableA.capabilityId ")
	List<CapabilityDetailsforGraphs> capabilityDetailsOfPerson(@Bind("personId") int  personId);
	
	@SqlQuery("select * from CAPABILITYMEASURES  where capabilityId=:capabilityId")
	List<CapabilityIdMeasure> getMeasuresOfCapability(@Bind("capabilityId") int capabilityId);
	
	@SqlQuery("select * from CAPABILITYDB where  capabilityName = :capabilityName")
	List<Capability> getCapabilityId(@Bind("capabilityName") String capabilityName );
	
	@SqlQuery("select * from CAPABILITYDB")
	List<Capability> getAllCapabilities();
	
	@SqlQuery("select * from PEOPLE where personId = :personId")
	List<People> getPersonDetails(@Bind("personId") int personId);
	
	@SqlQuery("select count (*) as numb from CAPABILITYDB")
	List<Integer> getNumbOfCapabilities(); 
	
	//@SqlQuery("select distinct proficiency, lastupdate  from assessmentofcapabilities where capabilityid =2)
			
			
}
