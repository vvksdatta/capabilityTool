package se.bth.didd.wiptool.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.jersey.caching.CacheControl;
import se.bth.didd.wiptool.api.Login;
import se.bth.didd.wiptool.api.LoginFullName;
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.auth.Secrets;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.db.AuthDAO;
import se.bth.didd.wiptool.resources.dto.LoginResponse;

/**
 * Exchanges login information for a JWT token.
 */

@Path("auth")
@Produces(APPLICATION_JSON)
public class LoginResource {

	private AuthDAO authDAO;

	public LoginResource(AuthDAO authDAO) {
		this.authDAO = authDAO;
	}

	@GET
	@Path("/login")
	@CacheControl(noCache = true, noStore = true, mustRevalidate = true, maxAge = 0)

	public final Response doLogin(@Auth PrincipalImpl user) throws JoseException {
		People newUser = new People();
		List<Login> person = authDAO.getUser(user.getName());
		for (Login client : person) {
			newUser.setPersonId(client.getUserId());
			newUser.setPersonName(client.getUserFirstName());
			newUser.setRole(client.getRole());

		}

		return Response.ok(new LoginResponse(buildToken(user, newUser.getRole()).getCompactSerialization(),
				newUser.getPersonName(), newUser.getPersonId(), newUser.getRole())).build();
	}

	/*
	 * @GET
	 * 
	 * @Path("/updatedUserDetails/{id}")
	 * 
	 * public Response updatedUserDetails(@Auth User user, @PathParam("id")
	 * Integer userId) throws JoseException { People newUser = new People();
	 * List<Login> person = authDAO.getUserById(userId); for (Login client :
	 * person) { newUser.setPersonId(client.getUserId());
	 * newUser.setPersonName(client.getUserFirstName());
	 * newUser.setRole(client.getRole());
	 * 
	 * }
	 * 
	 * return Response.ok(newUser).build(); }
	 */
	@RolesAllowed({ UserRoles.ROLE_ONE })
	@PUT
	@Path("/register")
	public Response registerNewUser(@Auth User user, LoginFullName login) {

		if (authDAO.ifCredentialsExists(login.getUserMailId(), login.getUserName().toLowerCase()) != true) {
			authDAO.insertIntoLoginCredentials(login);
			SuccessMessage success = new SuccessMessage();
			success.setSuccess("Successfully created new user " + login.getUserName());
			return Response.ok(success).build();

		}

		else {
			return Response.status(Status.BAD_REQUEST).entity("Email or user name already registered!").build();
		}
	}

	public Response registerNewDefaultUser(Login login) {

		if (authDAO.ifCredentialsExists(login.getUserMailId(), login.getUserName().toLowerCase()) != true) {
			authDAO.insertIntoLoginCredentials(login);
			SuccessMessage success = new SuccessMessage();
			success.setSuccess("Successfully created new user " + login.getUserName());
			return Response.ok(success).build();

		}

		else {
			return Response.status(Status.BAD_REQUEST).entity("Email or user name already registered!").build();
		}
	}

	/* token builder */
	private JsonWebSignature buildToken(PrincipalImpl user, String role) {
		// These claims would be tightened up for production

		final JwtClaims claims = new JwtClaims();
		claims.setSubject("1");
		claims.setStringClaim("roles", role);
		claims.setStringClaim("user", user.getName());
		claims.setIssuedAtToNow();
		claims.setGeneratedJwtId();
		claims.setExpirationTimeMinutesInTheFuture(87658);
		final JsonWebSignature jws = new JsonWebSignature();
		jws.setPayload(claims.toJson());
		jws.setAlgorithmHeaderValue(HMAC_SHA256);
		jws.setKey(new HmacKey(Secrets.JWT_SECRET_KEY));
		return jws;
	}

}