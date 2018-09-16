package se.bth.didd.wiptool.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *Template for fetching the list of domains and types of assets associated with a sprint.
 */

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
