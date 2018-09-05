package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;


public class Capability {
	@JsonProperty
	@NotNull
	@ColumnName("capabilityId")
	public int capabilityId;
	
	@JsonProperty
	@ColumnName("capabilityName")
	public String capabilityName;

	
	public Capability() {
		
	}
	
	public Capability(int capabilityId, String capabilityName) {
		super();
		this.capabilityId = capabilityId;
		this.capabilityName = capabilityName;
	}

	public int getCapabilityId() {
		return capabilityId;
	}

	public String getCapabilityName() {
		return capabilityName;
	}

	public void setCapabilityId(int capabilityId) {
		this.capabilityId = capabilityId;
	}

	public void setCapabilityName(String capabilityName) {
		this.capabilityName = capabilityName;
	}
}
