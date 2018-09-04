package se.bth.didd.wiptool.api;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;

/*Sprint entity which  extends the SprintsSummary entity.*/

public class SprintComprisingIssues {

	@NotNull
	@JsonProperty
	@ColumnName("projectId")
	public Integer projectId;
	

	@NotNull
	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	@NotNull
	@JsonProperty
	@ColumnName("issueId")
	public Integer issueId;

	public SprintComprisingIssues(Integer projectId, Integer sprintId, Integer issueId) {
		super();
		this.projectId = projectId;
		this.sprintId = sprintId;
		this.issueId = issueId;
	}

	public SprintComprisingIssues(){
		
	}
	public Integer getProjectId() {
		return projectId;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	
}
