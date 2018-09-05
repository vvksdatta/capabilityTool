package se.bth.didd.wiptool.api;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;

/*Sprint entity which  extends the SprintsSummary entity.*/

public class ExistingSprint {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;
	
	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	@JsonProperty
	@ColumnName("sprintName")
	public String sprintName;

	
	@JsonProperty
	@ColumnName("sprintDescription")
	public String sprintDescription;
	
	@JsonProperty
	@ColumnName("sprintStatus")
	public String sprintStatus;

	@JsonProperty
	@ColumnName("sprintPhase")
	public String sprintPhase;

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
	@ColumnName("sprintActualEffort")
	public Integer sprintActualEffort;

	@JsonProperty
	@ColumnName("sprintUpdatedBy")
	public String sprintUpdatedBy;

	@JsonProperty
	@OneToMany("applicationDomain")
	public List<SprintDomain> applicationDomain;

	@JsonProperty
	@OneToMany("kindOfAssets")
	public List<SprintAsset> kindOfAssets;

	@JsonProperty
	@ColumnName("expectedTeamKnowledgeDiversity")
	public String expectedTeamKnowledgeDiversity;

	@JsonProperty
	@ColumnName("expectedCoachingPotential")
	public String expectedCoachingPotential;
	



	public ExistingSprint(Integer projectId, Integer sprintId, String sprintName, String sprintDescription,
			String sprintStatus, String sprintPhase, Date sprintStartDate, Date sprintEndDate,
			Integer sprintEstimatedEffort, Integer sprintActualEffort, String sprintUpdatedBy,
			List<SprintDomain> applicationDomain, List<SprintAsset> kindOfAssets, String expectedTeamKnowledgeDiversity,
			String expectedCoachingPotential) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.sprintDescription = sprintDescription;
		this.sprintStatus = sprintStatus;
		this.sprintPhase = sprintPhase;
		this.sprintStartDate = sprintStartDate;
		this.sprintEndDate = sprintEndDate;
		this.sprintEstimatedEffort = sprintEstimatedEffort;
		this.sprintActualEffort = sprintActualEffort;
		this.sprintUpdatedBy = sprintUpdatedBy;
		this.applicationDomain = applicationDomain;
		this.kindOfAssets = kindOfAssets;
		this.expectedTeamKnowledgeDiversity = expectedTeamKnowledgeDiversity;
		this.expectedCoachingPotential = expectedCoachingPotential;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ExistingSprint() {

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

	public String getSprintStatus() {
		return sprintStatus;
	}

	public void setSprintStatus(String sprintStatus) {
		this.sprintStatus = sprintStatus;
	}

	public Integer getSprintActualEffort() {
		return sprintActualEffort;
	}

	public void setSprintActualEffort(Integer sprintActualEffort) {
		this.sprintActualEffort = sprintActualEffort;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public String getSprintPhase() {
		return sprintPhase;
	}

	public void setSprintPhase(String sprintPhase) {
		this.sprintPhase = sprintPhase;
	}

	public String getExpectedTeamKnowledgeDiversity() {
		return expectedTeamKnowledgeDiversity;
	}

	public String getExpectedCoachingPotential() {
		return expectedCoachingPotential;
	}

	public void setExpectedTeamKnowledgeDiversity(String expectedTeamKnowledgeDiversity) {
		this.expectedTeamKnowledgeDiversity = expectedTeamKnowledgeDiversity;
	}

	public void setExpectedCoachingPotential(String expectedCoachingPotential) {
		this.expectedCoachingPotential = expectedCoachingPotential;
	}

	
	
}
