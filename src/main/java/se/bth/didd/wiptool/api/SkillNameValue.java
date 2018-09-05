package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class SkillNameValue {
	
	
	@JsonProperty
	@ColumnName("skillName")
	public String skillName;

	@JsonProperty
	@ColumnName("proficiency")
	public String proficiency;

	
	public SkillNameValue() {
		
	}

	public SkillNameValue(String skillName, String proficiency) {
		super();
		this.skillName = skillName;
		this.proficiency = proficiency;
	}

	public String getSkillName() {
		return skillName;
	}

	public String getProficiency() {
		return proficiency;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}
	
}
