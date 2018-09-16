package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 * ProjectMembers entity that holds personID and personName. This has a relation with projectSummaries and project entity. 
 * */

public class ExistingPerson {
	
	@JsonProperty
	@PrimaryKey
	@ColumnName("personId")
	public Integer personId;
	
	@JsonProperty
	@ColumnName("firstName")
	public String firstName;
	
	@JsonProperty
	@ColumnName("lastName")
	public String lastName;
	
	@JsonProperty
	@ColumnName("emailId")
	public String emailID;
	
	public ExistingPerson(Integer personId, String firstName, String lastName, String emailID) {
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailID = emailID;
	
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public ExistingPerson() {
	}

	public Integer getPersonId() {
		return personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	
}
