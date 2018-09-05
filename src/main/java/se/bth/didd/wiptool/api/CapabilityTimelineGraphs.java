package se.bth.didd.wiptool.api;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class CapabilityTimelineGraphs {
	
	@JsonProperty
	@ColumnName("proficiency")
	public String proficiency;
	
	@JsonProperty
	@ColumnName("lastUpdate")
	public Date lastUpdate;
	
	

	public CapabilityTimelineGraphs(String proficiency, Date lastUpdate) {
		super();
		this.proficiency = proficiency;
		this.lastUpdate = lastUpdate;
	}


	public CapabilityTimelineGraphs(){
		
	}


	public String getProficiency() {
		return proficiency;
	}


	public Date getLastUpdate() {
		return lastUpdate;
	}


	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}


	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
