package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class Skill {
	@JsonProperty
	@NotNull
	@ColumnName("skillId")
	public int skillId;
	
	@JsonProperty
	@ColumnName("skillName")
	public String skillName;

	
	public Skill() {
		
	}


	public Skill(int skillId, String skillName) {
		super();
		this.skillId = skillId;
		this.skillName = skillName;
	}


	public int getSkillId() {
		return skillId;
	}


	public String getSkillName() {
		return skillName;
	}


	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}


	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	
}
