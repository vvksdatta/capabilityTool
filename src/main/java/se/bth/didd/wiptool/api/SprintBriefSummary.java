package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 * Template for presenting a brief summary of sprint, with details such as name,
 * associated project name, etc.
 */

public class SprintBriefSummary {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	@JsonProperty
	@ColumnName("sprintName")
	public String sprintName;

	@JsonProperty
	@ColumnName("projectName")
	public String projectName;

	@JsonProperty
	@ColumnName("sprintDescription")
	public String sprintDescription;

	@JsonProperty
	@ColumnName("sprintStartDate")
	public Date sprintStartDate;

	@JsonProperty
	@ColumnName("sprintEndDate")
	public Date sprintEndDate;

	@JsonProperty
	@ColumnName("sprintEstimatedEffort")
	public Integer sprintEstimatedEffort;

	public SprintBriefSummary(Integer projectId, Integer sprintId, String sprintName, String projectName,
			String sprintDescription, Date sprintStartDate, Date sprintEndDate, Integer sprintEstimatedEffort) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.projectName = projectName;
		this.sprintDescription = sprintDescription;
		this.sprintStartDate = sprintStartDate;
		this.sprintEndDate = sprintEndDate;
		this.sprintEstimatedEffort = sprintEstimatedEffort;
	}

	public SprintBriefSummary() {

	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public String getSprintDescription() {
		return sprintDescription;
	}

	public Date getSprintStartDate() {
		return sprintStartDate;
	}

	public Date getSprintEndDate() {
		return sprintEndDate;
	}

	public Integer getSprintEstimatedEffort() {
		return sprintEstimatedEffort;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public void setSprintDescription(String sprintDescription) {
		this.sprintDescription = sprintDescription;
	}

	public void setSprintStartDate(Date sprintStartDate) {
		this.sprintStartDate = sprintStartDate;
	}

	public void setSprintEndDate(Date sprintEndDate) {
		this.sprintEndDate = sprintEndDate;
	}

	public void setSprintEstimatedEffort(Integer sprintEstimatedEffort) {
		this.sprintEstimatedEffort = sprintEstimatedEffort;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
