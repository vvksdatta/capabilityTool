package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template useful retrieving list of people.
 */

public class PersonIdNameEmail extends PersonId {
	
	@JsonProperty
	@ColumnName("firstName")
	public String firstName;
	
	@JsonProperty
	@ColumnName("lastName")
	public String lastName;
	
	@JsonProperty
	@ColumnName("fullName")
	public String fullName;
	
	@JsonProperty
	@ColumnName("emailId")
	public String emailID;
	
	public PersonIdNameEmail(Integer personId, String firstName, String lastName, String fullName, String emailID) {
		super(personId);
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.emailID = emailID;
	}

	public PersonIdNameEmail() {
		
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

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	
	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
}
