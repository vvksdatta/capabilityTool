package se.bth.didd.wiptool.api;


import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SprintIdProjectIdParticipants {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;
	
	@JsonProperty
	@ColumnName("personName")
	public List<People> sprintParticipants;

	public SprintIdProjectIdParticipants(Integer projectId, Integer sprintId, List<People> sprintParticipants) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.sprintParticipants = sprintParticipants;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public SprintIdProjectIdParticipants() {

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

	public List<People> getSprintParticipants() {
		return sprintParticipants;
	}

	public void setSprintParticipants(List<People> sprintParticipants) {
		this.sprintParticipants = sprintParticipants;
	}

}
