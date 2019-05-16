package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 * Template for replying back with the IDs of a sprint and its associated
 * project. This is useful for passing sprint information between different
 * pages related to a sprint.
 */

public class SharedSprint {

	@NotNull
	@JsonProperty
	@ColumnName("associatedProjectId")
	public Integer associatedProjectId;
	
	@NotNull
	@JsonProperty
	@ColumnName("parentProjectId")
	public Integer parentProjectId;

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	public SharedSprint(Integer associatedProjectId, Integer parentProjectId, Integer sprintId) {
		super();
		this.associatedProjectId = associatedProjectId;
		this.parentProjectId = parentProjectId;
		this.sprintId = sprintId;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public SharedSprint() {

	}

	public Integer getAssociatedProjectId() {
		return associatedProjectId;
	}

	public Integer getParentProjectId() {
		return parentProjectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public void setAssociatedProjectId(Integer associatedProjectId) {
		this.associatedProjectId = associatedProjectId;
	}

	public void setParentProjectId(Integer parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}
}
