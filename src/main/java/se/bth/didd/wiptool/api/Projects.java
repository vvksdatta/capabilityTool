package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template for retrieving or updating the details of a project.
 */

public class Projects {

	@JsonProperty
	@PrimaryKey
	@NotNull
	public Integer projectId;
	
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
	@ColumnName("projectActualEffort")
	public Integer projectActualEffort;

	@JsonProperty
	@ColumnName("projectStatus")
	public String projectStatus;

	@JsonProperty
	@ColumnName("projectLastUpdate")
	public Date projectLastUpdate;

	@JsonProperty
	@ColumnName("redmineLastUpdate")
	public Date redmineLastUpdate;

	@JsonProperty
	@ColumnName("projectUpdatedBy")
	public String projectUpdatedBy;

	@JsonProperty
	@ColumnName("projectLeader")
	public Integer projectLeader;

	@JsonProperty("projectProgress")
	public Integer projectProgress;

	public Projects(Integer projectId, Integer parentProjectId, String projectName, String projectDescription,
			Date projectStartDate, Date projectEndDate, Integer projectEstimatedEffort, Integer projectActualEffort,
			String projectStatus, Date projectLastUpdate, Date redmineLastUpdate, String projectUpdatedBy,
			Integer projectLeader, Integer projectProgress) {
		super();
		this.projectId = projectId;
		this.parentProjectId = parentProjectId;
		this.projectName = projectName;
		this.projectDescription = projectDescription;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.projectEstimatedEffort = projectEstimatedEffort;
		this.projectActualEffort = projectActualEffort;
		this.projectStatus = projectStatus;
		this.projectLastUpdate = projectLastUpdate;
		this.redmineLastUpdate = redmineLastUpdate;
		this.projectUpdatedBy = projectUpdatedBy;
		this.projectLeader = projectLeader;
		this.projectProgress = projectProgress;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public Projects() {

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

	public Integer getProjectActualEffort() {
		return projectActualEffort;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public Date getProjectLastUpdate() {
		return projectLastUpdate;
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

	public void setProjectActualEffort(Integer projectActualEffort) {
		this.projectActualEffort = projectActualEffort;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public void setProjectLastUpdate(Date projectLastUpdate) {
		this.projectLastUpdate = projectLastUpdate;
	}

	public Date getRedmineLastUpdate() {
		return redmineLastUpdate;
	}

	public void setRedmineLastUpdate(Date redmineLastUpdate) {
		this.redmineLastUpdate = redmineLastUpdate;
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

	public Integer getProjectProgress() {
		return projectProgress;
	}

	public void setProjectProgress(Integer projectProgress) {
		this.projectProgress = projectProgress;
	}

	public Integer getParentProjectId() {
		return parentProjectId;
	}

	public void setParentProjectId(Integer parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

}
