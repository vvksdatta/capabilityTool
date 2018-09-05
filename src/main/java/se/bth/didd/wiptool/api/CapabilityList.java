package se.bth.didd.wiptool.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CapabilityList {
	
	@JsonProperty
	public String commitment;

	@JsonProperty
	public String domainKnowledge;

	@JsonProperty
	public String ownInterest;

	@JsonProperty
	public String prevDelQuality;
	
	@JsonProperty
	public String prevProjectPerf;
	
	@JsonProperty
	public String prgmExperience;
	
	@JsonProperty
	public String prgmLanKnowledge;
	
	@JsonProperty
	public String undrSoftSec;
	
	@JsonProperty
	public String updatedBy;

	public CapabilityList() {
		
	}

	public CapabilityList(String commitment, String domainKnowledge, String ownInterest, String prevDelQuality,
			String prevProjectPerf, String prgmExperience, String prgmLanKnowledge, String undrSoftSec) {
		this.commitment = commitment;
		this.domainKnowledge = domainKnowledge;
		this.ownInterest = ownInterest;
		this.prevDelQuality = prevDelQuality;
		this.prevProjectPerf = prevProjectPerf;
		this.prgmExperience = prgmExperience;
		this.prgmLanKnowledge = prgmLanKnowledge;
		this.undrSoftSec = undrSoftSec;
	}

	public String getCommitment() {
		return commitment;
	}

	public String getDomainKnowledge() {
		return domainKnowledge;
	}

	public String getOwnInterest() {
		return ownInterest;
	}

	public String getPrevDelQuality() {
		return prevDelQuality;
	}

	public String getPrevProjectPerf() {
		return prevProjectPerf;
	}

	public String getPrgmExperience() {
		return prgmExperience;
	}

	public String getPrgmLanKnowledge() {
		return prgmLanKnowledge;
	}

	public String getUndrSoftSec() {
		return undrSoftSec;
	}

	public void setCommitment(String commitment) {
		this.commitment = commitment;
	}

	public void setDomainKnowledge(String domainKnowledge) {
		this.domainKnowledge = domainKnowledge;
	}

	public void setOwnInterest(String ownInterest) {
		this.ownInterest = ownInterest;
	}

	public void setPrevDelQuality(String prevDelQuality) {
		this.prevDelQuality = prevDelQuality;
	}

	public void setPrevProjectPerf(String prevProjectPerf) {
		this.prevProjectPerf = prevProjectPerf;
	}

	public void setPrgmExperience(String prgmExperience) {
		this.prgmExperience = prgmExperience;
	}

	public void setPrgmLanKnowledge(String prgmLanKnowledge) {
		this.prgmLanKnowledge = prgmLanKnowledge;
	}

	public void setUndrSoftSec(String undrSoftSec) {
		this.undrSoftSec = undrSoftSec;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
