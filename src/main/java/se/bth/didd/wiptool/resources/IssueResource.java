package se.bth.didd.wiptool.resources;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.Version;
import com.taskadapter.redmineapi.bean.VersionFactory;

import se.bth.didd.wiptool.api.AllocatedIssue;
import se.bth.didd.wiptool.api.Capability;
import se.bth.didd.wiptool.api.CapabilityDetailsComparisonGraphs;
import se.bth.didd.wiptool.api.CapabilityDetailsforGraphs;
import se.bth.didd.wiptool.api.CapabilityTimelineGraphs;
import se.bth.didd.wiptool.api.CapabilityValueOfPeople;
import se.bth.didd.wiptool.api.ErrorMessage;
import se.bth.didd.wiptool.api.IssueTemplate;
import se.bth.didd.wiptool.api.IssueUpdateTemplate;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.PersonId;
import se.bth.didd.wiptool.api.ProgrammingSkillValueOfPeople;
import se.bth.didd.wiptool.api.ProgrammingSkillsDetailsComparisonGraphs;
import se.bth.didd.wiptool.api.Skill;
import se.bth.didd.wiptool.api.SkillDetailsforGraphs;
import se.bth.didd.wiptool.api.SkillNameValue;
import se.bth.didd.wiptool.api.SkillNameValueUser;
import se.bth.didd.wiptool.api.SkillTabsTemplate;
import se.bth.didd.wiptool.api.SkillTimelineGraphs;
import se.bth.didd.wiptool.api.Sprint;
import se.bth.didd.wiptool.api.SprintIdProjectIdIssuesAllocated;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.db.IssuesDAO;
import se.bth.didd.wiptool.db.SkillDAO;

@Path("/issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueResource {

	private String redmineUrl;
	private String apiAccessKey;

	IssuesDAO issueDAO;
	public IssueResource(IssuesDAO issueDAO,String redmineUrl, String apiAccessKey) {
		this.redmineUrl = redmineUrl;
		this.apiAccessKey = apiAccessKey;
		this.issueDAO = issueDAO;
	}


	@GET
	@Path("/getSpecialIssuesInSprint/{sprintId}/{projectId}")
	public Response getSpecialIssuesInSprint(@PathParam("sprintId") Integer sprintId, @PathParam("projectId") Integer projectId ) {

		try {
			List<IssueTemplate> specialIssuesInaSprint= issueDAO.getSpecialIssuesInSprint(sprintId, projectId);
			return Response.ok(specialIssuesInaSprint).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}
	
	
	
	@POST
	@Path("/updateAllocatedIssues")
	public Response updateAllocatedIssues(SprintIdProjectIdIssuesAllocated issuesAllocated) {
		//List<Issue> updatedRedmineIssues = new ArrayList<Issue>();
		for(AllocatedIssue eachAllocatedIssue : issuesAllocated.getIssuesAllocated() ){
			
	/*		Check whether the issue exists in the specified project*/
				if (issueDAO.ifIssueExistsInProject(issuesAllocated.getProjectId(), eachAllocatedIssue.getIssueId()) == true) {
	/*				Check whether the issue exists in the specified sprint of the project*/
					if(issueDAO.ifIssueExistsInSprint(issuesAllocated.getProjectId(), issuesAllocated.getSprintId(),
							eachAllocatedIssue.getIssueId()) == true){
						RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiAccessKey);
						/*try {
							Issue existingIssueOnRedmine = 	redmineManager.getIssueManager().getIssueById(eachAllocatedIssue.getIssueId());
								if(existingIssueOnRedmine.getAssigneeId() != null ){
								
								System.out.println("the name is "+ existingIssueOnRedmine.getSubject());
							}
						} catch (RedmineException e1) {
							System.out.println(e1);
							return Response.status(Status.BAD_REQUEST).entity(e1).build();
						}*/
						
						Issue redmineIssue = IssueFactory.create(eachAllocatedIssue.getIssueId());
						redmineIssue.setProjectId(issuesAllocated.getProjectId());
						/*Sprint sampleSprint   = new Sprint();
						List<Sprint> existingSprint = issueDAO.getSprintName(issuesAllocated.getProjectId(), issuesAllocated.getSprintId());
						for(Sprint existingSprintDetails : existingSprint){
							//sampleSprint.setSprintId(existingSprintDetails.getSprintId());
							sampleSprint.setSprintName(existingSprintDetails.getSprintName());
						}*/
						//Version redmineSprintDetails = VersionFactory.create(issuesAllocated.getProjectId(), sampleSprint.getSprintName());
						
						//redmineIssue.setTargetVersion(redmineSprintDetails);
						
						redmineIssue.setAssigneeId(eachAllocatedIssue.getPersonId());
						try {
							redmineManager.getIssueManager().update(redmineIssue);
							
							//Integer include = null;
							Issue updatedIssue = redmineManager.getIssueManager().getIssueById(eachAllocatedIssue.getIssueId());
							if( updatedIssue!= null){
								
								IssueUpdateTemplate issue = new IssueUpdateTemplate();
								issue.setProjectId(updatedIssue.getProjectId());
								issue.setIssueId(eachAllocatedIssue.getIssueId());
								issue.setPersonId(updatedIssue.getAssigneeId());
								issue.setSecurityLevel(eachAllocatedIssue.getSecurityLevel());
								issue.setSecurityRiskAnalysis(eachAllocatedIssue.getSecurityRiskAnalysis());
								issue.setIssueLastUpdate(updatedIssue.getUpdatedOn());
								issue.setRedmineLastUpdate(updatedIssue.getUpdatedOn());
								issue.setIssueDone(updatedIssue.getDoneRatio());
								try {
									issueDAO.updateIssuesTable(issue);
								} catch (Exception e) {
									ErrorMessage error = new ErrorMessage();
									error.setError("updated on Redmine. But, failed to update on database");
								System.out.println(e);
								return Response.status(Status.BAD_REQUEST).entity(error).build();
								}
							}
							
						} catch (RedmineException e) {
							System.out.println(e);
							e.printStackTrace();
							return Response.status(Status.BAD_REQUEST).entity(e).build();
						}
						
					}
				}
				
		}
		
		SuccessMessage success = new SuccessMessage();
		success.setSuccess("Successfully added people to issues");
		return Response.ok(success).build();
		}
	
	}
