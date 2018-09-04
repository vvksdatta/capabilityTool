package se.bth.didd.wiptool.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class CapabilityNameMeasures {
	@JsonProperty
	@NotNull
	@ColumnName("capabilityName")
	public String capabilityName;
	
	@JsonProperty
	@ColumnName("capabilityMeasures")
	public List<CapabilityMeasuresDisplay> capabilityMeasures;

	
	
	public CapabilityNameMeasures(String capabilityName, List<CapabilityMeasuresDisplay> capabilityMeasures) {
		this.capabilityName = capabilityName;
		this.capabilityMeasures = capabilityMeasures;
	}
	
	public CapabilityNameMeasures(){
		
	}

	public String getCapabilityName() {
		return capabilityName;
	}

	public List<CapabilityMeasuresDisplay> getCapabilityMeasures() {
		return capabilityMeasures;
	}

	public void setCapabilityName(String capabilityName) {
		this.capabilityName = capabilityName;
	}

	public void setCapabilityMeasures(List<CapabilityMeasuresDisplay> capabilityMeasures) {
		this.capabilityMeasures = capabilityMeasures;
	}

	

	
	
}
