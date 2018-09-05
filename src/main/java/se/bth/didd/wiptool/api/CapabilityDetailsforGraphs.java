package se.bth.didd.wiptool.api;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class CapabilityDetailsforGraphs {
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
	

	

	public CapabilityDetailsforGraphs(String capabilityName, String proficiency, String updatedBy, Date lastUpdate) {
		super();
		this.capabilityName = capabilityName;
		this.proficiency = proficiency;
		this.updatedBy = updatedBy;
		this.lastUpdate = lastUpdate;
	}

	public CapabilityDetailsforGraphs(){
		
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	
	
}
