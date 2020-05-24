package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 * Template for retrieving the details of a user.
 */

public class LoginFullName extends Login {

	@JsonProperty
	@ColumnName("userFullName")
	public String userFullName;

	public LoginFullName(Integer userId, String userName, String userFirstName, String userLastName, String userMailId,
			String password, String role, String userFullName) {
		super(userId, userName, userFirstName, userLastName, userMailId, password, role);
		this.userFullName = userFullName;
	}

	public LoginFullName() {

	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

}
