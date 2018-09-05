package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/*Project entity which  extends the projectSummary entity.*/

public class ProjectIdName {

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


	public ProjectIdName(Integer projectId, String projectName) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ProjectIdName() {

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

}
