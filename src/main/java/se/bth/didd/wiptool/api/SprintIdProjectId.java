package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SprintIdProjectId {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	public SprintIdProjectId(Integer projectId, Integer sprintId) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public SprintIdProjectId() {

	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

}
