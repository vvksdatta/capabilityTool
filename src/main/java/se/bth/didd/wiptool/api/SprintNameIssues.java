package se.bth.didd.wiptool.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;

public class SprintNameIssues {
	@JsonProperty
	@PrimaryKey
	@ColumnName("sprintId")
	private Integer sprintId;

	@JsonProperty
	@ColumnName("sprintName")
	public String sprintName;
	
	@JsonProperty
	@OneToMany("issues")
	public List<IssueId> issues;

	@JsonProperty
	public int numberofIssues;
	
	public SprintNameIssues(Integer sprintId, String projectName, List<IssueId> issues ) {
		
		this.sprintId = sprintId;
		this.sprintName = projectName;
		this.issues = issues;
		this.numberofIssues = issues.size();
	}

	public SprintNameIssues(){
		
	}

	public List<IssueId> getIssues() {
		return issues;
	}

	public void setIssues(List<IssueId> issues) {
		this.issues = issues;
	}

	public int getNumberofIssues() {
		return numberofIssues;
	}

	public void setNumberofIssues(int numberofIssues) {
		this.numberofIssues = numberofIssues;
	}
	
}
