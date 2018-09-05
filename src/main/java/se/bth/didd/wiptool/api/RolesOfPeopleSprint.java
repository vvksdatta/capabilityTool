package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class RolesOfPeopleSprint {

	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("personId")
	public Integer personId;
	
	@JsonProperty
	@ColumnName("personName")
	public String personName;

	@JsonProperty
	@ColumnName("roleName")
	public String roleName;
	

	public RolesOfPeopleSprint(Integer personId, String personName, String roleName) {
		super();
		this.personId = personId;
		this.personName = personName;
		this.roleName = roleName;
	}

	public RolesOfPeopleSprint(){
		
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

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
