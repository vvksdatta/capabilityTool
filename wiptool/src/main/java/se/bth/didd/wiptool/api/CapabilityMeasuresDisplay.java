package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class CapabilityMeasuresDisplay {
	@JsonProperty
	@NotNull
	@ColumnName("value")
	public String value;
	
	@JsonProperty
	@ColumnName("legend")
	public String legend;

	public CapabilityMeasuresDisplay(String value, String legend) {
		this.value = value;
		this.legend = legend;
	}

	public CapabilityMeasuresDisplay(){
		
	}

	public String getValue() {
		return value;
	}

	public String getLegend() {
		return legend;
	}


	public void setValue(String value) {
		this.value = value;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	
}
