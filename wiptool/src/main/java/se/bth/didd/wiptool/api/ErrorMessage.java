package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

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
