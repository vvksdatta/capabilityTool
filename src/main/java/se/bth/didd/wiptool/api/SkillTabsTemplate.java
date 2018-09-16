package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for fetching the details of skills in order to comply with the tabular display of skills on UI.
 */

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
