package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonIdEstimatedHours {
	
	
	@JsonProperty
	@NotNull
	public Integer personId;
	
	@JsonProperty
	@NotNull
	public double estimatedHours;

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public double getEstimatedHours() {
		return estimatedHours;
	}

	public void setEstimatedHours(double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public PersonIdEstimatedHours(Integer personId, double estimatedHours) {
		super();
		this.personId = personId;
		this.estimatedHours = estimatedHours;
	}
	
}
