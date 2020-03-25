package se.bth.didd.wiptool.resources;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rkmk.container.FoldingList;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import io.dropwizard.auth.Auth;
import se.bth.didd.wiptool.api.ErrorMessage;
import se.bth.didd.wiptool.api.ExistingPerson;
import se.bth.didd.wiptool.api.NewPerson;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.PeopleSummary;
import se.bth.didd.wiptool.api.ProjectNameSprints;
import se.bth.didd.wiptool.api.SprintNameIssues;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.api.UserPasswordChange;
import se.bth.didd.wiptool.api.UserTemplate;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.db.PeopleDAO;
import se.bth.didd.wiptool.api.Roles;

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
/*
 * This servers as the main API resource for projects and projectsSumary
 * entities
 */
public class PeopleResource {

	private String redmineUrl;
	private PeopleDAO peopleDAO;

	/*
	 * This constructor is invoked in the run method of the main application and
	 * it maps the redmine configuration values to the variables 'redmineUrl'
	 * and 'apiAcessKey'.
	 */

	public PeopleResource(PeopleDAO peopleDAO, String redmineUrl) {
		this.redmineUrl = redmineUrl;
		this.peopleDAO = peopleDAO;
		
	}

	public PeopleResource() {
		
	}
	
	@GET
	@Path("/getPersonName/{id}")
	public Response getPerson(@Auth User user, @PathParam("id") Integer personId) throws SQLException {
		List<People> person = peopleDAO.getPersonDetails(personId);
		People personDetails = new People();
		for (People singlePerson : person) {
			personDetails.setPersonName(singlePerson.getPersonName());
			personDetails.setPersonId(singlePerson.getPersonId());
		}
		return Response.ok(personDetails).build();
	}
	
