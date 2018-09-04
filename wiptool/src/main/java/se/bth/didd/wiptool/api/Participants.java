package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.PrimaryKey;

/*ProjectMembers entity that holds personID and personName. This has a relation with projectSummaries and project entity. */

public class Participants {
	
	@JsonProperty
	@PrimaryKey
	public Integer personId;
	
	@JsonProperty
	public String personName;
	
	public Participants(Integer personId, String personName) {
		super();
		this.personId = personId;
		this.personName = personName;
	}
	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public Participants() {
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
