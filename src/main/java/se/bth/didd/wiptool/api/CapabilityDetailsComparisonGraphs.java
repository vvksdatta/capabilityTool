package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 * Template for retrieving the last updated proficiency for each capability,
 * together with the first name of user who updated it. This template is used
 * for plotting comparative graphs.
 */

public class CapabilityDetailsComparisonGraphs {
	@JsonProperty
	@NotNull
	@ColumnName("personName")
	public String personName;

	@JsonProperty
	@NotNull
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

	public CapabilityDetailsComparisonGraphs(String personName, String capabilityName, String proficiency,
			String updatedBy, Date lastUpdate) {
		super();
		this.personName = personName;
		this.capabilityName = capabilityName;
		this.proficiency = proficiency;
		this.updatedBy = updatedBy;
		this.lastUpdate = lastUpdate;
	}

	public CapabilityDetailsComparisonGraphs() {

	}

	public String getPersonName() {
		return personName;
	}

	public String getCapabilityName() {
		return capabilityName;
	}

	public String getProficiency() {
		return proficiency;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public void setCapabilityName(String capabilityName) {
		this.capabilityName = capabilityName;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
