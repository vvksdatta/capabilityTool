package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template for fetching the details of a role associated to a person.
 */
public class RolesOfPeople {
	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("roleId")
	public Integer roleId;
	
	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("personId")
	public Integer personId;

	@JsonProperty
	@ColumnName("roleName")
	public String roleName;
	
	public RolesOfPeople(Integer roleId, Integer personId, String roleName) {
		super();
		this.roleId = roleId;
		this.personId = personId;
		this.roleName = roleName;
	}

	public RolesOfPeople(){
		
	}

	public Integer getRoleId() {
		return roleId;
	}

	
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
