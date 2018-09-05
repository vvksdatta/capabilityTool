package se.bth.didd.wiptool.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import se.bth.didd.wiptool.api.People;
import se.bth.didd.wiptool.api.SuccessMessage;
import se.bth.didd.wiptool.auth.Secrets;
import se.bth.didd.wiptool.auth.jwt.UserRoles;
import se.bth.didd.wiptool.db.AuthDAO;
import se.bth.didd.wiptool.resources.dto.LoginResponse;

/**
 * Exchanges login information for a JWT token.
 *
 * @author Hendrik van Huyssteen
 * @since 09 Aug 2017
 */

@Path("auth")
@Produces(APPLICATION_JSON)
public class LoginResource {

	AuthDAO authDAO;

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

		}

		return Response.ok(new LoginResponse(buildToken(user).getCompactSerialization(), newUser.getPersonName(),
				newUser.getPersonId())).build();
	}

	@PUT
	@Path("/register")
	public Response registerNewUser(Login login) {

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

	/**
	 * Example token builder. This would be handled by some role mapping system
	 * in production.
	 *
	 * @return Example token with the {@link UserRoles#ROLE_ONE} role.
	 */
	private JsonWebSignature buildToken(PrincipalImpl user) {
		// These claims would be tightened up for production

		final JwtClaims claims = new JwtClaims();
		claims.setSubject("1");
		claims.setStringClaim("roles", UserRoles.ROLE_ONE);
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