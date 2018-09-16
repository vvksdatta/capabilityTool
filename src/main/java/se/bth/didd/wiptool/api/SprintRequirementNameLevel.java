package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for updating and fetching the details of requirements associated with a sprint and the level of requirement.
 */
public class SprintRequirementNameLevel extends SprintRequirement{
	
	@JsonProperty
	@ColumnName("requirementLevel")
	public String requirementLevel;

	@JsonProperty
	@ColumnName("outcome")
	public String outcome;

	public SprintRequirementNameLevel() {

	}

	public SprintRequirementNameLevel(int sprintRequirementId, String sprintRequirementName,
			String sprintRequirementDescription, String requirementLevel, String outcome) {
		super();
		this.sprintRequirementId = sprintRequirementId;
		this.sprintRequirementName = sprintRequirementName;
		this.sprintRequirementDescription = sprintRequirementDescription;
		this.requirementLevel = requirementLevel;
		this.outcome = outcome;
	}

	public int getSprintRequirementId() {
		return sprintRequirementId;
	}

	public String getSprintRequirementName() {
		return sprintRequirementName;
	}

	public String getSprintRequirementDescription() {
		return sprintRequirementDescription;
	}

	public void setSprintRequirementId(int sprintRequirementId) {
		this.sprintRequirementId = sprintRequirementId;
	}

	public void setSprintRequirementName(String sprintRequirementName) {
		this.sprintRequirementName = sprintRequirementName;
	}

	public void setSprintRequirementDescription(String sprintRequirementDescription) {
		this.sprintRequirementDescription = sprintRequirementDescription;
	}

	public String getRequirementLevel() {
		return requirementLevel;
	}

	public void setRequirementLevel(String requirementLevel) {
		this.requirementLevel = requirementLevel;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

}
