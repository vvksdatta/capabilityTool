package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectIdProjectNameIssueId {
	
	
	@NotNull
	public int projectId;
	
	@NotNull
	@JsonProperty
	public String projectName;

	@JsonProperty
	@NotNull
	public int issueId;

	public ProjectIdProjectNameIssueId(int projectId, String projectName, int issueId) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.issueId = issueId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
	
}
