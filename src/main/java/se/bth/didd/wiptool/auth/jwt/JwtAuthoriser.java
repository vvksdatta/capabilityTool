package se.bth.didd.wiptool.auth.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.auth.Authorizer;

/**
 * Determines if a user is authorised to access an API endpoint, after they were authenticated with {@link JwtAuthenticator}.
 *

 */
public class JwtAuthoriser implements Authorizer<User> {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthoriser.class);

	@Override
	public boolean authorize(User exampleUser, String requiredRole) {
		if (exampleUser == null) {
			LOGGER.warn("msg=user object was null");
			return false;
		}

		String roles = exampleUser.getRole();
		if (roles == null) {
			LOGGER.warn("msg=roles were null, user={}, userId={}", exampleUser.getName(), exampleUser.getId());
			return false;
		}
		return roles.contains(requiredRole);
	}
}