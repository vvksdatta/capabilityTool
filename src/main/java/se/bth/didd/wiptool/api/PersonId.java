package se.bth.didd.wiptool.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;
import com.github.rkmk.annotations.PrimaryKey;

/*ProjectMembers entity that holds personID and personName. This has a relation with projectSummaries and project entity. */

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
