package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template for updating issues allocated in a sprint. This class is used in association with {@code se.bth.didd.wiptool.api.SprintIdProjectIdIssuesAllocated}
 */

public class AllocatedIssue {
	
	/*
	 * @ColoumnName("name") is an annotation that belongs to jdbi-folder. This
	 * is essential for fetching values from the 'name' column of database
	 * table.
	 */
	
	@JsonProperty
	@PrimaryKey
	@NotNull
	@ColumnName("issueId")
	public Integer issueId;
	
	@JsonProperty
	@ColumnName("personId")
	public Integer personId;
	
	@JsonProperty
	@ColumnName("securityLevel")
	public String securityLevel;
	
	@JsonProperty
	@ColumnName("securityRiskAnalysis")
	public String securityRiskAnalysis;
	
	public AllocatedIssue(Integer issueId, Integer personId, String securityLevel, String securityRiskAnalysis) {
		super();
		this.issueId = issueId;
		this.personId = personId;
		this.securityLevel = securityLevel;
		this.securityRiskAnalysis = securityRiskAnalysis;
	}

	/*
	 * default constructor is required for FoldingListContainerFactory
	 * (jdbi-folder)
	 */
	
	public AllocatedIssue(){
		
	}

	public Integer getIssueId() {
		return issueId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public String getSecurityLevel() {
		return securityLevel;
	}

	public String getSecurityRiskAnalysis() {
		return securityRiskAnalysis;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	public void setSecurityRiskAnalysis(String securityRiskAnalysis) {
		this.securityRiskAnalysis = securityRiskAnalysis;
	}

}
