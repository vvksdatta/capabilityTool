package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Template for issueId. Mainly used in sprints resource, to fetch issues
 * associated with a sprint
 */

public class Status {
	@JsonProperty
	public Integer id;

	@JsonProperty
	public String name;

	public Status(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Status() {

	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
