package se.bth.didd.wiptool.api;

import java.util.Date;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class IssueTemplate {
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
	@ColumnName("issueName")
	public String issueName;
	
	@JsonProperty
	@ColumnName("issueStartDate")
	public Date issueStartDate;

	@JsonProperty
	@ColumnName("issueDueDate")
	public Date issueDueDate;
	
	@JsonProperty
	@ColumnName("issueCategory")
	public String issueCategory;
	
	@JsonProperty
	@ColumnName("issueDescription")
	public String issueDescription;

	
	@JsonProperty
	@ColumnName("issuePriority")
	public String issuePriority;
	
	@JsonProperty
	@ColumnName("securityRiskAnalysis")
	public String securityRiskAnalysis;
	
	@JsonProperty
	@ColumnName("securityLevel")
	public String securityLevel;
	
	@JsonProperty
	@ColumnName("issueEstimatedTime")
	public Float issueEstimatedTime;

	@JsonProperty
	@ColumnName("issueDone")
	public Integer issueDone;
	
	@JsonProperty
	@ColumnName("redmineLastUpdate")
	public Date redmineLastUpdate;
	
	@JsonProperty
	@ColumnName("issueLastUpdate")
	public Date issueLastUpdate;

	public IssueTemplate(Integer issueId, Integer projectId, Integer personId, String issueName, Date issueStartDate,
			Date issueDueDate, String issueCategory, String issueDescription, String issuePriority,
			String securityRiskAnalysis, String securityLevel, Float issueEstimatedTime, Integer issueDone,
			Date redmineLastUpdate, Date issueLastUpdate) {
		super();
		this.issueId = issueId;
		this.projectId = projectId;
		this.personId = personId;
		this.issueName = issueName;
		this.issueStartDate = issueStartDate;
		this.issueDueDate = issueDueDate;
		this.issueCategory = issueCategory;
		this.issueDescription = issueDescription;
		this.issuePriority = issuePriority;
		this.securityRiskAnalysis = securityRiskAnalysis;
		this.securityLevel = securityLevel;
		this.issueEstimatedTime = issueEstimatedTime;
		this.issueDone = issueDone;
		this.redmineLastUpdate = redmineLastUpdate;
		this.issueLastUpdate = issueLastUpdate;
	}

	public IssueTemplate(){
		
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

	public Date getIssueStartDate() {
		return issueStartDate;
	}

	public Date getIssueDueDate() {
		return issueDueDate;
	}

	public String getIssueCategory() {
		return issueCategory;
	}

	public String getIssuePriority() {
		return issuePriority;
	}

	public String getSecurityRiskAnalysis() {
		return securityRiskAnalysis;
	}

	public String getSecurityLevel() {
		return securityLevel;
	}

	public Float getIssueEstimatedTime() {
		return issueEstimatedTime;
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

	public void setIssueStartDate(Date issueStartDate) {
		this.issueStartDate = issueStartDate;
	}

	public void setIssueDueDate(Date issueDueDate) {
		this.issueDueDate = issueDueDate;
	}

	public void setIssueCategory(String issueCategory) {
		this.issueCategory = issueCategory;
	}

	public void setIssuePriority(String issuePriority) {
		this.issuePriority = issuePriority;
	}

	public void setSecurityRiskAnalysis(String securityRiskAnalysis) {
		this.securityRiskAnalysis = securityRiskAnalysis;
	}

	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}

	public void setIssueEstimatedTime(Float issueEstimatedTime) {
		this.issueEstimatedTime = issueEstimatedTime;
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

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}
}
