package se.bth.didd.wiptool.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.taskadapter.redmineapi.MembershipManager;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Role;
import com.taskadapter.redmineapi.bean.RoleFactory;

import se.bth.didd.wiptool.api.Roles;
import se.bth.didd.wiptool.api.RolesOfPeople;
import se.bth.didd.wiptool.db.RolesDAO;

@Path("/roles")
@Produces(APPLICATION_JSON)
public class RolesResource {

	RolesDAO rolesDAO;
	private String redmineUrl;
	private String apiAccessKey;
	
	public RolesResource(RolesDAO rolesDAO, String redmineUrl, String apiAccessKey) {
		super();
		this.rolesDAO = rolesDAO;
		this.redmineUrl = redmineUrl;
		this.apiAccessKey = apiAccessKey;
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