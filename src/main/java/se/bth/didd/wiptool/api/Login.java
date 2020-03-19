package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template for retrieving the details of a user.
 */

public class Login {

	@JsonProperty
	@PrimaryKey
	@ColumnName("userId")
	public Integer userId;

	@JsonProperty
	@ColumnName("userName")
	public String userName;
	
	@JsonProperty
	@ColumnName("userFirstName")
	public String userFirstName;
	
	@JsonProperty
	@ColumnName("userLastName")
	public String userLastName;

	@JsonProperty
	@ColumnName("userMailId")
	public String userMailId;

	@JsonProperty
	@ColumnName("password")
	public String password;
	
	@JsonProperty
	@ColumnName("apiKey")
	public String apiKey;
	
	@JsonProperty
	@ColumnName("role")
	public String role;

	
	public Login(Integer userId, String userName, String userFirstName, String userLastName, String userMailId,
			String password, String role) {
		this.userId = userId;
		this.userName = userName;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userMailId = userMailId;
		this.password = password;
		this.role = role;
	}
	public Login(String userName, String userFirstName, String userLastName, String userMailId,
			String password, String role) {
		this.userName = userName;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userMailId = userMailId;
		this.password = password;
		this.role = role;
	}
	public Login() {

	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserMailId() {
		return userMailId;
	}

	public String getPassword() {
		return password;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserMailId(String userMailId) {
		this.userMailId = userMailId;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
