package se.bth.didd.wiptool.db;

import java.sql.Timestamp;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.ProgrammingSkillsDetailsComparisonGraphs;
import se.bth.didd.wiptool.api.Skill;
import se.bth.didd.wiptool.api.SkillDetailsforGraphs;
import se.bth.didd.wiptool.api.SkillTimelineGraphs;

public interface SkillDAO {

	@SqlUpdate("create table if not exists SkillDB (skillId serial primary key, skillName varchar(50))")
	void createSkillDBTable();

	@SqlUpdate("create table if not exists ASSESSMENTOFSKILLS (personId int REFERENCES PEOPLE(personId), skillId int REFERENCES "
			+ "SkillDB(skillId), proficiency varchar(30), updatedBy varchar(30), lastUpdate timestamp, status varchar(10))")
	void createSkillAssessmentTable();

	@SqlUpdate("insert into SkillDB (skillName) values(:skillName)")
	void insertDefuaultValSkillDB(@Bind("skillName") String skillName);

	@SqlUpdate("insert into ASSESSMENTOFSKILLS (personId,skillId, proficiency,updatedBy,lastUpdate, status ) values(:personId, :skillId, :proficiency, :updatedBy, :lastUpdate, 'new')")
	void insertSkillAssessment(@Bind("personId") int personId, @Bind("skillId") int skillId,
			@Bind("proficiency") String proficiency, @Bind("updatedBy") String updatedBy,
			@Bind("lastUpdate") Timestamp timeStamp);

	@SqlQuery("select exists( select 1 from SkillDB where  skillName = :skillName)")
	boolean ifSkillExists(@Bind("skillName") String skillName);

	@SqlQuery("select exists( select 1 from ASSESSMENTOFSKILLS where  personId = :personId and skillId = :skillId)")
	boolean ifSkillAssessed(@Bind("personId") int personId, @Bind("skillId") int skillId);

	@SqlQuery("select * from SkillDB where skillName like :skillName")
	List<Skill> getSkillId(@Bind("skillName") String skillName);

	@SqlQuery("select * from SkillDB")
	List<Skill> getAllSkills();

	@SqlQuery("select SKILLDB.skillName,TableA.proficiency, TableA.lastUpdate, TableA.updatedBy from SKILLDB RIGHT JOIN (select  skillId, proficiency,lastUpdate,updatedBy  from ASSESSMENTOFSKILLS  where"
			+ " personId = :personId and skillId = :skillId ORDER BY lastUpdate DESC limit 1)AS TableA ON TableA.skillId = SKILLDB.skillId")
	List<SkillDetailsforGraphs> skillsDetailsOfPerson(@Bind("personId") int personId, @Bind("skillId") int skillId);

	@SqlQuery("select TableB.skillName, PEOPLE.personName, TableB.proficiency, TableB.lastUpdate, TableB.updatedBy from PEOPLE RIGHT JOIN (select SKILLDB.skillName,TableA.personId, TableA.proficiency, TableA.lastUpdate, TableA.updatedBy from SKILLDB RIGHT JOIN (select  *  from ASSESSMENTOFSKILLS  where personId = :personId and skillId = :skillId ORDER BY lastUpdate DESC limit 1)AS TableA ON TableA.skillId = SKILLDB.skillId) AS TableB ON TableB.personId = PEOPLE.personId")
	List<ProgrammingSkillsDetailsComparisonGraphs> specificSkillOfPerson(@Bind("personId") int personId,
			@Bind("skillId") int skillId);

	@SqlQuery("select proficiency, lastUpdate from ASSESSMENTOFSKILLS where personId = :personId and skillId = :skillId ORDER BY lastUpdate ASC")
	List<SkillTimelineGraphs> getDistinctProficienciesOfSkill(@Bind("personId") int personId,
			@Bind("skillId") int skillId);

	@SqlQuery("select TableA.skillId, SKILLDB.skillName from SKILLDB RIGHT JOIN (select distinct skillId from ASSESSMENTOFSKILLS where personId = :personId) AS TableA ON TableA.skillId = SKILLDB.skillId")
	List<Skill> getDistinctSkillsOfPerson(@Bind("personId") int personId);

	@SqlQuery("select * from PEOPLE where personId = :personId")
	List<People> getPersonDetails(@Bind("personId") int personId);

}
