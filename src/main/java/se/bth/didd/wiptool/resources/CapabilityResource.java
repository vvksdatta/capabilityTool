package se.bth.didd.wiptool.resources;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.dropwizard.auth.Auth;
import se.bth.didd.wiptool.api.Capability;
import se.bth.didd.wiptool.api.CapabilityDetailsComparisonGraphs;
import se.bth.didd.wiptool.api.CapabilityIdMeasure;
import se.bth.didd.wiptool.api.CapabilityIdProficiency;
import se.bth.didd.wiptool.api.CapabilityList;
import se.bth.didd.wiptool.api.CapabilityTimelineGraphs;
import se.bth.didd.wiptool.api.CapabilityValueOfPeople;
import se.bth.didd.wiptool.api.CapabilityDetailsforGraphs;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.PersonId;
import se.bth.didd.wiptool.api.ProjectIdCapabilityIdProficiency;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.db.CapabilityDAO;

@Path("/capabilities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CapabilityResource {

	private CapabilityDAO capabilityDAO;

	public CapabilityResource(CapabilityDAO capabilityDAO) {

		this.capabilityDAO = capabilityDAO;
	}

	@GET
	@Path("/getCapabilitiesList")
	public Response getCapabilitiesList() {
		try {
			List<Capability> capabilitiesList = capabilityDAO.getAllCapabilities();
			return Response.ok(capabilitiesList).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}
	
	
	@GET
	@Path("/getCapabilitiesOfPerson/{id}")
	public List<CapabilityIdProficiency> getCapabilitiesOfPerson(@PathParam("id") Integer personId) {
		try {
			List<CapabilityIdProficiency> capabilitiesOfPerson = capabilityDAO.capabilitiesOfPerson(personId);
			return capabilitiesOfPerson;

		} catch (Exception e) {
			return null;
		}
	}
	
	@GET
	@Path("/getCapabilitiesOfPersoninProject/{projectId}/{personId}")
	public List<ProjectIdCapabilityIdProficiency> getCapabilitiesOfPerson(@PathParam("projectId") Integer projectId, @PathParam("personId") Integer personId) {
		try {
			List<ProjectIdCapabilityIdProficiency> capabilitiesOfPerson = capabilityDAO.capabilitiesOfPersoninProject(projectId, personId);
			return capabilitiesOfPerson;

		} catch (Exception e) {
			return null;
		}
	}

	@GET
	@Path("/getCapabilitiesTimeline/{personName}/{capabilityId}")
	public Response getCapabilitiesTimeline(@PathParam("personName") Integer personId,
			@PathParam("capabilityId") Integer capabilityId) {

		try {
			List<CapabilityTimelineGraphs> distinctProficienciesOfCapability = capabilityDAO
					.getDistinctProficienciesOfCapability(personId, capabilityId);
			return Response.ok(distinctProficienciesOfCapability).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

	}

	@GET
	@Path("/getCapabilityMeasures/{capabilityId}")
	public Response getCapabilityMeasures(@PathParam("capabilityId") Integer capabilityId) {

		try {
			List<CapabilityIdMeasure> measuresOfCapability = capabilityDAO.getMeasuresOfCapability(capabilityId);
			return Response.ok(measuresOfCapability).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

	}

	@GET
	@Path("/getCapabilityValuesOfPerson/{id}")
	public List<CapabilityDetailsforGraphs> getCapabilityValuesOfPerson(@PathParam("id") Integer personId) {
		try {
			List<CapabilityDetailsforGraphs> capabilitiesOfPerson = capabilityDAO.capabilityDetailsOfPerson(personId);
			return capabilitiesOfPerson;

		} catch (Exception e) {
			return null;
		}
	}
	
	@GET
	@Path("/ifcapabilitiesInProjectAssessed/{id}")
	public Response ifcapabilitiesInProjectAssessed(@PathParam("id") Integer projectId) {
		try {
			boolean capabilitiesAssessedInProject = capabilityDAO.ifcapabilitiesInProjectAssessed(projectId);
			return Response.ok(capabilitiesAssessedInProject).build();

		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/getCapabilityValueOfPeople")
	public Response getCapabilityValueOfPerson(@Auth User user,CapabilityValueOfPeople capabilityOfPeople) {
		List<CapabilityDetailsComparisonGraphs> capabilityValuesOfPeople = new ArrayList<CapabilityDetailsComparisonGraphs>();

		for (PersonId eachPerson : capabilityOfPeople.getPeople()) {
			CapabilityDetailsComparisonGraphs sampleDetail = new CapabilityDetailsComparisonGraphs();
			try {
				if (capabilityDAO.capabilityOfPersonExists(eachPerson.getPersonId(),
						capabilityOfPeople.getCapabilityId())) {
					List<CapabilityDetailsComparisonGraphs> capabilitiesOfPerson = capabilityDAO
							.specificCapabilityOfPerson(eachPerson.getPersonId(), capabilityOfPeople.getCapabilityId());

					for (CapabilityDetailsComparisonGraphs capabilityDetails : capabilitiesOfPerson) {
						if (capabilityDetails.getProficiency() != null) {
							sampleDetail.setCapabilityName(capabilityDetails.getCapabilityName());
							sampleDetail.setLastUpdate(capabilityDetails.getLastUpdate());
							sampleDetail.setPersonName(capabilityDetails.getPersonName());
							sampleDetail.setProficiency(capabilityDetails.getProficiency());
							sampleDetail.setUpdatedBy(capabilityDetails.getUpdatedBy());
						}
					}
					capabilityValuesOfPeople.add(sampleDetail);
				} else {
					List<People> personDetails = capabilityDAO.getPersonDetails(eachPerson.getPersonId());
					for (People person : personDetails) {
						sampleDetail.setPersonName(person.getPersonName());
					}
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					java.util.Date parsed = format.parse("20180101");
					java.sql.Date sql = new java.sql.Date(parsed.getTime());

					sampleDetail.setCapabilityName("");
					sampleDetail.setLastUpdate(sql);
					sampleDetail.setProficiency("");
					sampleDetail.setUpdatedBy("");
					capabilityValuesOfPeople.add(sampleDetail);
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		}

		capabilityValuesOfPeople.sort((o1, o2) -> o1.getLastUpdate().compareTo(o2.getLastUpdate()));
		return Response.ok(capabilityValuesOfPeople).build();

	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@POST
	@Path("/insertCapabilities/{projectId}/{personId}")
	public Response insertCapabilities(@Auth User user, List<CapabilityList> capabilityList, @PathParam("projectId") Integer projectId, @PathParam("personId") Integer personId) {
		Long timeStamp = Calendar.getInstance().getTimeInMillis();

		for (CapabilityList capability : capabilityList) {

			if (capability.getCommitment() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 1, capability.getCommitment(),
						capability.getUpdatedBy(), timestamp);
			}
			if (capability.getDomainKnowledge() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 2, capability.getDomainKnowledge(),
						capability.getUpdatedBy(), timestamp);
			}
			if (capability.getOwnInterest() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 3, capability.getOwnInterest(),
						capability.getUpdatedBy(), timestamp);
			}
			if (capability.getPrevDelQuality() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 4, capability.getPrevDelQuality(),
						capability.getUpdatedBy(), timestamp);
			}
			if (capability.getPrevProjectPerf() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 5, capability.getPrevProjectPerf(),
						capability.getUpdatedBy(), timestamp);
			}
			if (capability.getPrgmExperience() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 6, capability.getPrgmExperience(),
						capability.getUpdatedBy(), timestamp);
			}
			if (capability.getPrgmLanKnowledge() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 7, capability.getPrgmLanKnowledge(),
						capability.getUpdatedBy(), timestamp);
			}
			if (capability.getUndrSoftSec() != "") {
				Timestamp timestamp = new Timestamp(timeStamp);
				capabilityDAO.insertCapabilityAssessment(projectId, personId, 8, capability.getUndrSoftSec(),
						capability.getUpdatedBy(), timestamp);
			}
		}
		SuccessMessage success = new SuccessMessage();
		success.setSuccess("updated");
		return Response.ok(success).build();
	}
	
	
	@POST
	@Path("/insertDefaultCapabilities")
	public Response insertDefaultCapabilities() {

		List<Integer> numberOfCapabilities = capabilityDAO.getNumbOfCapabilities();

		if (numberOfCapabilities.get(0).equals(0)) {
			List<String> capabilities = new ArrayList<String>();

			capabilities.add("Commitment");
			capabilities.add("Domain knowledge");
			capabilities.add("Person's own interest");
			capabilities.add("Previous deliverables' quality");
			capabilities.add("Previous projects performance");
			capabilities.add("Programming experience");
			capabilities.add("Programming language knowledge");
			capabilities.add("Understanding software security");
			// capabilities.add("");
			for (String eachCapability : capabilities) {

				try {
					capabilityDAO.insertDefuaultValCapabilityDB(eachCapability);
				} catch (Exception e) {
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			}

			for (String eachCapability : capabilities) {

				List<Capability> Updatedcapability = capabilityDAO.getCapabilityId(eachCapability);
				for (Capability eachUpdatedCapability : Updatedcapability) {

					if (eachUpdatedCapability.getCapabilityName().equals("Commitment")) {
						System.out.println(
								eachUpdatedCapability.getCapabilityId() + eachUpdatedCapability.getCapabilityName());
						List<String> measures = new ArrayList<String>();
						measures.add("Superficial");
						measures.add("Satisfactory");
						measures.add("Good");
						measures.add("Excellent");
						measures.add("Perfect");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
					if (eachUpdatedCapability.getCapabilityName().equals("Domain knowledge")) {
						List<String> measures = new ArrayList<String>();
						measures.add("Superficial");
						measures.add("Satisfactory");
						measures.add("Good");
						measures.add("Excellent");
						measures.add("Perfect");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
					if (eachUpdatedCapability.getCapabilityName().equals("Person's own interest")) {
						List<String> measures = new ArrayList<String>();
						measures.add("Undefined");
						measures.add("No match");
						measures.add("Average match");
						measures.add("Good match");
						measures.add("Excellent match");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
					if (eachUpdatedCapability.getCapabilityName().equals("Previous deliverables' quality")) {
						List<String> measures = new ArrayList<String>();
						measures.add("Undefined");
						measures.add("Acceptable");
						measures.add("Good");
						measures.add("Excellent");
						measures.add("Outstanding");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
					if (eachUpdatedCapability.getCapabilityName().equals("Previous projects performance")) {
						List<String> measures = new ArrayList<String>();
						measures.add("Undefined");
						measures.add("Acceptable");
						measures.add("Good");
						measures.add("Excellent");
						measures.add("Outstanding");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
					if (eachUpdatedCapability.getCapabilityName().equals("Programming experience")) {
						List<String> measures = new ArrayList<String>();
						measures.add("Undefined");
						measures.add("Average");
						measures.add("Good");
						measures.add("High");
						measures.add("Very high");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
					if (eachUpdatedCapability.getCapabilityName().equals("Programming language knowledge")) {
						List<String> measures = new ArrayList<String>();
						measures.add("Undefined");
						measures.add("Acceptable");
						measures.add("Good");
						measures.add("Excellent");
						measures.add("Outstanding");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
					if (eachUpdatedCapability.getCapabilityName().equals("Understanding software security")) {
						List<String> measures = new ArrayList<String>();
						measures.add("Undefined");
						measures.add("Average");
						measures.add("Good");
						measures.add("High");
						measures.add("Very high");
						for (String measure : measures) {
							try {
								capabilityDAO.insertDefuaultMeasuresCapabilities(
										eachUpdatedCapability.getCapabilityId(), measure);
							} catch (Exception e) {
								return Response.status(Status.BAD_REQUEST).entity(e).build();
							}
						}
					}
				}
			}
			SuccessMessage success = new SuccessMessage();
			success.setSuccess("Updated capabilities successfully");
			return Response.ok(success).build();
		}
		return null;
	}
}
