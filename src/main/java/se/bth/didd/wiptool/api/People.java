package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template to fetch names of different persons who are enrolled on Redmine.
 */

public class People {
	
	@JsonProperty
	@PrimaryKey
	public Integer personId;
	
	@JsonProperty
	public String personName;
	
	@JsonProperty
	public String role;
	
		
	public People(Integer personId, String personName, String role) {
		super();
		this.personId = personId;
		this.personName = personName;
		this.role = role;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public People() {
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
