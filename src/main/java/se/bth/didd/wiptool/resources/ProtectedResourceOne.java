package se.bth.didd.wiptool.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.auth.Auth;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.db.AuthDAO;
import se.bth.didd.wiptool.resources.dto.ProtectedResourceResponse;

@Path("/protectedResourceOne")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProtectedResourceOne {

	AuthDAO authDAO;
	
	public ProtectedResourceOne(AuthDAO authDAO) {
		super();
		this.authDAO = authDAO;
	}

	@GET
	@RolesAllowed({ UserRoles.ROLE_ONE })
	public ProtectedResourceResponse getAuthProcessingTime(@Auth User user) throws Exception {

			return new ProtectedResourceResponse(user.getRoles(), user.getName());

	}
}