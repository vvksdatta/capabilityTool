package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template for updating the details of a new project.
 */

public class NewProject {

	@JsonProperty
	@PrimaryKey
	@NotNull
	public Integer projectId;
	/*
	 * @ColoumnName("name") is an annotation that belongs to jdbi-folder. This
	 * is essential for fetching values from the 'name' column of database
	 * table.
	 */
	@JsonProperty
	@ColumnName("parentProjectId")
	public Integer parentProjectId;

	@JsonProperty
	@ColumnName("projectName")
	public String projectName;

	@JsonProperty
	@ColumnName("projectDescription")
	public String projectDescription;

	@JsonProperty
	@ColumnName("projectStartDate")
	public Date projectStartDate;

	@JsonProperty
	@ColumnName("projectEndDate")
	public Date projectEndDate;

	@JsonProperty
	@ColumnName("projectEstimatedEffort")
	public Integer projectEstimatedEffort;

	@JsonProperty
	@ColumnName("projectUpdatedBy")
	public String projectUpdatedBy;

	@JsonProperty
	@ColumnName("projectLeader")
	public Integer projectLeader;

	public NewProject(Integer projectId, Integer parentProjectId, String projectName, String projectDescription,
			Date projectStartDate, Date projectEndDate, Integer projectEstimatedEffort, String projectUpdatedBy,
			Integer projectLeader) {
		super();
		this.projectId = projectId;
		this.parentProjectId = parentProjectId;
		this.projectName = projectName;
		this.projectDescription = projectDescription;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.projectEstimatedEffort = projectEstimatedEffort;
		this.projectUpdatedBy = projectUpdatedBy;
		this.projectLeader = projectLeader;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public NewProject() {

	}

	public Integer getProjectId() {
		return projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public Date getProjectStartDate() {
		return projectStartDate;
	}

	public Date getProjectEndDate() {
		return projectEndDate;
	}

	public Integer getProjectEstimatedEffort() {
		return projectEstimatedEffort;
	}

	public String getProjectUpdatedBy() {
		return projectUpdatedBy;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public void setProjectStartDate(Date projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public void setProjectEstimatedEffort(Integer projectEstimatedEffort) {
		this.projectEstimatedEffort = projectEstimatedEffort;
	}

	public void setProjectUpdatedBy(String projectUpdatedBy) {
		this.projectUpdatedBy = projectUpdatedBy;
	}

	public Integer getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(Integer projectLeader) {
		this.projectLeader = projectLeader;
	}

	public Integer getParentProjectId() {
		return parentProjectId;
	}

	public void setParentProjectId(Integer parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

}
