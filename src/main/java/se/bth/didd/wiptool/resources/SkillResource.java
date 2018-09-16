package se.bth.didd.wiptool.resources;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.PersonId;
import se.bth.didd.wiptool.api.ProgrammingSkillValueOfPeople;
import se.bth.didd.wiptool.api.ProgrammingSkillsDetailsComparisonGraphs;
import se.bth.didd.wiptool.api.Skill;
import se.bth.didd.wiptool.api.SkillDetailsforGraphs;
import se.bth.didd.wiptool.api.SkillNameValueUser;
import se.bth.didd.wiptool.api.SkillTabsTemplate;
import se.bth.didd.wiptool.api.SkillTimelineGraphs;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.db.SkillDAO;

@Path("/skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkillResource {

	private SkillDAO skillDAO;

	public SkillResource(SkillDAO skillDAO) {
		this.skillDAO = skillDAO;
	}

	@GET
	@Path("/getProgrammingSkillsList")
	public Response getProgrammingSkillsList() {
		try {
			List<Skill> skillsList = skillDAO.getAllSkills();
			return Response.ok(skillsList).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/getProgrammingSkllsRecorded/{id}")
	public Response getProgrammingSkllsRecorded(@PathParam("id") Integer personId) {

		try {
			List<Skill> distinctSkillsOfPerson = skillDAO.getDistinctSkillsOfPerson(personId);
			return Response.ok(distinctSkillsOfPerson).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

	}

	@GET
	@Path("/getProgrammingSkllsTimeline/{personId}/{skillId}")
	public Response getProgrammingSkllsTimeline(@PathParam("personId") Integer personId,
			@PathParam("skillId") Integer skillId) {

		try {
			List<SkillTimelineGraphs> distinctProficienciesOfSkill = skillDAO.getDistinctProficienciesOfSkill(personId,
					skillId);
			return Response.ok(distinctProficienciesOfSkill).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

	}

	@GET
	@Path("/getProgrammingSkllsOfPerson/{id}")
	public Response getSkillsfPerson(@PathParam("id") Integer personId) {
		List<SkillDetailsforGraphs> setOfSkillDetails = new ArrayList<SkillDetailsforGraphs>();
		try {
			List<Skill> distinctSkillsOfPerson = skillDAO.getDistinctSkillsOfPerson(personId);
			for (Skill eachSkill : distinctSkillsOfPerson) {
				try {
					List<SkillDetailsforGraphs> skillsProficiency = skillDAO.skillsDetailsOfPerson(personId,
							eachSkill.getSkillId());
					for (SkillDetailsforGraphs proficiencyInEachSkill : skillsProficiency) {
						setOfSkillDetails.add(proficiencyInEachSkill);
					}
				} catch (Exception e) {
					System.out.println(e);
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}

			}
			return Response.ok(setOfSkillDetails).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@PUT
	@Path("/getProgrammingSkillValueOfPeople")
	public Response getProgrammingSkillValueOfPeople(ProgrammingSkillValueOfPeople programmingSkillOfPeople) {
		List<ProgrammingSkillsDetailsComparisonGraphs> programmingSkillValuesOfPeople = new ArrayList<ProgrammingSkillsDetailsComparisonGraphs>();

		for (PersonId eachPerson : programmingSkillOfPeople.getPeople()) {
			ProgrammingSkillsDetailsComparisonGraphs sampleDetail = new ProgrammingSkillsDetailsComparisonGraphs();
			try {
				if (skillDAO.ifSkillAssessed(eachPerson.getPersonId(), programmingSkillOfPeople.getSkillId())) {
					List<ProgrammingSkillsDetailsComparisonGraphs> capabilitiesOfPerson = skillDAO
							.specificSkillOfPerson(eachPerson.getPersonId(), programmingSkillOfPeople.getSkillId());

					for (ProgrammingSkillsDetailsComparisonGraphs capabilityDetails : capabilitiesOfPerson) {
						sampleDetail.setSkillName(capabilityDetails.getSkillName());
						sampleDetail.setLastUpdate(capabilityDetails.getLastUpdate());
						sampleDetail.setPersonName(capabilityDetails.getPersonName());
						sampleDetail.setProficiency(capabilityDetails.getProficiency());
						sampleDetail.setUpdatedBy(capabilityDetails.getUpdatedBy());
					}
					programmingSkillValuesOfPeople.add(sampleDetail);
				} else {
					List<People> personDetails = skillDAO.getPersonDetails(eachPerson.getPersonId());
					for (People person : personDetails) {
						sampleDetail.setPersonName(person.getPersonName());
					}
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					java.util.Date parsed = format.parse("20180101");
					java.sql.Date sql = new java.sql.Date(parsed.getTime());

					sampleDetail.setSkillName("");
					sampleDetail.setLastUpdate(sql);
					sampleDetail.setProficiency("");
					sampleDetail.setUpdatedBy("");
					programmingSkillValuesOfPeople.add(sampleDetail);
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		}
		programmingSkillValuesOfPeople.sort((o1, o2) -> o1.getLastUpdate().compareTo(o2.getLastUpdate()));
		return Response.ok(programmingSkillValuesOfPeople).build();

	}

	@PUT
	@Path("/searchSkill")
	public List<SkillTabsTemplate> searchSkill(String enteredSkill) {
		if (enteredSkill != "") {
			// System.out.println("the "+ enteredSkill);
			List<Skill> skills = skillDAO.getSkillId("%" + enteredSkill + "%");
			List<SkillTabsTemplate> sampleSkill = new ArrayList<SkillTabsTemplate>();
			for (Skill skill : skills) {
				// System.out.println("the skill is "+ skill.getSkillName());
				SkillTabsTemplate sample = new SkillTabsTemplate();
				sample.setValue(skill.getSkillName());
				sample.setDisplay(skill.getSkillName());
				sampleSkill.add(sample);
			}
			return sampleSkill;
		} else {
			List<Skill> skills = skillDAO.getAllSkills();
			List<SkillTabsTemplate> sampleSkill = new ArrayList<SkillTabsTemplate>();
			for (Skill skill : skills) {
				SkillTabsTemplate sample = new SkillTabsTemplate();
				sample.setValue(skill.getSkillName());
				sample.setDisplay(skill.getSkillName());
				sampleSkill.add(sample);
			}
			return sampleSkill;

		}
	}

	@POST
	@Path("/insertSkillsAssessment/{id}")
	public Response insertSkillAssessment(List<SkillNameValueUser> skillList, @PathParam("id") int personId) {
		Long timeStamp = Calendar.getInstance().getTimeInMillis();
		for (SkillNameValueUser eachEnteredSkill : skillList) {

			Timestamp timestamp = new Timestamp(timeStamp);
			List<Skill> listOfskills = skillDAO.getSkillId(eachEnteredSkill.getSkillName());
			for (Skill thisSkill : listOfskills) {
				try {

					skillDAO.insertSkillAssessment(personId, thisSkill.getSkillId(), eachEnteredSkill.getProficiency(),
							eachEnteredSkill.getUpdatedBy(), timestamp);
				} catch (Exception e) {

					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			}
		}
		SuccessMessage success = new SuccessMessage();
		success.setSuccess("success");
		return Response.ok(success).build();
	}

	@POST
	@Path("/insertSkill")
	public List<SkillTabsTemplate> insertSkill(String newSkill) {
		if (newSkill != "") {

			try {
				skillDAO.insertDefuaultValSkillDB(newSkill);
			} catch (Exception e1) {
				return null;
			}

			List<Skill> skills = skillDAO.getAllSkills();
			List<SkillTabsTemplate> sampleSkill = new ArrayList<SkillTabsTemplate>();
			for (Skill skill : skills) {
				SkillTabsTemplate sample = new SkillTabsTemplate();
				sample.setValue(skill.getSkillName());
				sample.setDisplay(skill.getSkillName());
				sampleSkill.add(sample);
			}
			return sampleSkill;
		}

		return null;
	}

}
