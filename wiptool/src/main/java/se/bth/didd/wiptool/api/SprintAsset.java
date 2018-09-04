package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;
import com.github.rkmk.annotations.PrimaryKey;

public class SprintAsset {
	@JsonProperty
	@NotNull
	@ColumnName("assetId")
	public int assetId;
	
	@JsonProperty
	@ColumnName("assetName")
	public String assetName;

	
	public SprintAsset() {
		
	}


	public SprintAsset(int assetId, String assetName) {
		super();
		this.assetId = assetId;
		this.assetName = assetName;
	}


	public int getAssetId() {
		return assetId;
	}


	public String getAssetName() {
		return assetName;
	}


	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}


	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}


	
}
