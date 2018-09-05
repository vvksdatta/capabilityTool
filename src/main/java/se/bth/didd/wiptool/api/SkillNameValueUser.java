package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SkillNameValueUser {

	@JsonProperty
	@ColumnName("skillName")
	public String skillName;

	@JsonProperty
	@ColumnName("proficiency")
	public String proficiency;

	@JsonProperty
	@ColumnName("updatedBy")
	public String updatedBy;

	public SkillNameValueUser() {

	}

	public SkillNameValueUser(String skillName, String proficiency, String updatedBy) {
		super();
		this.skillName = skillName;
		this.proficiency = proficiency;
		this.updatedBy = updatedBy;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
