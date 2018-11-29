package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template useful retrieving list of people.
 */

public class RedminePersonIdentifier {
	
	@JsonProperty
	@PrimaryKey
	@ColumnName("redminePersonIdentifier")
	public String redminePersonIdentifier;
	
	public RedminePersonIdentifier(String redminePersonIdentifier) {
		super();
		this.redminePersonIdentifier = redminePersonIdentifier;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public RedminePersonIdentifier() {
	}

	public String getRedminePersonIdentifier() {
		return redminePersonIdentifier;
	}

	public void setRedminePersonIdentifier(String redminePersonIdentifier) {
		this.redminePersonIdentifier = redminePersonIdentifier;
	}
}
