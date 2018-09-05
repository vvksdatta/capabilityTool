package se.bth.didd.wiptool.api;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class CapabilityValueOfPeople {
	@JsonProperty
	@NotNull
	@ColumnName("capabilityId")
	public Integer capabilityId;
	
	@JsonProperty
	@ColumnName("people")
	public List<PersonId> people;

	
	public CapabilityValueOfPeople() {
		
	}


	public CapabilityValueOfPeople(Integer capabilityId, List<PersonId> people) {
		super();
		this.capabilityId = capabilityId;
		this.people = people;
	}


	public Integer getCapabilityId() {
		return capabilityId;
	}


	public List<PersonId> getPeople() {
		return people;
	}


	public void setCapabilityId(Integer capabilityId) {
		this.capabilityId = capabilityId;
	}


	public void setPeople(List<PersonId> people) {
		this.people = people;
	}
}
