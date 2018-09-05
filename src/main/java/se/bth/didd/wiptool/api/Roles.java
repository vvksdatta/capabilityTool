package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class Roles {
	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("roleId")
	public Integer roleId;
	
	@JsonProperty
	@ColumnName("roleName")
	public String roleName;

	public Roles(Integer roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}
	
	public Roles(){
		
	}

	public Integer getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
