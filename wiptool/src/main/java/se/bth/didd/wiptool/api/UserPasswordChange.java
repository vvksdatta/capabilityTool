package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/*ProjectMembers entity that holds personID and personName. This has a relation with projectSummaries and project entity. */

public class UserPasswordChange {
	
	@JsonProperty
	@PrimaryKey
	@ColumnName("userId")
	public Integer userId;

	@JsonProperty
	@ColumnName("userName")
	public String userName;
	
	@JsonProperty
	@ColumnName("currentPassword")
	public String currentPassword;
	
	@JsonProperty
	@ColumnName("newPassword")
	public String newPassword;

	@JsonProperty
	@ColumnName("newPasswordRepeat")
	public String newPasswordRepeat;
	
	
	public UserPasswordChange(Integer userId, String userName, String currentPassword, String newPassword,
			String newPasswordRepeat) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.newPasswordRepeat = newPasswordRepeat;
	}

	public UserPasswordChange() {
		
	}
	public Integer getUserId() {
		return userId;
	}




	public String getUserName() {
		return userName;
	}




	public String getCurrentPassword() {
		return currentPassword;
	}




	public String getNewPassword() {
		return newPassword;
	}




	public String getNewPasswordRepeat() {
		return newPasswordRepeat;
	}




	public void setUserId(Integer userId) {
		this.userId = userId;
	}




	public void setUserName(String userName) {
		this.userName = userName;
	}




	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}




	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}




	public void setNewPasswordRepeat(String newPasswordRepeat) {
		this.newPasswordRepeat = newPasswordRepeat;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	
	
}
