package se.bth.didd.wiptool.api;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;

/**
 * People summaries entity which has one-to-many mapping with projects and sprints
 * */

public class PeopleSummary {

	@JsonProperty
	@PrimaryKey
	@ColumnName("personId")
	public Integer personId;
	
	@JsonProperty
	@ColumnName("personName")
	public String personName;
	
	@JsonProperty
	@OneToMany("projects")
	public List<ProjectNameSprints> projects = new ArrayList<ProjectNameSprints>();

	public PeopleSummary(Integer personId, String personName, List<ProjectNameSprints> projects) {
		this.personId = personId;
		this.personName = personName;
		this.projects = projects;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public PeopleSummary() {

	}

	public List<ProjectNameSprints> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectNameSprints> projects) {
		this.projects = projects;
	}

	public Integer getPersonId() {
		return personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
