package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template useful retrieving list of people.
 */

public class RedmineProjectIdentifier {
	
	@JsonProperty
	@PrimaryKey
	@ColumnName("redmineProjectIdentifier")
	public String redmineProjectIdentifier;
	
	public RedmineProjectIdentifier(String redmineProjectIdentifier) {
		super();
		this.redmineProjectIdentifier = redmineProjectIdentifier;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public RedmineProjectIdentifier() {
	}

	public String getRedmineProjectIdentifier() {
		return redmineProjectIdentifier;
	}

	public void setRedmineProjectIdentifier(String redmineProjectIdentifier) {
		this.redmineProjectIdentifier = redmineProjectIdentifier;
	}

}
