package se.bth.didd.wiptool.db;

import java.sql.Timestamp;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import se.bth.didd.wiptool.api.Capability;
import se.bth.didd.wiptool.api.CapabilityDetailsComparisonGraphs;
import se.bth.didd.wiptool.api.CapabilityIdMeasure;
import se.bth.didd.wiptool.api.CapabilityIdProficiency;
import se.bth.didd.wiptool.api.CapabilityTimelineGraphs;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.ProjectIdCapabilityIdProficiency;
import se.bth.didd.wiptool.api.ProjectNameCapabilityFiveOptions;
import se.bth.didd.wiptool.api.CapabilityDetailsforGraphs;
import se.bth.didd.wiptool.api.CapabilityIdFiveOptions;

public interface CapabilityDAO {

	@SqlUpdate("create table if not exists CAPABILITYDB (capabilityId serial primary key, capabilityName varchar(50))")
	void createCapabilityDBTable();

	@SqlUpdate("create table if not exists CAPABILITYMEASURES (capabilityId int REFERENCES CAPABILITYDB(capabilityId), measure varchar(30))")
	void createCapabilityMeasuresTable();

	@SqlUpdate("create table if not exists ASSESSMENTOFCAPABILITIES (projectId int REFERENCES PROJECTS(projectId), personId int REFERENCES PEOPLE(personId), capabilityId int REFERENCES "
			+ "CAPABILITYDB(capabilityId), proficiency varchar(30), updatedBy varchar(30), lastUpdate timestamp, status varchar(10))")
	void createCapabilityAssessmentTable();

	@SqlUpdate("insert into CAPABILITYDB (capabilityName) values(:capabilityName)")
	void insertDefuaultValCapabilityDB(@Bind("capabilityName") String capabilityName);

	@SqlUpdate("insert into CAPABILITYMEASURES (capabilityId, measure) values(:capabilityId,:measure)")
	void insertDefuaultMeasuresCapabilities(@Bind("capabilityId") int capabilityId, @Bind("measure") String measure);

	@SqlUpdate("insert into ASSESSMENTOFCAPABILITIES (projectId, personId,capabilityId, proficiency,updatedBy,lastUpdate, status) values(:projectId, :personId, :capabilityId, :proficiency, :updatedBy, :lastUpdate, 'new')")
	void insertCapabilityAssessment(@Bind("projectId") int projectId, @Bind("personId") int personId,
			@Bind("capabilityId") int capabilityId, @Bind("proficiency") String proficiency,
			@Bind("updatedBy") String updatedBy, @Bind("lastUpdate") Timestamp timeStamp);

	@SqlQuery("select exists( select 1 from CAPABILITYDB where  capabilityName = :capabilityName)")
	boolean ifcapabilityExists(@Bind("capabilityName") String capabilityName);

	@SqlQuery("select exists( select 1 from ASSESSMENTOFCAPABILITIES where  personId = :personId and capabilityId = :capabilityId)")
	boolean ifcapabilityAssessed(@Bind("personId") int personId, @Bind("capabilityId") int capabilityId);

	@SqlQuery("select exists( select 1 from ASSESSMENTOFCAPABILITIES where  projectId = :projectId)")
	boolean ifcapabilitiesInProjectAssessed(@Bind("projectId") int projectId);

	@SqlQuery("select capabilityId, sum(case when proficiency = 'Superficial' then 1 else 0 end) AS option1, sum(case when proficiency = 'Satisfactory' then 1 else 0 end) AS option2, sum(case when proficiency = 'Good' then 1 else"
			+ " 0 end) AS option3, sum(case when proficiency = 'Excellent' then 1 else 0 end) AS option4, sum(case when proficiency = 'Perfect' then 1 else 0 end) AS option5 from (select TableD.personId, TableD.projectId, TableD.capabilityId,"
			+ " assessmentOfCapabilities.proficiency, TableD.lastUp as lastUpdate from assessmentOfCapabilities RIGHT JOIN (select distinct personId, capabilityId, projectId, MAX(lastUpdate) as lastUp  from assessmentofcapabilities where projectId is"
			+ " not null GROUP BY personId, projectId, capabilityId  ) AS TableD ON TableD.personId= assessmentOfCapabilities.personId and TableD.capabilityId= assessmentOfCapabilities.capabilityId and TableD.projectId= assessmentOfCapabilities.projectId "
			+ "and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId, projectId, capabilityId) AS TableAssessment where personId = :personId and (capabilityId = 1 or capabilityId = 2) and projectid is not null GROUP BY capabilityId")
	List<CapabilityIdFiveOptions> summaryOfFirstTwoCapabilities(@Bind("personId") int personId);

