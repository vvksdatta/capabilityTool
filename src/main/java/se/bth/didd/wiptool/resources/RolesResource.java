package se.bth.didd.wiptool.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import se.bth.didd.wiptool.api.ErrorMessage;
import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeople;
import se.bth.didd.wiptool.db.RolesDAO;

@Path("/roles")
@Produces(APPLICATION_JSON)
public class RolesResource {

	private RolesDAO rolesDAO;

	public RolesResource(RolesDAO rolesDAO) {
		super();
		this.rolesDAO = rolesDAO;
	}

	@Path("/getRoles")
	@GET
	public List<Roles> getRolesList() {
		return rolesDAO.getRolesList();
	}

	@Path("/rolesOfPeople")
	@GET
	public Response rolesOfPeople() {
		try {
			List<RolesOfPeople> rolesOfPeople = rolesDAO.getRolesOfPeople();
			Collections.sort(rolesOfPeople, new Comparator<RolesOfPeople>() {
			    public int compare(RolesOfPeople v1, RolesOfPeople v2) {
			        return v1.getPersonName().compareTo(v2.getPersonName());
			    }
			});
			return Response.ok(rolesOfPeople).build();
		} catch (Exception e) {
			System.out.println(e);
			ErrorMessage error = new ErrorMessage();
			error.setError(e.toString());
			return Response.status(Status.BAD_REQUEST).entity(error).build();

		}
	}

}