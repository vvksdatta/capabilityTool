package se.bth.didd.wiptool.api;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;

public class SprintCompanyDrivenFactors {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	@JsonProperty
	@ColumnName("coachingPotential")
	public String coachingPotential;

	@JsonProperty
	@ColumnName("teamKnowledgeDiversity")
	public String teamKnowledgeDiversity;

	@JsonProperty
	@OneToMany("developmentEnvironments")
	public List<SprintDevelopmentEnvironment> developmentEnvironments;

	@JsonProperty
	@ColumnName("sprintUpdatedBy")
	public String updatedBy;

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public SprintCompanyDrivenFactors() {

	}

	public SprintCompanyDrivenFactors(Integer projectId, Integer sprintId, String coachingPotential,
			String teamKnowledgeDiversity, List<SprintDevelopmentEnvironment> developmentEnvironments,
			String updatedBy) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.coachingPotential = coachingPotential;
		this.teamKnowledgeDiversity = teamKnowledgeDiversity;
		this.developmentEnvironments = developmentEnvironments;
		this.updatedBy = updatedBy;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public String getCoachingPotential() {
		return coachingPotential;
	}

	public String getTeamKnowledgeDiversity() {
		return teamKnowledgeDiversity;
	}

	public List<SprintDevelopmentEnvironment> getDevelopmentEnvironments() {
		return developmentEnvironments;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setCoachingPotential(String coachingPotential) {
		this.coachingPotential = coachingPotential;
	}

	public void setTeamKnowledgeDiversity(String teamKnowledgeDiversity) {
		this.teamKnowledgeDiversity = teamKnowledgeDiversity;
	}

	public void setDevelopmentEnvironments(List<SprintDevelopmentEnvironment> developmentEnvironments) {
		this.developmentEnvironments = developmentEnvironments;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
