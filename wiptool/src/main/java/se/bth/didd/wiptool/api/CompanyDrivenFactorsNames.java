package se.bth.didd.wiptool.api;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.OneToMany;

/*Sprint entity which  extends the SprintsSummary entity.*/

public class CompanyDrivenFactorsNames {

	
	@JsonProperty
	@ColumnName("expectedCoachingPotential")
	public String coachingPotential;

	@JsonProperty
	@ColumnName("expectedTeamKnowledgeDiversity")
	public String teamKnowledgeDiversity;

		/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	public CompanyDrivenFactorsNames() {

	}

	public CompanyDrivenFactorsNames(String coachingPotential, String teamKnowledgeDiversity) {
		super();
		this.coachingPotential = coachingPotential;
		this.teamKnowledgeDiversity = teamKnowledgeDiversity;
	}

	public String getCoachingPotential() {
		return coachingPotential;
	}

	public String getTeamKnowledgeDiversity() {
		return teamKnowledgeDiversity;
	}

	public void setCoachingPotential(String coachingPotential) {
		this.coachingPotential = coachingPotential;
	}

	public void setTeamKnowledgeDiversity(String teamKnowledgeDiversity) {
		this.teamKnowledgeDiversity = teamKnowledgeDiversity;
	}

	
}
