package se.bth.didd.wiptool.api;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class IssueUpdateTemplate {
	@JsonProperty
	@PrimaryKey
	@NotNull
	public Integer issueId;
	
	@JsonProperty
	@NotNull
	public Integer projectId;
	
	@JsonProperty
	public Integer personId;
	

	@JsonProperty
	@ColumnName("securityRiskAnalysis")
	public String securityRiskAnalysis;
	
	@JsonProperty
	@ColumnName("securityLevel")
	public String securityLevel;
	

	@JsonProperty
	@ColumnName("issueDone")
	public Integer issueDone;
	
	@JsonProperty
	@ColumnName("redmineLastUpdate")
	public Date redmineLastUpdate;
	
	@JsonProperty
	@ColumnName("issueLastUpdate")
	public Date issueLastUpdate;

	

	public IssueUpdateTemplate(Integer issueId, Integer projectId, Integer personId, String securityRiskAnalysis,
			String securityLevel, Integer issueDone, Date redmineLastUpdate, Date issueLastUpdate) {
		super();
		this.issueId = issueId;
		this.projectId = projectId;
		this.personId = personId;
		this.securityRiskAnalysis = securityRiskAnalysis;
		this.securityLevel = securityLevel;
		this.issueDone = issueDone;
		this.redmineLastUpdate = redmineLastUpdate;
		this.issueLastUpdate = issueLastUpdate;
	}

	public IssueUpdateTemplate(){
		
	}

	public Integer getIssueId() {
		return issueId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public String getSecurityRiskAnalysis() {
		return securityRiskAnalysis;
	}

	public String getSecurityLevel() {
		return securityLevel;
	}

	public Integer getIssueDone() {
		return issueDone;
	}

	public Date getRedmineLastUpdate() {
		return redmineLastUpdate;
	}

	public Date getIssueLastUpdate() {
		return issueLastUpdate;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setSecurityRiskAnalysis(String securityRiskAnalysis) {
		this.securityRiskAnalysis = securityRiskAnalysis;
	}

	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	public void setIssueDone(Integer issueDone) {
		this.issueDone = issueDone;
	}

	public void setRedmineLastUpdate(Date redmineLastUpdate) {
		this.redmineLastUpdate = redmineLastUpdate;
	}

	public void setIssueLastUpdate(Date issueLastUpdate) {
		this.issueLastUpdate = issueLastUpdate;
	}
}
