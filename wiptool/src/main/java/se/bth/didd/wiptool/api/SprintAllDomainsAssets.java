package se.bth.didd.wiptool.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class SprintAllDomainsAssets {
	@JsonProperty
	public List<SprintAsset> latestassetsList;
	
	@JsonProperty
	public List<SprintDomain> latestdomainsList;
	

	public SprintAllDomainsAssets() {
		
	}

	public SprintAllDomainsAssets(List<SprintAsset> latestassetsList, List<SprintDomain> latestdomainsList) {
		super();
		this.latestassetsList = latestassetsList;
		this.latestdomainsList = latestdomainsList;
	}

	public List<SprintAsset> getLatestassetsList() {
		return latestassetsList;
	}

	public List<SprintDomain> getLatestdomainsList() {
		return latestdomainsList;
	}

	public void setLatestassetsList(List<SprintAsset> latestassetsList) {
		this.latestassetsList = latestassetsList;
	}

	public void setLatestdomainsList(List<SprintDomain> latestdomainsList) {
		this.latestdomainsList = latestdomainsList;
	}

}
