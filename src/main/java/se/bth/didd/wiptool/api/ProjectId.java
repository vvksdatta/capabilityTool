package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 * Template useful retrieving projectId.
 */

public class ProjectId {

	@JsonProperty
	@PrimaryKey
	@ColumnName("projectId")
	public Integer projectId;

	public ProjectId(Integer projectId) {
		super();
		this.projectId = projectId;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ProjectId() {
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

}
