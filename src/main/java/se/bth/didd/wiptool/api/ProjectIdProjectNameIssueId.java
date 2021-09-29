package se.bth.didd.wiptool.api;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectIdProjectNameIssueId {
	
	
	@NotNull
	public Integer projectId;
	
	@NotNull
	@JsonProperty
	public String projectName;

	@JsonProperty
	@NotNull
	public Integer issueId;
	
	@JsonProperty
	@NotNull
	public Integer personId;
	
	@JsonProperty
	@NotNull
	public double estimatedHours;
	
	@JsonProperty
	@NotNull
	public double hoursSpent;
	
	@JsonProperty
	@NotNull
	public Date spentOn;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public double getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public double getHoursSpent() {
		return hoursSpent;
	}

	public void setHoursSpent(double hoursSpent) {
		this.hoursSpent = hoursSpent;
	}

	public Date getSpentOn() {
		return spentOn;
	}

	public void setSpentOn(Date spentOn) {
		this.spentOn = spentOn;
	}

	public ProjectIdProjectNameIssueId(Integer projectId, String projectName, Integer issueId, Integer personId,
			double estimatedHours, double hoursSpent, Date spentOn) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.issueId = issueId;
		this.personId = personId;
		this.estimatedHours = estimatedHours;
		this.hoursSpent = hoursSpent;
		this.spentOn = spentOn;
	}

	public ProjectIdProjectNameIssueId(Integer projectId, String projectName, Integer issueId, double hoursSpent,
			Date spentOn) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.issueId = issueId;
		this.hoursSpent = hoursSpent;
		this.spentOn = spentOn;
	}
	
	
}
