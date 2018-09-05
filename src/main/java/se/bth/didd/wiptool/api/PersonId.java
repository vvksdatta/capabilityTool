package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class PersonId {
	
	@JsonProperty
	@PrimaryKey
	@ColumnName("personId")
	public Integer personId;
	
	public PersonId(Integer personId) {
		super();
		this.personId = personId;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public PersonId() {
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

}
