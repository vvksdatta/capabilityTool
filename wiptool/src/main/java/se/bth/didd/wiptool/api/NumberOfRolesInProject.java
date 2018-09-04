package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class NumberOfRolesInProject extends Roles {
	@JsonProperty
	public Integer numberOfPeople;

	public NumberOfRolesInProject(Integer roleId, String roleName, Integer numberOfPeople) {
		super(roleId, roleName);
		this.numberOfPeople = numberOfPeople;
	}

public NumberOfRolesInProject(){
		
	}
	public Integer getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(Integer numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}
	
	
}
