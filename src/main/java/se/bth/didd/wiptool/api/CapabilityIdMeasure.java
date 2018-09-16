package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for associating various measures of a capability to its ID.
 */

public class CapabilityIdMeasure {
	@JsonProperty
	@NotNull
	@ColumnName("capabilityId")
	public int capabilityId;
	
	@JsonProperty
	@ColumnName("measure")
	public String measure;
	
	public CapabilityIdMeasure() {
		
	}

	public CapabilityIdMeasure(int capabilityId, String measure) {
		super();
		this.capabilityId = capabilityId;
		this.measure = measure;
	}


	public int getCapabilityId() {
		return capabilityId;
	}


	public String getMeasure() {
		return measure;
	}


	public void setCapabilityId(int capabilityId) {
		this.capabilityId = capabilityId;
	}


	public void setMeasure(String measure) {
		this.measure = measure;
	}
	
}
