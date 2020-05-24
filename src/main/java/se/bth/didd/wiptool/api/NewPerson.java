package se.bth.didd.wiptool.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;

/**
 * ProjectMembers entity that holds personID and personName. This has a relation
 * with projectSummaries and project entity.
 */

public class NewPerson {

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

	@JsonProperty
	@OneToMany("roles")
	public List<Roles> roles;

	@JsonProperty
	@ColumnName("userId")
	public Integer userId;

	public NewPerson(Integer personId, String firstName, String lastName, String emailID, List<Roles> roles) {
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailID = emailID;
		this.roles = roles;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public NewPerson() {
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

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
