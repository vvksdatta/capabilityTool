package se.bth.didd.wiptool.api;

import java.util.Date;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 * Template for retrieving capabilities associated with a project.
 */

public class CapabilitiesOfProject {
	@JsonProperty
	@NotNull
	@ColumnName("projectId")
	public Integer projectId;

	@JsonProperty
	@NotNull
	@ColumnName("personId")
	public Integer personId;

	@JsonProperty
	@ColumnName("capabilityName")
	public String capabilityName;

	@JsonProperty
	@ColumnName("proficiency")
	public String proficiency;

	@JsonProperty
	@ColumnName("updatedBy")
	public String updatedBy;

	@JsonProperty
	@ColumnName("lastUpdate")
	public Date lastUpdate;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getCapabilityName() {
		return capabilityName;
	}

	public void setCapabilityName(String capabilityName) {
		this.capabilityName = capabilityName;
	}

	public String getProficiency() {
		return proficiency;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public CapabilitiesOfProject(Integer projectId, Integer personId, String capabilityName, String proficiency,
			String updatedBy, Date lastUpdate) {
		super();
		this.projectId = projectId;
		this.personId = personId;
		this.capabilityName = capabilityName;
		this.proficiency = proficiency;
		this.updatedBy = updatedBy;
		this.lastUpdate = lastUpdate;
	}

	public CapabilitiesOfProject() {
		super();
	}

}
