package se.bth.didd.wiptool.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;

/*Representation of projects with one to many relationship with sprints. This representation can be used for presenting summaries of people*/
public class ProjectNameSprints {

	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("projectId")
	public Integer projectId;

	@JsonProperty
	@ColumnName("projectName")
	public String projectName;
	
	@JsonProperty
	@OneToMany("sprints")
	public List<SprintNameIssues> sprints = new ArrayList<SprintNameIssues>();

	public ProjectNameSprints(Integer projectId, String projectName, List<SprintNameIssues> sprints) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.sprints = sprints;
	}
	
	public ProjectNameSprints(){
	
	}

	public List<SprintNameIssues> getSprints() {
		return sprints;
	}

	public void setSprints(List<SprintNameIssues> sprints) {
		this.sprints = sprints;
	}
}
