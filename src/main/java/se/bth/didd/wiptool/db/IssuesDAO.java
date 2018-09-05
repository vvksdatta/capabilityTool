package se.bth.didd.wiptool.db;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import se.bth.didd.wiptool.api.IssueTemplate;
import se.bth.didd.wiptool.api.IssueUpdateTemplate;
import se.bth.didd.wiptool.api.Sprint;

public interface IssuesDAO {
	@SqlUpdate("create table if not exists ISSUES (issueId int, projectId int REFERENCES PROJECTS(projectId),personId int REFERENCES PEOPLE(personId), "
			+ " issueName varchar(60), issueStartDate date, issueDueDate date, issueCategory varchar(40), issuePriority varchar(20), securityRiskAnalysis varchar(20), issueDescription text,"
			+ "securityLevel varchar(20), issueEstimatedTime NUMERIC(10,3) , issueDone int, redmineLastUpdate timestamp with time zone, issueLastUpdate timestamp, redmineProjectIdentifier varchar(10), redmineIssueIdentifier varchar(10), PRIMARY KEY (issueId, projectId))")
	void createIssuesTable();

	@SqlQuery("select * from ISSUES RIGHT JOIN (select issueId from sprintcomprisingissues where sprintId=:sprintId and projectid =:projectId) AS TableA ON TableA.issueId = ISSUES.issueId where"
			+ " ISSUES.issueCategory !='null'")
	List<IssueTemplate> getSpecialIssuesInSprint(@Bind("sprintId") int sprintId, @Bind("projectId") int projectId);

	@SqlUpdate("delete from SPRINTS where projectId = :projectId and sprintId = :sprintId")
	void deleteById(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select exists( select 1 from ISSUES where projectId = :projectId and issueId = :issueId)")
	boolean ifIssueExistsInProject(@Bind("projectId") int projectId, @Bind("issueId") int issueId);

	@SqlQuery("select exists(select 1 from SPRINTCOMPRISINGISSUES where projectId= :projectId and sprintId = :sprintId and issueId = :issueId )")
	boolean ifIssueExistsInSprint(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("issueId") int issueId);

	@SqlQuery("select exists( select 1 from SPRINTS where projectId= :projectId and sprintId = :sprintId)")
	boolean ifSprintExists(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlQuery("select * from SPRINTS where projectId = :projectId and sprintId = :sprintId")
	List<Sprint> getSprintName(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId);

	@SqlUpdate("update ISSUES set issueId = :issueId, projectId = :projectId, personId = :personId, securityRiskAnalysis = :securityRiskAnalysis, securityLevel = :securityLevel,"
			+ " issueDone = :issueDone, redmineLastUpdate = :redmineLastUpdate, issueLastUpdate = :issueLastUpdate"
			+ " where projectId = :projectId and issueId = :issueId")
	void updateIssuesTable(@BindBean IssueUpdateTemplate issue);

	@SqlUpdate("insert into SPRINTCOMPRISINGISSUES (projectId, sprintId, issueId, redmineProjectIdentifier, redmineSptintIdentifier, redmineIssueIdentifier) values (:projectId, :sprintId, :issueId, 'new', 'new', 'new')")
	void insertIntoSprintComprisingIssuesTable(@Bind("projectId") int projectId, @Bind("sprintId") int sprintId,
			@Bind("issueId") int issueId);

}
