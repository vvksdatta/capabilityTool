package se.bth.didd.wiptool.api;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;

/*Sprint entity which  extends the SprintsSummary entity.*/

public class NewSprint {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@JsonProperty
	@ColumnName("sprintName")
	public String sprintName;

	
	@JsonProperty
	@ColumnName("sprintDescription")
	public String sprintDescription;

	@JsonProperty
	@ColumnName("sprintStartDate")
	public Date sprintStartDate;

	@JsonProperty
	@ColumnName("sprintEndDate")
	public Date sprintEndDate;

	@JsonProperty
	@ColumnName("sprintEstimatedEffort")
	public Integer sprintEstimatedEffort;

	@JsonProperty
	@ColumnName("sprintUpdatedBy")
	public String sprintUpdatedBy;

	@JsonProperty
	@OneToMany("applicationDomain")
	public List<SprintDomain> applicationDomain;

	@JsonProperty
	@OneToMany("kindOfAssets")
	public List<SprintAsset> kindOfAssets;

	


	public NewSprint(Integer projectId, String sprintName, String sprintDescription, Date sprintStartDate,
			Date sprintEndDate, Integer sprintEstimatedEffort, 
			 String sprintUpdatedBy, List<SprintDomain> applicationDomain,
			List<SprintAsset> kindOfAssets) {
		this.projectId = projectId;
		this.sprintName = sprintName;
		this.sprintDescription = sprintDescription;
		this.sprintStartDate = sprintStartDate;
		this.sprintEndDate = sprintEndDate;
		this.sprintEstimatedEffort = sprintEstimatedEffort;
		this.sprintUpdatedBy = sprintUpdatedBy;
		this.applicationDomain = applicationDomain;
		this.kindOfAssets = kindOfAssets;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public NewSprint() {

	}

	public Integer getProjectId() {
		return projectId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public String getSprintDescription() {
		return sprintDescription;
	}

	public Date getSprintStartDate() {
		return sprintStartDate;
	}

	public Date getSprintEndDate() {
		return sprintEndDate;
	}

	public Integer getSprintEstimatedEffort() {
		return sprintEstimatedEffort;
	}

		public String getSprintUpdatedBy() {
		return sprintUpdatedBy;
	}

	public List<SprintDomain> getApplicationDomain() {
		return applicationDomain;
	}

	public List<SprintAsset> getKindOfAssets() {
		return kindOfAssets;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public void setSprintDescription(String sprintDescription) {
		this.sprintDescription = sprintDescription;
	}

	public void setSprintStartDate(Date sprintStartDate) {
		this.sprintStartDate = sprintStartDate;
	}

	public void setSprintEndDate(Date sprintEndDate) {
		this.sprintEndDate = sprintEndDate;
	}

	public void setSprintEstimatedEffort(Integer sprintEstimatedEffort) {
		this.sprintEstimatedEffort = sprintEstimatedEffort;
	}

	public void setSprintUpdatedBy(String sprintUpdatedBy) {
		this.sprintUpdatedBy = sprintUpdatedBy;
	}

	public void setApplicationDomain(List<SprintDomain> applicationDomain) {
		this.applicationDomain = applicationDomain;
	}

	public void setKindOfAssets(List<SprintAsset> kindOfAssets) {
		this.kindOfAssets = kindOfAssets;
	}

}
