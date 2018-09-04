package se.bth.didd.wiptool.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/*Project entity which  extends the projectSummary entity.*/

public class ProjectsList  {

	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("projectId")
	public Integer projectId;
	/*
	 * @ColoumnName("name") is an annotation that belongs to jdbi-folder. This is
	 * essential for fetching values from the 'name' column of database table.
	 */ 
	
	@JsonProperty
	@ColumnName("projectName")
	public String projectName;
	
	@JsonProperty
	public List<ProjectsList> projectList;

	
	public ProjectsList(Integer projectId, String projectName, List<ProjectsList> projectList) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		this.projectList = projectList;
	}



	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ProjectsList() {

	}



	public Integer getProjectId() {
		return projectId;
	}



	public String getProjectName() {
		return projectName;
	}


	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public List<ProjectsList> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<ProjectsList> projectList) {
		this.projectList = projectList;
	}



}
