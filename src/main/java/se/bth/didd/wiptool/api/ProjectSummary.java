package se.bth.didd.wiptool.api;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;

/**
 * Project summaries entity which has one-to-many mapping with projectmembers and sprints.
 * */
public class ProjectSummary {

	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("projectId")
	public Integer projectId;
	
	@JsonProperty
	@ColumnName("parentProjectId")
	public Integer parentProjectId;
	
	/*
	 * @ColoumnName("name") is an annotation that belongs to jdbi-folder. This is
	 * essential for fetching values from the 'name' column of database table.
	 */ 
	
	@JsonProperty
	@ColumnName("projectName")
	public String projectName;

	@JsonProperty
	@ColumnName("parentProjectName")
	public String parentProjectName;
	
	@JsonProperty
	@ColumnName("projectLeader")
	public String projectLeader;
	
	@JsonProperty("projectProgress")
	public Integer projectProgress;

	@JsonProperty
	@OneToMany("projectMembers")
	public List<Participants> projectMembers = new ArrayList<Participants>();

	@JsonProperty
	@OneToMany("sprints")
	public List<SprintNameProgress> sprints = new ArrayList<SprintNameProgress>();

	@JsonProperty
	public List<String> projectHierarchy;

	
	public ProjectSummary(Integer projectId, Integer parentProjectId, String projectName, String parentProjectName,
			String projectLeader, Integer projectProgress, List<Participants> projectMembers,
			List<SprintNameProgress> sprints, List<String> projectHierarchy) {
		super();
		this.projectId = projectId;
		this.parentProjectId = parentProjectId;
		this.projectName = projectName;
		this.parentProjectName = parentProjectName;
		this.projectLeader = projectLeader;
		this.projectProgress = projectProgress;
		this.projectMembers = projectMembers;
		this.sprints = sprints;
		this.projectHierarchy = projectHierarchy;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ProjectSummary() {

	}

	public List<Participants> getProjectMembers() {
		return projectMembers;
	}

	public void setProjectMembers(List<Participants> projectMembers) {
		this.projectMembers = projectMembers;
	}

	public List<SprintNameProgress> getSprints() {
		return sprints;
	}

	public void setSprints(List<SprintNameProgress> sprints) {
		this.sprints = sprints;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public Integer getProjectProgress() {
		return projectProgress;
	}

	public void setProjectProgress(Integer projectProgress) {
		this.projectProgress = projectProgress;
	}

	public Integer getParentProjectId() {
		return parentProjectId;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public void setParentProjectId(Integer parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}

	public List<String> getProjectHierarchy() {
		return projectHierarchy;
	}

	public void setProjectHierarchy(List<String> projectHierarchy) {
		this.projectHierarchy = projectHierarchy;
	}
	
}