	@GET
	@Path("/getProjectsCountPerson/{id}")
	public Response getProjectsCountPerson(@Auth User user, @PathParam("id") Integer personId) throws SQLException {
		try {
			List<Integer> count = peopleDAO.getProjectsCountPerson(personId);
			return Response.ok(count).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/getUserName/{id}")
	public Response getUser(@Auth User currentUser, @PathParam("id") Integer personId) throws SQLException {
		List<UserTemplate> user = peopleDAO.getUserDetails(personId);
		People personDetails = new People();
		for (UserTemplate singleUser : user) {
			personDetails.setPersonName(singleUser.getUserFirstName() + " " + singleUser.getUserLastName());
			personDetails.setPersonId(singleUser.getUserId());
		}
		return Response.ok(personDetails).build();
	}

	@GET
	@Path("/getUserDetailsbyId/{id}")
	public Response getUserDetailsbyId(@Auth User currentUser, @PathParam("id") Integer userId) throws SQLException {
		List<UserTemplate> user = peopleDAO.getUserDetails(userId);
		UserTemplate userDetails = new UserTemplate();
		for (UserTemplate singleUser : user) {
			userDetails = singleUser;
		}
		return Response.ok(userDetails).build();
	}

	@GET
	@Path("/getAllPeople")
	public Response getPerson(@Auth User user) {
		try {
			List<People> people = peopleDAO.getAllPeople();
			return Response.ok(people).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/getUsersList")

	public Response getUsersList(@Auth User user) {
		try {
			List<UserTemplate> users = peopleDAO.getUsersList();
			return Response.ok(users).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@DELETE
	@Path("/deleteUser/{userId}")

	public Response deleteUser(@Auth User user, @PathParam("userId") Integer userId) {
		try {
			peopleDAO.deleteUser(userId);
			SuccessMessage success = new SuccessMessage();
			success.setSuccess("Removed the user");
			return Response.ok(success).build();
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
	}

	@GET
	@Path("/summary")
	public List<PeopleSummary> getSummary(@Auth User user) throws SQLException, RedmineException {

		/*
		 * As specified in jdbi-folder, whenever we need results to be folded,
		 * we need to use FoldingList instead of List. Result will be folded.
		 * Just call getValues on FoldingList will give List<?> values.
		 */
		FoldingList<PeopleSummary> foldedResult = peopleDAO.getSummary();
		List<PeopleSummary> peopleSummary = foldedResult.getValues();
		/*
		 * The value that indicates the number of issues(count) assigned to a
		 * person in a sprint isn't retrieved from the database. Instead, we
		 * retrieve the issueIds in the form of a list and then count the length
		 * of the list. So, after the above command retrieves data from the
		 * database (getSummary request) and folds the objects, we set the value
		 * of 'numberofIssues' parameter based on the size of 'List<> of issues'
		 * (Refer 'SprintNameIssues.java' to see the parameters)
		 */
		for (PeopleSummary people : peopleSummary) {

			for (ProjectNameSprints projects : people.getProjects()) {
				for (SprintNameIssues sprints : projects.getSprints()) {
					sprints.setNumberofIssues(sprints.getIssues().size());
				}
			}

		}
		List<People> allocatedPeople = new ArrayList<>();
		List<People> peopleList = peopleDAO.getAll();
		List<People> peopleListCopy = peopleDAO.getAll();

		for (People person : peopleListCopy) {

			for (PeopleSummary people : new ArrayList<>(peopleSummary)) {
				if (people.getPersonId().equals(person.getPersonId())) {

					People newPerson = new People();
					newPerson.setPersonId(people.getPersonId());
					newPerson.setPersonName(people.getPersonName());
					allocatedPeople.add(newPerson);
				}
			}
		}

		boolean restart = false;
		for (int i = 0; i < peopleList.size(); i++) {
			if (restart) {
				i = 0;

			}
			restart = false;
			for (People allocatedPerson : new ArrayList<>(allocatedPeople)) {
				if (peopleList.get(i).getPersonId().equals(allocatedPerson.getPersonId())) {

					peopleList.remove(i);
					restart = true;
					break;

				}
			}
		}

		for (People person : new ArrayList<>(peopleList)) {
			// System.out.println("the name of the person is" +
			// person.personName + "\n");
			PeopleSummary newPerson = new PeopleSummary();
			newPerson.setPersonId(person.getPersonId());
			newPerson.setPersonName(person.getPersonName());
			peopleSummary.add(newPerson);
		}
		return peopleSummary;
	}

	@PUT
	@Path("/summaryOfPeopleInProject")
	public List<PeopleSummary> summaryOfPeopleInProject(@Auth User user, Integer projectId)
			throws SQLException, RedmineException {

		/*
		 * As specified in jdbi-folder, whenever we need results to be folded,
		 * we need to use FoldingList instead of List. Result will be folded.
		 * Just call getValues on FoldingList will give List<?> values.
		 */
		FoldingList<PeopleSummary> foldedResult = peopleDAO.getSummary();
		List<PeopleSummary> peopleSummary = foldedResult.getValues();
		/*
		 * The value that indicates the number of issues(count) assigned to a
		 * person in a sprint isn't retrieved from the database. Instead, we
		 * retrieve the issueIds in the form of a list and then count the length
		 * of the list. So, after the above command retrieves data from the
		 * database (getSummary request) and folds the objects, we set the value
		 * of 'numberofIssues' parameter based on the size of 'List<> of issues'
		 * (Refer 'SprintNameIssues.java' to see the parameters)
		 */
		for (PeopleSummary people : peopleSummary) {
			for (ProjectNameSprints projects : people.getProjects()) {
				for (SprintNameIssues sprints : projects.getSprints()) {
					sprints.setNumberofIssues(sprints.getIssues().size());
				}
			}

		}

		/*
		 * preparing a list by including all project participants (both, people
		 * who have been allocated to sprints and people who haven't been
		 * allocated to any sprint).For this, people who have been allocated to
		 * projects and sprints are selected (allocatedPeople). Then, these
		 * people are removed from the full list of project
		 * participants(peopleList). Finally, the summary list (peopleSummary)is
		 * appended with the details of remaining people from 'peopleList' i.e.
		 * people who haven't been allocated to any sprint.
		 */
		List<People> allocatedPeople = new ArrayList<>();
		List<People> peopleList = peopleDAO.peopleNamesInaProject(projectId);
		for (People person : new ArrayList<>(peopleList)) {
			for (PeopleSummary people : new ArrayList<>(peopleSummary)) {
				if (people.getPersonId().equals(person.getPersonId())) {
					People newPerson = new People();
					newPerson.setPersonId(people.getPersonId());
					newPerson.setPersonName(people.getPersonName());
					// peopleSummary.add(newPerson);
					allocatedPeople.add(newPerson);
				}
			}
		}

		for (People person : new ArrayList<>(peopleList)) {

			for (People allocatedPerson : new ArrayList<>(allocatedPeople)) {
				if (person.getPersonId().equals(allocatedPerson.getPersonId())) {

					for (int i = 0; i < peopleList.size(); i++) {
						People p = peopleList.get(i);
						if (person.getPersonId().equals(p.getPersonId())) {
							peopleList.remove(i);
						}
					}

				}
			}
		}

		for (People person : new ArrayList<>(peopleList)) {
			// System.out.println("the name of the person is" +
			// person.personName + "\n");
			PeopleSummary newPerson = new PeopleSummary();
			newPerson.setPersonId(person.getPersonId());
			newPerson.setPersonName(person.getPersonName());
			newPerson.setProjects(null);
			peopleSummary.add(newPerson);
		}
		
		Collections.sort(peopleSummary, new Comparator<PeopleSummary>() {
		    public int compare(PeopleSummary v1, PeopleSummary v2) {
		        return v1.getPersonName().compareTo(v2.getPersonName());
		    }
		});
		return peopleSummary;
	}

	@GET
	public List<People> getAll(@Auth User user) {
		return peopleDAO.getAll();
	}

	@GET
	@Path("/getPersonNameRoles/{id}")
	public NewPerson getPersonNameRoles(@Auth User user, @PathParam("id") Integer personId) throws RedmineException {
		if (peopleDAO.ifPersonRolesexists(personId)) {
			FoldingList<NewPerson> foldedResult = peopleDAO.getPersonDetailsRoles(personId);
			List<NewPerson> person = foldedResult.getValues();
			NewPerson personDetailsRoles = new NewPerson();
			for (NewPerson newPerson : person) {
				personDetailsRoles.setPersonId(personId);
				personDetailsRoles.setFirstName(newPerson.getFirstName());
				personDetailsRoles.setLastName(newPerson.getLastName());
				personDetailsRoles.setEmailID(newPerson.getEmailID());
				personDetailsRoles.setRoles(newPerson.getRoles());
			}
			return personDetailsRoles;
		} else {
			List<ExistingPerson> plainResult = peopleDAO.getPerson(personId);

			NewPerson personDetailsRoles = new NewPerson();
			for (ExistingPerson newPerson : plainResult) {
				personDetailsRoles.setPersonId(personId);
				personDetailsRoles.setFirstName(newPerson.getFirstName());
				personDetailsRoles.setLastName(newPerson.getLastName());
				personDetailsRoles.setEmailID(newPerson.getEmailID());
			}
			return personDetailsRoles;
		}
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/addPerson")
	public Response addNewPerson(@Auth User user, NewPerson newPerson) throws RedmineException {
		String apiKey;
		try {
			 apiKey = peopleDAO.getApiKeyOfUser(newPerson.getUserId()).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
		com.taskadapter.redmineapi.bean.User newUser = new com.taskadapter.redmineapi.bean.User();
		People addedPerson = new People();
		newUser.setFirstName(newPerson.getFirstName());
		newUser.setLastName(newPerson.getLastName());
		newUser.setFullName(newPerson.getFirstName() + " " + newPerson.getLastName());
		addedPerson.setPersonName(newPerson.getFirstName() + " " + newPerson.getLastName());
		newUser.setLogin(newPerson.getEmailID());
		newUser.setMail(newPerson.getEmailID());
		try {
			com.taskadapter.redmineapi.bean.User createdUser = redmineManager.getUserManager().createUser(newUser);
			addedPerson.setPersonId(createdUser.getId());
			peopleDAO.insertIntoPeopleTable(createdUser.getId(), createdUser.getFullName(), createdUser.getFirstName(),
					createdUser.getLastName(), createdUser.getMail());
			for (Roles roles : newPerson.getRoles()) {
				try {
					peopleDAO.insertIntoRolesOfPeopleTable(createdUser.getId(), roles.getRoleId());
				} catch (Exception e) {
					System.out.println(e.toString());
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			}
		} catch (RedmineException e) {
			System.out.println(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

		return Response.ok(addedPerson).build();
	}

	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/updateUserDetails")
	public Response updateUserDetails(@Auth User curentUser, UserTemplate user) throws RedmineException {
		if (peopleDAO.ifUserExists(user.getUserId()) == true) {

			try {
				peopleDAO.UpdateUserDetails(user);
				SuccessMessage success = new SuccessMessage();
				success.setSuccess("update successful");
				return Response.ok(success).build();
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}

		}
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/updateUserPassword")
	public Response updateUserPassword(@Auth User currentUser, String encoded)
			throws RedmineException, ClassNotFoundException, IOException {
		byte[] obj = Base64.getDecoder().decode(encoded);

		ObjectMapper mapper = new ObjectMapper();
		String str = new String(obj, Charset.forName("UTF-8"));
		UserPasswordChange user = mapper.readValue(str, UserPasswordChange.class);

		if (peopleDAO.ifCurrentPasswordExists(user.getUserId(), user.getUserName(),
				user.getCurrentPassword()) == true) {

			try {
				peopleDAO.updateUserPassword(user);
				SuccessMessage success = new SuccessMessage();
				success.setSuccess("update successful");
				return Response.ok(success).build();
			} catch (Exception e) {
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).entity(e).build();
			}

		} else {
			ErrorMessage error = new ErrorMessage();
			error.setError("Your current password is wrong");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

	}

	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/updatePerson")
	public Response updatePerson(@Auth User user, NewPerson newPerson) throws RedmineException {
		String apiKey;
		try {
			 apiKey = peopleDAO.getApiKeyOfUser(newPerson.getUserId()).get(0);
		} catch (Exception e1) {
			System.out.println(e1);
			return Response.status(Status.BAD_REQUEST).entity(e1).build();
		}
		RedmineManager redmineManager = RedmineManagerFactory.createWithApiKey(redmineUrl, apiKey);
		com.taskadapter.redmineapi.bean.User newUser = new com.taskadapter.redmineapi.bean.User(
				newPerson.getPersonId());

		newUser.setFirstName(newPerson.getFirstName());
		newUser.setLastName(newPerson.getLastName());
		newUser.setFullName(newPerson.getFirstName() + " " + newPerson.getLastName());
		newUser.setLogin(newPerson.getEmailID());
		newUser.setMail(newPerson.getEmailID());
		try {
			redmineManager.getUserManager().update(newUser);
			String fullName = newPerson.getFirstName() + " " + newPerson.getLastName();
			if (peopleDAO.ifPersonIdExists(newPerson.getPersonId())) {
				peopleDAO.updatePerson(newPerson.getPersonId(), fullName, newPerson.getFirstName(),
						newPerson.getLastName(), newPerson.getEmailID());
			}

			if (newPerson.getRoles().size() != 0) {
				peopleDAO.deleteRolesOfPerson(newPerson.getPersonId());
			}
			for (Roles roles : newPerson.getRoles()) {
				try {
					if (peopleDAO.ifPersonAssignedRole(newPerson.getPersonId(), roles.getRoleId()) == false) {
						peopleDAO.insertIntoRolesOfPeopleTable(newPerson.getPersonId(), roles.getRoleId());
					}
				} catch (Exception e) {
					System.out.println(e.toString());
					return Response.status(Status.BAD_REQUEST).entity(e).build();
				}
			}
		} catch (RedmineException e) {
			System.out.println(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

		return Response.ok(newPerson).build();
	}
}
