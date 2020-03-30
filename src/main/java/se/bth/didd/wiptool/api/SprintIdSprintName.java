package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 * Template for replying back with the IDs of a sprint and its associated
 * project. This is useful for passing sprint information between different
 * pages related to a sprint.
 */

public class SprintIdSprintName {

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;
	
	@JsonProperty
	@ColumnName("sprintName")
	public String sprintName;

	

	public SprintIdSprintName(Integer sprintId, String sprintName) {
		super();
		this.sprintId = sprintId;
		this.sprintName = sprintName;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public SprintIdSprintName() {

	}

	public Integer getSprintId() {
		return sprintId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

}
