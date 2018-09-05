package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class Sprint extends SprintNameProgress {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

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

	@JsonProperty
	@ColumnName("sprintActualEffort")
	public Integer sprintActualEffort;

	@JsonProperty
	@ColumnName("sprintStatus")
	public String sprintStatus;

	@JsonProperty
	@ColumnName("sprintLastUpdate")
	public Date sprintLastUpdate;

	@JsonProperty
	@ColumnName("sprintRedmineUpdate")
	public Date sprintRedmineUpdate;

	@JsonProperty
	@ColumnName("sprintUpdatedBy")
	public String sprintUpdatedBy;

	@JsonProperty
	@ColumnName("sprintPhase")
	public String sprintPhase;

	@JsonProperty
	@ColumnName("expectedTeamKnowledgeDiversity")
	public String expectedTeamKnowledgeDiversity;

	@JsonProperty
	@ColumnName("expectedCoachingPotential")
	public String expectedCoachingPotential;

	
	
	public Sprint(Integer sprintId, String sprintName, Integer sprintProgress, Integer projectId,
			String sprintDescription, Date sprintStartDate, Date sprintEndDate, Integer sprintEstimatedEffort,
			Integer sprintActualEffort, String sprintStatus, Date sprintLastUpdate, Date sprintRedmineUpdate,
			String sprintUpdatedBy, String sprintPhase, String expectedTeamKnowledgeDiversity,
			String expectedCoachingPotential) {
		super(sprintId, sprintName, sprintProgress);
		this.projectId = projectId;
		this.sprintDescription = sprintDescription;
		this.sprintStartDate = sprintStartDate;
		this.sprintEndDate = sprintEndDate;
		this.sprintEstimatedEffort = sprintEstimatedEffort;
		this.sprintActualEffort = sprintActualEffort;
		this.sprintStatus = sprintStatus;
		this.sprintLastUpdate = sprintLastUpdate;
		this.sprintRedmineUpdate = sprintRedmineUpdate;
		this.sprintUpdatedBy = sprintUpdatedBy;
		this.sprintPhase = sprintPhase;
		this.expectedTeamKnowledgeDiversity = expectedTeamKnowledgeDiversity;
		this.expectedCoachingPotential = expectedCoachingPotential;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public Sprint() {

	}

	public Integer getProjectId() {
		return projectId;
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

	public Integer getSprintActualEffort() {
		return sprintActualEffort;
	}

	public String getSprintStatus() {
		return sprintStatus;
	}

	public Date getSprintLastUpdate() {
		return sprintLastUpdate;
	}

	public String getSprintUpdatedBy() {
		return sprintUpdatedBy;
	}


	public String getExpectedTeamKnowledgeDiversity() {
		return expectedTeamKnowledgeDiversity;
	}

	public String getExpectedCoachingPotential() {
		return expectedCoachingPotential;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
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

	public void setSprintActualEffort(Integer sprintActualEffort) {
		this.sprintActualEffort = sprintActualEffort;
	}

	public void setSprintStatus(String sprintStatus) {
		this.sprintStatus = sprintStatus;
	}

	public void setSprintLastUpdate(Date sprintLastUpdate) {
		this.sprintLastUpdate = sprintLastUpdate;
	}
	
	public Date getSprintRedmineUpdate() {
		return sprintRedmineUpdate;
	}

	public void setSprintRedmineUpdate(Date sprintRedmineUpdate) {
		this.sprintRedmineUpdate = sprintRedmineUpdate;
	}

	public void setSprintUpdatedBy(String sprintUpdatedBy) {
		this.sprintUpdatedBy = sprintUpdatedBy;
	}


	public void setExpectedTeamKnowledgeDiversity(String expectedTeamKnowledgeDiversity) {
		this.expectedTeamKnowledgeDiversity = expectedTeamKnowledgeDiversity;
	}

	public void setExpectedCoachingPotential(String expectedCoachingPotential) {
		this.expectedCoachingPotential = expectedCoachingPotential;
	}

	public String getSprintPhase() {
		return sprintPhase;
	}

	public void setSprintPhase(String sprintPhase) {
		this.sprintPhase = sprintPhase;
	}
	
}
