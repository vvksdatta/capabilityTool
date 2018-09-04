package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.PrimaryKey;

public class SprintNameProgress {
	@JsonProperty
	@PrimaryKey
	public Integer sprintId;

	@JsonProperty
	public String sprintName;

	@JsonProperty
	public Integer sprintProgress;

	public SprintNameProgress(Integer sprintId, String sprintName, Integer sprintProgress) {
		super();
		this.sprintId = sprintId;
		this.sprintName = sprintName;
		this.sprintProgress = sprintProgress;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public SprintNameProgress() {
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

}
