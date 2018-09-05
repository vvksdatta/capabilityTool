package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SkillTabsTemplate {
	@JsonProperty
	@NotNull
	@ColumnName("value")
	public String value;

	@JsonProperty
	@ColumnName("skillName")
	public String display;

	public SkillTabsTemplate() {

	}

	public SkillTabsTemplate(String value, String display) {
		super();
		this.value = value;
		this.display = display;
	}

	public String getValue() {
		return value;
	}

	public String getDisplay() {
		return display;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

}
