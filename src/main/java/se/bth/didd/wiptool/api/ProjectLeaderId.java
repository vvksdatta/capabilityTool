package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template useful retrieving list of people.
 */

public class ProjectLeaderId {
	
	@JsonProperty
	@PrimaryKey
	@ColumnName("projectLeader")
	public Integer projectLeader;
	
	public ProjectLeaderId(Integer projectLeader) {
		super();
		this.projectLeader = projectLeader;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ProjectLeaderId() {
	}

	public Integer getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(Integer projectLeader) {
		this.projectLeader = projectLeader;
	}

}
