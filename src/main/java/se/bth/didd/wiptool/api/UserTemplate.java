package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

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

	public UserTemplate(Integer userId, String userName, String userFirstName, String userLastName, String usermailId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.usermailId = usermailId;
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

}
