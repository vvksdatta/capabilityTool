package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
*Template for sending a success  message. Mainly used in the 'Response' of various resource classes    
 */

public class SuccessMessage {
	@JsonProperty

	public String success;

	public SuccessMessage() {

	}

	public SuccessMessage(String success) {
		super();
		this.success = success;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

}
