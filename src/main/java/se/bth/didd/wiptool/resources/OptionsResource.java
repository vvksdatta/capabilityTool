package se.bth.didd.wiptool.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.taskadapter.redmineapi.RedmineException;

import io.dropwizard.auth.Auth;
import se.bth.didd.wiptool.api.OptionsTemplate;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.db.OptionsDAO;


@Path("/options")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

/*
 * This servers as the main API resource for setting options
 */
public class OptionsResource {

	private OptionsDAO optionsDAO;

	/*
	 * This constructor is invoked in the run method of the main application and
	 * it maps the redmine configuration values to the variables 'redmineUrl'
	 * and 'apiAcessKey'.
	 */

	public OptionsResource(OptionsDAO optionsDAO) {
		this.optionsDAO = optionsDAO;
	}

	@GET
	@Path("/statusOfOptions")
	public Response getListOfOptions() {

		try {
			List<OptionsTemplate> currentOptionsSetting = optionsDAO.selectSettingsOfAllOptions();
			for (OptionsTemplate setOfOptions : currentOptionsSetting) {
				OptionsTemplate currentSetOfOptions = setOfOptions;
				return Response.ok(currentSetOfOptions).build();
			}
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@POST
	@Path("/updateOptions")
	public Response updateOptions(@Auth User user, OptionsTemplate updatedOptions) throws RedmineException {

		try {
			if (optionsDAO.ifOptionsAdded()) {
				optionsDAO.updateAllOptions(updatedOptions);
			}

		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}

		return Response.ok(updatedOptions).build();
	}
	
	
	@POST
	@Path("/insertDefaultOptions")
	public Response insertDefaultOptions() {

		try {
			if (optionsDAO.ifOptionsAdded() != true) {
				OptionsTemplate defaultOptions = new OptionsTemplate();
				defaultOptions.setAddNewProject(0);
				defaultOptions.setAddNewPerson(0);
				defaultOptions.setAddNewSprint(1);

				optionsDAO.insertIntoOptionsTable(defaultOptions);
			}
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e).build();
		}
		SuccessMessage success = new SuccessMessage();
		success.setSuccess("success");
		return Response.ok(success).build();
	}

}
