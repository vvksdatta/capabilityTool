package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template for saving the details of options.
 */

public class OptionsTemplate {

	@JsonProperty
	@PrimaryKey
	@ColumnName("optionId")
	public Integer optionId;

	@JsonProperty
	@ColumnName("addNewProject")
	public Integer addNewProject;
	
	@JsonProperty
	@ColumnName("addNewPerson")
	public Integer addNewPerson;
	
	@JsonProperty
	@ColumnName("addNewSprint")
	public Integer addNewSprint;

	public OptionsTemplate() {

	}

	public Integer getOptionId() {
		return optionId;
	}

	public Integer getAddNewProject() {
		return addNewProject;
	}

	public Integer getAddNewPerson() {
		return addNewPerson;
	}

	public Integer getAddNewSprint() {
		return addNewSprint;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	public void setAddNewProject(Integer addNewProject) {
		this.addNewProject = addNewProject;
	}

	public void setAddNewPerson(Integer addNewPerson) {
		this.addNewPerson = addNewPerson;
	}

	public void setAddNewSprint(Integer addNewSprint) {
		this.addNewSprint = addNewSprint;
	}

}
