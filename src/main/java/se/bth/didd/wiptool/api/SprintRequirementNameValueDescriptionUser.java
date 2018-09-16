package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for updating the requirements selected by a user for a specific sprint 
 */

public class SprintRequirementNameValueDescriptionUser {

	@JsonProperty
	@NotNull
	@ColumnName("projectId")
	public int projectId;

	@JsonProperty
	@NotNull
	@ColumnName("sprintId")
	public int sprintId;

	@JsonProperty
	@ColumnName("sprintRequirementName")
	public String sprintRequirementName;

	@JsonProperty
	@ColumnName("requirementLevel")
	public String requirementLevel;

	@JsonProperty
	@ColumnName("sprintRequirementDescription")
	public String sprintRequirementDescription;

	@JsonProperty
	@ColumnName("updatedBy")
	public String updatedBy;

	public SprintRequirementNameValueDescriptionUser() {

	}

	public SprintRequirementNameValueDescriptionUser(int projectId, int sprintId, String sprintRequirementName,
			String requirementLevel, String sprintRequirementDescription, String updatedBy) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintRequirementName = sprintRequirementName;
		this.requirementLevel = requirementLevel;
		this.sprintRequirementDescription = sprintRequirementDescription;
		this.updatedBy = updatedBy;
	}

	public String getSprintRequirementName() {
		return sprintRequirementName;
	}

	public String getRequirementLevel() {
		return requirementLevel;
	}

	public String getSprintRequirementDescription() {
		return sprintRequirementDescription;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setSprintRequirementName(String sprintRequirementName) {
		this.sprintRequirementName = sprintRequirementName;
	}

	public void setRequirementLevel(String requirementLevel) {
		this.requirementLevel = requirementLevel;
	}

	public void setSprintRequirementDescription(String sprintRequirementDescription) {
		this.sprintRequirementDescription = sprintRequirementDescription;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getProjectId() {
		return projectId;
	}

	public int getSprintId() {
		return sprintId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}

}
