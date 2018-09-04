package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class SprintRequirement {
	@JsonProperty
	@NotNull
	@ColumnName("sprintRequirementId")
	public int sprintRequirementId;
	
	@JsonProperty
	@ColumnName("sprintRequirementName")
	public String sprintRequirementName;

	@JsonProperty
	@ColumnName("sprintRequirementDescription")
	public String sprintRequirementDescription;

	
	public SprintRequirement() {
		
	}


	public SprintRequirement(int sprintRequirementId, String sprintRequirementName,
			String sprintRequirementDescription) {
		super();
		this.sprintRequirementId = sprintRequirementId;
		this.sprintRequirementName = sprintRequirementName;
		this.sprintRequirementDescription = sprintRequirementDescription;
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

}
