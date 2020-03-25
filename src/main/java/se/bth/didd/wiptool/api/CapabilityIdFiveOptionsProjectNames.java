package se.bth.didd.wiptool.api;

import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *Template for associating various measures of a capability to its ID.
 */

public class CapabilityIdFiveOptionsProjectNames {
	@JsonProperty
	@NotNull
	public int capabilityId;
	
	@JsonProperty
	public int option1;
	
	@JsonProperty
	public List<String> option1Projects;
	
	@JsonProperty
	public int option2;
	
	@JsonProperty
	public List<String> option2Projects;
	
	@JsonProperty
	public int option3;
	
	@JsonProperty
	public List<String> option3Projects;
	
	@JsonProperty
	public int option4;
	
	@JsonProperty
	public List<String> option4Projects;
	
	@JsonProperty
	public int option5;
	
	@JsonProperty
	public List<String> option5Projects;
	
	public CapabilityIdFiveOptionsProjectNames() {
		
	}

	public CapabilityIdFiveOptionsProjectNames(int capabilityId, int option1, List<String> option1Projects, int option2,
			List<String> option2Projects, int option3, List<String> option3Projects, int option4,
			List<String> option4Projects, int option5, List<String> option5Projects) {
		super();
		this.capabilityId = capabilityId;
		this.option1 = option1;
		this.option1Projects = option1Projects;
		this.option2 = option2;
		this.option2Projects = option2Projects;
		this.option3 = option3;
		this.option3Projects = option3Projects;
		this.option4 = option4;
		this.option4Projects = option4Projects;
		this.option5 = option5;
		this.option5Projects = option5Projects;
	}





	public int getCapabilityId() {
		return capabilityId;
	}

	public void setCapabilityId(int capabilityId) {
		this.capabilityId = capabilityId;
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

	public List<String> getOption1Projects() {
		return option1Projects;
	}

	public List<String> getOption2Projects() {
		return option2Projects;
	}

	public List<String> getOption3Projects() {
		return option3Projects;
	}

	public List<String> getOption4Projects() {
		return option4Projects;
	}

	public List<String> getOption5Projects() {
		return option5Projects;
	}

	public void setOption1Projects(List<String> option1Projects) {
		this.option1Projects = option1Projects;
	}

	public void setOption2Projects(List<String> option2Projects) {
		this.option2Projects = option2Projects;
	}

	public void setOption3Projects(List<String> option3Projects) {
		this.option3Projects = option3Projects;
	}

	public void setOption4Projects(List<String> option4Projects) {
		this.option4Projects = option4Projects;
	}

	public void setOption5Projects(List<String> option5Projects) {
		this.option5Projects = option5Projects;
	}

}
