package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

/**
 *Template for issueId. Mainly used in sprints resource, to fetch issues associated with a sprint
 */

public class IssueId {
	@JsonProperty
	@PrimaryKey
	@ColumnName("issueId")
	public Integer issueId;

	public IssueId(Integer issueId) {
			this.issueId = issueId;
	}

	public IssueId(){
		
	}
}
