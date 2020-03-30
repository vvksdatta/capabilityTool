package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 * Template to save new issues created from CAST
 */

public class IssueTemplateLimitedFields {

	@JsonProperty
	@ColumnName("sprintId")
	public Integer sprintId;

	@JsonProperty
	@ColumnName("issueName")
	public String issueName;

	@JsonProperty
	@ColumnName("categoryId")
	public Integer categoryId;

	@JsonProperty
	@ColumnName("issueDescription")
	public String issueDescription;

	public IssueTemplateLimitedFields(Integer sprintId, String issueName, Integer categoryId, String issueDescription) {
		super();
		this.sprintId = sprintId;
		this.issueName = issueName;
		this.categoryId = categoryId;
		this.issueDescription = issueDescription;
	}

	public IssueTemplateLimitedFields() {

	}

	public Integer getSprintId() {
		return sprintId;
	}

	public String getIssueName() {
		return issueName;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

}
