package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for fetching proficiency in a capability.
 */
public class ProjectIdCapabilityIdProficiency {
	@JsonProperty
	@NotNull
	@ColumnName("capabilityId")
	public int capabilityId;
	
	@JsonProperty
	@NotNull
	@ColumnName("projectId")
	public int projectId;
	
	@JsonProperty
	@ColumnName("proficiency")
	public String proficiency;

	
	public ProjectIdCapabilityIdProficiency() {
		
	}


	public ProjectIdCapabilityIdProficiency(int projectId, int capabilityId, String proficiency) {
		super();
		this.projectId = projectId;
		this.capabilityId = capabilityId;
		this.proficiency = proficiency;
	}


	public int getCapabilityId() {
		return capabilityId;
	}


	public String getProficiency() {
		return proficiency;
	}


	public void setCapabilityId(int capabilityId) {
		this.capabilityId = capabilityId;
	}


	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}


	public int getProjectId() {
		return projectId;
	}


	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
}
