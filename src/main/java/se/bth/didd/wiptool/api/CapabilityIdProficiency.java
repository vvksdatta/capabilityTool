package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for fetching proficiency in a capability.
 */
public class CapabilityIdProficiency {
	@JsonProperty
	@NotNull
	@ColumnName("capabilityId")
	public int capabilityId;
	
	@JsonProperty
	@ColumnName("proficiency")
	public String proficiency;

	
	public CapabilityIdProficiency() {
		
	}


	public CapabilityIdProficiency(int capabilityId, String proficiency) {
		super();
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
}
