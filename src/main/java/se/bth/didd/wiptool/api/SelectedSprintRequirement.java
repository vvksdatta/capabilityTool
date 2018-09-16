package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for fetching the requirements associated with a sprint.
 */
public class SelectedSprintRequirement  extends SprintRequirement{

	@JsonProperty
	@NotNull
	@ColumnName("projectId")
	public int projectId;
	
	@JsonProperty
	@NotNull
	@ColumnName("sprintId")
	public int sprintId;
	
	@JsonProperty
	@ColumnName("requirementLevel")
	public String requirementLevel;

	@JsonProperty
	@ColumnName("outcome")
	public String outcome;
	
	@JsonProperty
	@ColumnName("updatedBy")
	public String updatedBy;
	
	@JsonProperty
	@ColumnName("lastUpdate")
	public Date lastUpdate;

	public SelectedSprintRequirement() {

	}

	public SelectedSprintRequirement(int projectId, int sprintId, String requirementLevel, String outcome,
			String updatedBy, Date lastUpdate) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.requirementLevel = requirementLevel;
		this.outcome = outcome;
		this.updatedBy = updatedBy;
		this.lastUpdate = lastUpdate;
	}

	public int getProjectId() {
		return projectId;
	}

	public int getSprintId() {
		return sprintId;
	}

	public String getRequirementLevel() {
		return requirementLevel;
	}

	public String getOutcome() {
		return outcome;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}

	public void setRequirementLevel(String requirementLevel) {
		this.requirementLevel = requirementLevel;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
