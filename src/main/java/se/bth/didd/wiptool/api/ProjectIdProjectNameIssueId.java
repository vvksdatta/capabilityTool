package se.bth.didd.wiptool.api;

import java.util.Date;

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
	
	@JsonProperty
	@NotNull
	public double hours;
	
	@JsonProperty
	@NotNull
	public Date spentOn;
	
	public ProjectIdProjectNameIssueId(int projectId, String projectName, int issueId, double hours, Date spentOn) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.issueId = issueId;
		this.hours = hours;
		this.spentOn = spentOn;
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

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public Date getSpentOn() {
		return spentOn;
	}

	public void setSpentOn(Date spentOn) {
		this.spentOn = spentOn;
	}
	
	
}
