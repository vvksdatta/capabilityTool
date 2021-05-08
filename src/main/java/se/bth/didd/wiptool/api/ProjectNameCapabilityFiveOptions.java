package se.bth.didd.wiptool.api;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rkmk.annotations.ColumnName;

/**
 *Template for associating various measures of a capability to its ID.
 */

public class ProjectNameCapabilityFiveOptions {
	@JsonProperty
	@NotNull
	@ColumnName("projectName")
	public String projectName;
	
	@JsonProperty
	@ColumnName("capabilityId")
	public int capabilityId; 
	
	@JsonProperty
	@ColumnName("option1")
	public int option1;
	
	@JsonProperty
	@ColumnName("option2")
	public int option2;
	
	@JsonProperty
	@ColumnName("option3")
	public int option3;
	
	@JsonProperty
	@ColumnName("option4")
	public int option4;
	
	@JsonProperty
	@ColumnName("option5")
	public int option5;
	
	public ProjectNameCapabilityFiveOptions() {
		
	}

	public ProjectNameCapabilityFiveOptions(String projectName, int capabilityId, int option1, int option2, int option3,
			int option4, int option5) {
		super();
		this.projectName = projectName;
		this.capabilityId = capabilityId;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.option5 = option5;
	}
	

	public int getCapabilityId() {
		return capabilityId;
	}

	public void setCapabilityId(int capabilityId) {
		this.capabilityId = capabilityId;
	}

	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public int getOption1() {
		return option1;
	}


	public int getOption2() {
		return option2;
	}


	public int getOption3() {
		return option3;
	}


	public int getOption4() {
		return option4;
	}


	public int getOption5() {
		return option5;
	}


	public void setOption1(int option1) {
		this.option1 = option1;
	}


	public void setOption2(int option2) {
		this.option2 = option2;
	}


	public void setOption3(int option3) {
		this.option3 = option3;
	}


	public void setOption4(int option4) {
		this.option4 = option4;
	}


	public void setOption5(int option5) {
		this.option5 = option5;
	}

}
