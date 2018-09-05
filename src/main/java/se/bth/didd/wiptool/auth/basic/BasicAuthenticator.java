package se.bth.didd.wiptool.auth.basic;

import java.io.IOException;
import java.util.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentials;
import se.bth.didd.wiptool.auth.Secrets;
import se.bth.didd.wiptool.db.AuthDAO;

/**
 * Validates credentials for basic auth on login in 
 */
public class BasicAuthenticator implements Authenticator<BasicCredentials, PrincipalImpl> {
	AuthDAO authDAO;
	

	@Override
	public Optional<PrincipalImpl> authenticate(BasicCredentials credentials) throws AuthenticationException {
		try {
			if (isValidCredentials(credentials)) {
				return Optional.of(new PrincipalImpl(credentials.getUsername()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return Optional.empty();
	}

	/*private boolean isValidCredentials(BasicCredentials credentials) {
		return Secrets.LOGIN_USERNAME.equals(credentials.getUsername()) && (Secrets.LOGIN_PASSWORD.equals(credentials.getPassword()));
	}*/
	
	private boolean isValidCredentials(BasicCredentials credentials) throws IOException {
		//Secrets.test();
		/*try {
			authDAO.createLoginCredentialsTable2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*///System.out.println("hello"+ credentials.getUsername() + credentials.getPassword());
		Secrets secret = new Secrets();
		return secret.test(credentials.getUsername(), credentials.getPassword()); 	
	}
}