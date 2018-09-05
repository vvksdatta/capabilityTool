package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SprintRequirementsTabsTemplate {
	@JsonProperty
	@NotNull
	@ColumnName("value")
	public String value;

	@JsonProperty
	@ColumnName("sprintRequirementName")
	public String display;

	@JsonProperty
	@ColumnName("sprintRequirementDescription")
	public String existingDescription;

	public SprintRequirementsTabsTemplate() {

	}

	public SprintRequirementsTabsTemplate(String value, String display, String existingDescription) {
		super();
		this.value = value;
		this.display = display;
		this.existingDescription = existingDescription;
	}

	public String getValue() {
		return value;
	}

	public String getDisplay() {
		return display;
	}

	public String getExistingDescription() {
		return existingDescription;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public void setExistingDescription(String existingDescription) {
		this.existingDescription = existingDescription;
	}

}
