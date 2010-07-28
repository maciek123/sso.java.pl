package pl.java.sso;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.josso.gateway.WebserviceGatewayServiceLocator;
import org.josso.gateway.identity.SSOUser;
import org.josso.gateway.identity.exceptions.IdentityProvisioningException;
import org.josso.gateway.identity.exceptions.NoSuchUserException;
import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.josso.gateway.identity.service.SSOIdentityProviderService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class SSOAuthenticationProvider implements AuthenticationProvider {

	SSOIdentityProviderService ips;
	WebserviceGatewayServiceLocator wgsl = new WebserviceGatewayServiceLocator();

	public SSOAuthenticationProvider() {
		try {
			wgsl.setEndpoint("localhost:8080");
			wgsl.setServicesWebContext("epg-sso-gateway");
			ips = wgsl.getSSOIdentityProvider();
		} catch (Exception ex) {
			Logger.getLogger(SSOAuthenticationProvider.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public Authentication authenticate(Authentication a) throws AuthenticationException {
		String id = null;
		if (!a.isAuthenticated()) {
			try {
				ips.assertIdentityWithSimpleAuthentication("josso", a.getPrincipal().toString(), a.getCredentials().toString());
				try {
					id = ((WebAuthenticationDetails) a.getDetails()).getSessionId();
					SSOUser fUser = wgsl.getSSOIdentityManager().findUser("josso", a.getPrincipal().toString());
					SSOUser user = wgsl.getSSOIdentityManager().findUserInSession(id);
				} catch (Exception ex) {
					Logger.getLogger(SSOAuthenticationProvider.class.getName()).log(Level.SEVERE, null, ex);
				}
			} catch (IdentityProvisioningException e) {
				throw new AuthenticationException("error!") {
				};
			}
		}
		return a;
	}

	@Override
	public boolean supports(Class<? extends Object> type) {
		return true;
	}
}
