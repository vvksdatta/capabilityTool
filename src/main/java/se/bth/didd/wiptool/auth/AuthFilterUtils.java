package se.bth.didd.wiptool.auth;

import javax.ws.rs.core.Response;

import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import se.bth.didd.wiptool.auth.basic.BasicAuthenticator;
import se.bth.didd.wiptool.auth.jwt.User;
import se.bth.didd.wiptool.auth.jwt.JwtAuthenticator;
import se.bth.didd.wiptool.auth.jwt.JwtAuthoriser;


/**
 * Auth filter utilities used elsewhere, just to keep all the auth config nice and central.
 *
 * @author Hendrik van Huyssteen
 * @since 21 Sep 2017
 */
public class AuthFilterUtils {


	public BasicCredentialAuthFilter<PrincipalImpl> buildBasicAuthFilter() {
		return new BasicCredentialAuthFilter.Builder<PrincipalImpl>().setAuthenticator(new BasicAuthenticator()).setPrefix("Basic")
				.setUnauthorizedHandler((s, s1) -> Response.status(Response.Status.BAD_REQUEST).build())
				.buildAuthFilter();
	}

	public AuthFilter<JwtContext, User> buildJwtAuthFilter() {
		// These requirements would be tightened up for production use
		final JwtConsumer consumer = new JwtConsumerBuilder().setAllowedClockSkewInSeconds(300).setRequireSubject()
				.setVerificationKey(new HmacKey(Secrets.JWT_SECRET_KEY)).build();

		return new JwtAuthFilter.Builder<User>().setJwtConsumer(consumer).setRealm("realm").setPrefix("Bearer")
				.setUnauthorizedHandler((s, s1) -> Response.status(Response.Status.UNAUTHORIZED).build())
				.setAuthenticator(new JwtAuthenticator()).setAuthorizer(new JwtAuthoriser()).buildAuthFilter();
	}
}
