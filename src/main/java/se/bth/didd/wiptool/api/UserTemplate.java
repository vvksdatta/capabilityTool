package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 * Template for creating a new user.
 */

public class UserTemplate {

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
	@ColumnName("usermailId")
	public String usermailId;

	@JsonProperty
	@ColumnName("apiKey")
	public String apiKey;

	@JsonProperty
	@ColumnName("role")
	public String role;

	public UserTemplate(Integer userId, String userName, String userFirstName, String userLastName, String usermailId,
			String role) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.usermailId = usermailId;
		this.role = role;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public UserTemplate() {
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUsermailId() {
		return usermailId;
	}

	public void setUsermailId(String usermailId) {
		this.usermailId = usermailId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
