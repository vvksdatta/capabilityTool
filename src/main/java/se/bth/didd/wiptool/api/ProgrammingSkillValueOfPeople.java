package se.bth.didd.wiptool.api;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for retrieving list of people who are associated with a particular skill. 
 **/

public class ProgrammingSkillValueOfPeople {
	@JsonProperty
	@NotNull
	@ColumnName("skillId")
	public Integer skillId;
	
	@JsonProperty
	@ColumnName("people")
	public List<PersonId> people;

	
	public ProgrammingSkillValueOfPeople() {
		
	}

	public ProgrammingSkillValueOfPeople(Integer skillId, List<PersonId> people) {
		super();
		this.skillId = skillId;
		this.people = people;
	}


	public Integer getSkillId() {
		return skillId;
	}


	public List<PersonId> getPeople() {
		return people;
	}


	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}


	public void setPeople(List<PersonId> people) {
		this.people = people;
	}

}
