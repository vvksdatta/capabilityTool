package se.bth.didd.wiptool.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	public List<RolesOfPeople> rolesOfPeople() {
		return rolesDAO.getRolesOfPeople();
	}

}