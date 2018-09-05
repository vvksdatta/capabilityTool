package se.bth.didd.wiptool.api;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;

public class SprintSummary {

	@JsonProperty
	@PrimaryKey
	@ColumnName("sprintId")
	public Integer sprintId;

	@JsonProperty
	@ColumnName("sprintName")
	public String sprintName;

	@JsonProperty
	@ColumnName("sprintProgress")
	public Integer sprintProgress;

	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@JsonProperty
	@ColumnName("projectName")
	public String projectName;

	@JsonProperty
	@OneToMany("teamMembers")
	public List<Participants> teamMembers = new ArrayList<Participants>();

	public SprintSummary(Integer sprintId, String sprintName, Integer sprintProgress, Integer projectId,
			String projectName, List<Participants> teamMembers) {
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.sprintProgress = sprintProgress;
		this.projectId = projectId;
		this.projectName = projectName;
		this.teamMembers = teamMembers;
	}

	public SprintSummary() {

	}

	public Integer getSprintId() {
		return sprintId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public Integer getSprintProgress() {
		return sprintProgress;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public void setSprintProgress(Integer sprintProgress) {
		this.sprintProgress = sprintProgress;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public List<Participants> getTeamMembers() {
		return teamMembers;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setTeamMembers(List<Participants> teamMembers) {
		this.teamMembers = teamMembers;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
