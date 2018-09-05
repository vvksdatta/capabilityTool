package se.bth.didd.wiptool.api;


import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class AllocatedIssue {
	@JsonProperty
	@PrimaryKey
	@NotNull
	public Integer issueId;
	
	@JsonProperty
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
