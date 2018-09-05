package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.PrimaryKey;

public class ProjectParticipants {
	
	@JsonProperty
	@PrimaryKey
	public Integer personId;
	
	@JsonProperty
	public String personName;
	
	@JsonProperty
	public Integer roleId;
		
	@JsonProperty
	public String roleName;
	
	
	public ProjectParticipants(Integer personId, String personName,Integer roleId, String roleName) {
		super();
		this.personId = personId;
		this.personName = personName;
		this.roleId = roleId;
		this.roleName = roleName;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ProjectParticipants() {
	}
	
	public Integer getPersonId() {
		return personId;
	}

	public String getPersonName() {
		return personName;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
}
