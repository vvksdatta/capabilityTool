package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class ProgrammingSkillsDetailsComparisonGraphs {
	@JsonProperty
	@NotNull
	@ColumnName("personName")
	public String personName;

	@JsonProperty
	@NotNull
	@ColumnName("skillName")
	public String skillName;
	
	@JsonProperty
	@ColumnName("proficiency")
	public String proficiency;
	
	@JsonProperty
	@ColumnName("updatedBy")
	public String updatedBy;
	
	@JsonProperty
	@ColumnName("lastUpdate")
	public Date lastUpdate;

	
	
	public ProgrammingSkillsDetailsComparisonGraphs(String personName, String skillName, String proficiency,
			String updatedBy, Date lastUpdate) {
		super();
		this.personName = personName;
		this.skillName = skillName;
		this.proficiency = proficiency;
		this.updatedBy = updatedBy;
		this.lastUpdate = lastUpdate;
	}


	public ProgrammingSkillsDetailsComparisonGraphs(){
		
	}


	public String getPersonName() {
		return personName;
	}


	public String getSkillName() {
		return skillName;
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


	public void setSkillName(String skillName) {
		this.skillName = skillName;
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