	@SqlQuery("select capabilityId, sum(case when proficiency = 'Undefined' then 1 else 0 end) AS option1, sum(case when proficiency = 'No match' then 1 else 0 end) AS option2, sum(case when proficiency = 'Average match' then 1 else 0 end) AS option3, "
			+ "sum(case when proficiency = 'Good match' then 1 else 0 end) AS option4, sum(case when proficiency = 'Excellent match' then 1 else 0 end) AS option5 from (select TableD.personId, TableD.projectId, TableD.capabilityId, assessmentOfCapabilities.proficiency,"
			+ " TableD.lastUp as lastUpdate from assessmentOfCapabilities RIGHT JOIN (select distinct personId, capabilityId, projectId, MAX(lastUpdate) as lastUp  from assessmentofcapabilities where projectId is not null GROUP BY personId, projectId, capabilityId )"
			+ " AS TableD ON TableD.personId= assessmentOfCapabilities.personId and TableD.capabilityId= assessmentOfCapabilities.capabilityId and TableD.projectId= assessmentOfCapabilities.projectId and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId,"
			+ " projectId, capabilityId) AS TableAssessment where personId = :personId and capabilityId = 3 and projectid is not null GROUP BY capabilityId")
	List<CapabilityIdFiveOptions> summaryOfThirdCapabilities(@Bind("personId") int personId);

	@SqlQuery("select capabilityId, sum(case when proficiency = 'Undefined' then 1 else 0 end) AS option1, sum(case when proficiency = 'Acceptable' then 1 else 0 end) AS option2, sum(case when proficiency = 'Good' then 1 else 0 end) AS option3, sum(case when proficiency = 'Excellent'"
			+ " then 1 else 0 end) AS option4, sum(case when proficiency = 'Outstanding' then 1 else 0 end) AS option5 from (select TableD.personId, TableD.projectId, TableD.capabilityId, assessmentOfCapabilities.proficiency, TableD.lastUp as lastUpdate from assessmentOfCapabilities"
			+ " RIGHT JOIN (select distinct personId, capabilityId, projectId, MAX(lastUpdate) as lastUp  from assessmentofcapabilities where projectId is not null GROUP BY personId, projectId, capabilityId  ) AS TableD ON TableD.personId= assessmentOfCapabilities.personId and "
			+ "TableD.capabilityId= assessmentOfCapabilities.capabilityId and TableD.projectId= assessmentOfCapabilities.projectId and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId, projectId, capabilityId) AS TableAssessment where personId = :personId"
			+ " and (capabilityId = 4 or capabilityId = 5 or capabilityId = 7  ) and projectid is not null GROUP BY capabilityId")
	List<CapabilityIdFiveOptions> summaryOfThreeCapabilities(@Bind("personId") int personId);

	@SqlQuery("select capabilityId, sum(case when proficiency = 'Undefined' then 1 else 0 end) AS option1, sum(case when proficiency = 'Average' then 1 else 0 end) AS option2, sum(case when proficiency = 'Good' then 1 else 0 end) AS option3, sum(case when proficiency = 'High' then 1 else"
			+ " 0 end) AS option4, sum(case when proficiency = 'Very high' then 1 else 0 end) AS option5 from (select TableD.personId, TableD.projectId, TableD.capabilityId, assessmentOfCapabilities.proficiency, TableD.lastUp as lastUpdate from assessmentOfCapabilities RIGHT JOIN (select "
			+ "distinct personId, capabilityId, projectId, MAX(lastUpdate) as lastUp  from assessmentofcapabilities where projectId is not null GROUP BY personId, projectId, capabilityId  ) AS TableD ON TableD.personId= assessmentOfCapabilities.personId and TableD.capabilityId= "
			+ "assessmentOfCapabilities.capabilityId and TableD.projectId= assessmentOfCapabilities.projectId and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId, projectId, capabilityId) AS TableAssessment where personId = :personId and (capabilityId = 6 or"
			+ " capabilityId = 8 ) and projectid is not null GROUP BY capabilityId")
	List<CapabilityIdFiveOptions> summaryOfLastTwoCapabilities(@Bind("personId") int personId);

