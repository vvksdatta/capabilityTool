package se.bth.didd.wiptool.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.taskadapter.redmineapi.bean.IssueFactory;

import io.dropwizard.auth.Auth;
import se.bth.didd.wiptool.api.AllocatedIssue;
import se.bth.didd.wiptool.api.ErrorMessage;
import se.bth.didd.wiptool.api.IssueTemplate;
import se.bth.didd.wiptool.api.IssueUpdateTemplate;
import se.bth.didd.wiptool.api.SprintIdProjectIdIssuesAllocated;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.db.IssuesDAO;

@Path("/issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueResource {

	private String redmineUrl;
	private IssuesDAO issueDAO;

	public IssueResource(IssuesDAO issueDAO, String redmineUrl) {
		this.redmineUrl = redmineUrl;
		this.issueDAO = issueDAO;
	}

	@GET
	@Path("/getSpecialIssuesInSprint/{sprintId}/{projectId}")
	public Response getSpecialIssuesInSprint(@PathParam("sprintId") Integer sprintId,
			@PathParam("projectId") Integer projectId) {

		try {
			List<IssueTemplate> specialIssuesInaSprint = issueDAO.getSpecialIssuesInSprint(sprintId, projectId);
			return Response.ok(specialIssuesInaSprint).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@POST
	@Path("/updateAllocatedIssues")
	public Response updateAllocatedIssues(@Auth User user, SprintIdProjectIdIssuesAllocated issuesAllocated) {
		String apiKey;
		try {
			 apiKey = issueDAO.getApiKeyOfUser(issuesAllocated.getUserId()).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		for (AllocatedIssue eachAllocatedIssue : issuesAllocated.getIssuesAllocated()) {

			/* Check whether the issue exists in the specified project */
			if (issueDAO.ifIssueExistsInProject(issuesAllocated.getProjectId(),
					eachAllocatedIssue.getIssueId()) == true) {
				/*
				 * Check whether the issue exists in the specified sprint of the
				 * project
				 */
				if (issueDAO.ifIssueExistsInSprint(issuesAllocated.getProjectId(), issuesAllocated.getSprintId(),
						eachAllocatedIssue.getIssueId()) == true) {
					RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
					/*
					 * try { Issue existingIssueOnRedmine =
					 * redmineManager.getIssueManager().getIssueById(
					 * eachAllocatedIssue.getIssueId());
					 * if(existingIssueOnRedmine.getAssigneeId() != null ){
					 * 
					 * System.out.println("the name is "+
					 * existingIssueOnRedmine.getSubject()); } } catch
					 * (RedmineException e1) { System.out.println(e1); return
					 * Response.status(Status.BAD_REQUEST).entity(e1).build(); }
					 */

					Issue redmineIssue = IssueFactory.create(eachAllocatedIssue.getIssueId());
					redmineIssue.setProjectId(issuesAllocated.getProjectId());
					/*
					 * Sprint sampleSprint = new Sprint(); List<Sprint>
					 * existingSprint =
					 * issueDAO.getSprintName(issuesAllocated.getProjectId(),
					 * issuesAllocated.getSprintId()); for(Sprint
					 * existingSprintDetails : existingSprint){
					 * //sampleSprint.setSprintId(existingSprintDetails.
					 * getSprintId());
					 * sampleSprint.setSprintName(existingSprintDetails.
					 * getSprintName()); }
					 */
					// Version redmineSprintDetails =
					// VersionFactory.create(issuesAllocated.getProjectId(),
					// sampleSprint.getSprintName());

					// redmineIssue.setTargetVersion(redmineSprintDetails);

					redmineIssue.setAssigneeId(eachAllocatedIssue.getPersonId());
					try {
						redmineManager.getIssueManager().update(redmineIssue);

						// Integer include = null;
						Issue updatedIssue = redmineManager.getIssueManager()
								.getIssueById(eachAllocatedIssue.getIssueId());
						if (updatedIssue != null) {

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
