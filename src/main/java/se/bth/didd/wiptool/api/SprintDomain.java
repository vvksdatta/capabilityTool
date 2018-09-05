package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

public class SprintDomain {
	@JsonProperty
	@NotNull
	@ColumnName("domainId")
	public int domainId;

	@JsonProperty
	@ColumnName("domainName")
	public String domainName;

	public SprintDomain() {

	}

	public SprintDomain(int domainId, String domainName) {
		super();
		this.domainId = domainId;
		this.domainName = domainName;
	}

	public int getDomainId() {
		return domainId;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

}