	@SqlQuery("select distinct projects.projectName, TableA.capabilityId, TableA.option1, TableA.option2, TableA.option3, TableA.option4, TableA.option5  from projects RIGHT JOIN (select capabilityId, (case when proficiency='Superficial' then projectId else null end) AS option1, "
			+ "(case when proficiency='Satisfactory' then projectId else null end) AS option2, (case when proficiency='Good' then projectId else null end) AS option3, (case when proficiency='Excellent' then projectId else null end) AS option4, (case when proficiency='Perfect' then "
			+ "projectId else null end) AS option5 from (select TableD.personId, TableD.projectId, TableD.capabilityId, assessmentOfCapabilities.proficiency, TableD.lastUp as lastUpdate from assessmentOfCapabilities RIGHT JOIN (select distinct personId, capabilityId, projectId, "
			+ "MAX(lastUpdate) as lastUp  from assessmentofcapabilities where projectId is not null GROUP BY personId, projectId, capabilityId  ) AS TableD ON TableD.personId= assessmentOfCapabilities.personId and TableD.capabilityId= assessmentOfCapabilities.capabilityId and "
			+ "TableD.projectId= assessmentOfCapabilities.projectId and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId, projectId, capabilityId) AS TableAssessment where personid = :personId and capabilityId = :capabilityId and projectid is not null) "
			+ "AS TableA ON (TableA.option1 = projects.projectId or TableA.option2 = projects.projectId or TableA.option3 = projects.projectId or TableA.option4 = projects.projectId or TableA.option5 = projects.projectId) ")
	List<ProjectNameCapabilityFiveOptions> projectsFirstTwoCapabilities(@Bind("personId") int personId,
			@Bind("capabilityId") int capabilityId);

	@SqlQuery("select distinct projects.projectName, TableA.capabilityId, TableA.option1, TableA.option2, TableA.option3, TableA.option4, TableA.option5  from projects RIGHT JOIN (select capabilityId, (case when proficiency='Undefined' then projectId else null end) AS option1, "
			+ "(case when proficiency='No match' then projectId else null end) AS option2, (case when proficiency='Average match' then projectId else null end) AS option3, (case when proficiency='Good match' then projectId else null end) AS option4, (case when proficiency='Excellent "
			+ "match' then projectId else null end) AS option5 from (select TableD.personId, TableD.projectId, TableD.capabilityId, assessmentOfCapabilities.proficiency, TableD.lastUp as lastUpdate from assessmentOfCapabilities RIGHT JOIN (select distinct personId, capabilityId, projectId,"
			+ " MAX(lastUpdate) as lastUp  from assessmentofcapabilities where projectId is not null GROUP BY personId, projectId, capabilityId  ) AS TableD ON TableD.personId= assessmentOfCapabilities.personId and TableD.capabilityId= assessmentOfCapabilities.capabilityId and "
			+ "TableD.projectId= assessmentOfCapabilities.projectId and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId, projectId, capabilityId) AS TableAssessment where personid = :personId and capabilityId = :capabilityId and projectid is not null) AS TableA "
			+ "ON (TableA.option1 = projects.projectId or TableA.option2 = projects.projectId or TableA.option3 = projects.projectId or TableA.option4 = projects.projectId or TableA.option5 = projects.projectId) ")
	List<ProjectNameCapabilityFiveOptions> projectsThirdCapabilities(@Bind("personId") int personId,
			@Bind("capabilityId") int capabilityId);

	@SqlQuery("select distinct projects.projectName, TableA.capabilityId, TableA.option1, TableA.option2, TableA.option3, TableA.option4, TableA.option5  from projects RIGHT JOIN (select capabilityId, (case when proficiency='Undefined' then projectId else null end) AS option1, (case when "
			+ "proficiency='Acceptable' then projectId else null end) AS option2, (case when proficiency='Good' then projectId else null end) AS option3, (case when proficiency='Excellent' then projectId else null end) AS option4, (case when proficiency='Outstanding' then projectId else null "
			+ "end) AS option5 from (select TableD.personId, TableD.projectId, TableD.capabilityId, assessmentOfCapabilities.proficiency, TableD.lastUp as lastUpdate from assessmentOfCapabilities RIGHT JOIN (select distinct personId, capabilityId, projectId, MAX(lastUpdate) as lastUp  "
			+ "from assessmentofcapabilities where projectId is not null GROUP BY personId, projectId, capabilityId  ) AS TableD ON TableD.personId= assessmentOfCapabilities.personId and TableD.capabilityId= assessmentOfCapabilities.capabilityId and TableD.projectId= assessmentOfCapabilities.projectId"
			+ " and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId, projectId, capabilityId) AS TableAssessment where personid = :personId and capabilityId = :capabilityId and projectid is not null) AS TableA ON (TableA.option1 = projects.projectId or TableA.option2 = projects.projectId "
			+ "or TableA.option3 = projects.projectId or TableA.option4 = projects.projectId or TableA.option5 = projects.projectId) ")
	List<ProjectNameCapabilityFiveOptions> projectsThreeCapabilities(@Bind("personId") int personId,
			@Bind("capabilityId") int capabilityId);

