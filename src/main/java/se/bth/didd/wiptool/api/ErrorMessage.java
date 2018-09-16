package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
*Template for sending an error message. Mainly used in the 'Response' of various resource classes    
 */

public class ErrorMessage {
	@JsonProperty

	public String error;

	public ErrorMessage(String error) {
		super();
		this.error = error;
	}
	public ErrorMessage() {

	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
