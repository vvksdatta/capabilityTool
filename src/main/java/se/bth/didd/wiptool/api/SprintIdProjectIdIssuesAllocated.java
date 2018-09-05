package se.bth.didd.wiptool.api;

import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SprintIdProjectIdIssuesAllocated {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;
	
	@JsonProperty
	public List<AllocatedIssue> issuesAllocated;
	

	public SprintIdProjectIdIssuesAllocated(Integer projectId, Integer sprintId, List<AllocatedIssue> issuesAllocated) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.issuesAllocated = issuesAllocated;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public SprintIdProjectIdIssuesAllocated() {

	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public List<AllocatedIssue> getIssuesAllocated() {
		return issuesAllocated;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setIssuesAllocated(List<AllocatedIssue> issuesAllocated) {
		this.issuesAllocated = issuesAllocated;
	}

}