	@SqlQuery("select distinct projects.projectName, TableA.capabilityId, TableA.option1, TableA.option2, TableA.option3, TableA.option4, TableA.option5  from projects RIGHT JOIN (select capabilityId, (case when proficiency='Undefined' then projectId else null end) AS option1, (case when proficiency='Average'"
			+ " then projectId else null end) AS option2, (case when proficiency='Good' then projectId else null end) AS option3, (case when proficiency='High' then projectId else null end) AS option4, (case when proficiency='Very high' then projectId else null end) AS option5 from (select TableD.personId, "
			+ "TableD.projectId, TableD.capabilityId, assessmentOfCapabilities.proficiency, TableD.lastUp as lastUpdate from assessmentOfCapabilities RIGHT JOIN (select distinct personId, capabilityId, projectId, MAX(lastUpdate) as lastUp  from assessmentofcapabilities where projectId is not null GROUP BY "
			+ "personId, projectId, capabilityId  ) AS TableD ON TableD.personId= assessmentOfCapabilities.personId and TableD.capabilityId= assessmentOfCapabilities.capabilityId and TableD.projectId= assessmentOfCapabilities.projectId and TableD.lastUp = assessmentOfCapabilities.lastUpdate ORDER BY  personId,"
			+ " projectId, capabilityId) AS TableAssessment where personid = :personId and capabilityId = :capabilityId and projectid is not null) AS TableA ON (TableA.option1 = projects.projectId or TableA.option2 = projects.projectId or TableA.option3 = projects.projectId or TableA.option4 = projects.projectId "
			+ "or TableA.option5 = projects.projectId) ")
	List<ProjectNameCapabilityFiveOptions> projectsLastTwoCapabilities(@Bind("personId") int personId,
			@Bind("capabilityId") int capabilityId);

	@SqlQuery("select capabilityId,proficiency from assessmentofcapabilities where personid = :personId ORDER BY lastUpdate DESC LIMIT 8")
	List<CapabilityIdProficiency> capabilitiesOfPerson(@Bind("personId") int personId);

	@SqlQuery("select projectId, capabilityId, proficiency from assessmentofcapabilities where personid = :personId and projectId = :projectId ORDER BY lastUpdate DESC LIMIT 8")
	List<ProjectIdCapabilityIdProficiency> capabilitiesOfPersoninProject(@Bind("projectId") int projectId,
			@Bind("personId") int personId);

	@SqlQuery("select TableB.capabilityName,PEOPLE.personName, TableB.proficiency, TableB.updatedBy, TableB.lastUpdate from PEOPLE RIGHT JOIN  (select capabilitydb.capabilityName,TableA.personId, TableA.proficiency, TableA.updatedBy, TableA.lastUpdate  from capabilitydb RIGHT JOIN ( select * from assessmentofcapabilities"
			+ " where personId = :personId and capabilityId = :capabilityId ORDER BY lastUpdate DESC LIMIT 1 ) As TableA ON capabilitydb.capabilityId = TableA.capabilityId ) As TableB ON TableB.personId = PEOPLE.personId")
	List<CapabilityDetailsComparisonGraphs> specificCapabilityOfPerson(@Bind("personId") int personId,
			@Bind("capabilityId") int capabilityId);

	@SqlQuery("select exists( select 1 from assessmentofcapabilities where personId = :personId and capabilityId = :capabilityId)")
	boolean capabilityOfPersonExists(@Bind("personId") int personId, @Bind("capabilityId") int capabilityId);

	@SqlQuery("select proficiency, lastUpdate from ASSESSMENTOFCAPABILITIES where personId = :personId and capabilityId = :capabilityId ORDER BY lastUpdate ASC")
	List<CapabilityTimelineGraphs> getDistinctProficienciesOfCapability(@Bind("personId") int personId,
			@Bind("capabilityId") int capabilityId);

	@SqlQuery("select capabilitydb.capabilityName, TableA.proficiency, TableA.updatedBy, TableA.lastUpdate  from capabilitydb RIGHT JOIN ( select * from assessmentofcapabilities where personid = :personId ORDER BY lastUpdate DESC LIMIT 8 ) As TableA ON "
			+ "capabilitydb.capabilityId = TableA.capabilityId ")
	List<CapabilityDetailsforGraphs> capabilityDetailsOfPerson(@Bind("personId") int personId);

	@SqlQuery("select * from CAPABILITYMEASURES  where capabilityId=:capabilityId")
	List<CapabilityIdMeasure> getMeasuresOfCapability(@Bind("capabilityId") int capabilityId);

	@SqlQuery("select * from CAPABILITYDB where  capabilityName = :capabilityName")
	List<Capability> getCapabilityId(@Bind("capabilityName") String capabilityName);

	@SqlQuery("select * from CAPABILITYDB")
	List<Capability> getAllCapabilities();

	@SqlQuery("select * from PEOPLE where personId = :personId")
	List<People> getPersonDetails(@Bind("personId") int personId);

	@SqlQuery("select count (*) as numb from CAPABILITYDB")
	List<Integer> getNumbOfCapabilities();

}
