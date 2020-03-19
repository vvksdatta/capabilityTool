package se.bth.didd.wiptool.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Communicates JWT token to be used after login.
 *
 */
public class LoginResponse {

	public LoginResponse(String token, String userFirstName, Integer userId, String role) {
		super();
		this.token = token;
		this.userFirstName = userFirstName;
		this.userId = userId;
		this.role = role;
	}

	@JsonProperty("token")
	public String token;

	@JsonProperty("userFirstName")
	public String userFirstName;

	@JsonProperty("userId")
	public Integer userId;
	
	@JsonProperty("role")
	public String role;

}