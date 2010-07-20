package pl.java.sso;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.josso.gateway.WebserviceGatewayServiceLocator;
import org.josso.gateway.identity.exceptions.IdentityProvisioningException;
import org.josso.gateway.identity.service.SSOIdentityProviderService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class SSOAuthenticationProvider implements AuthenticationProvider {

	SSOIdentityProviderService ips;

	public SSOAuthenticationProvider() {
		try {
			WebserviceGatewayServiceLocator wgsl = new WebserviceGatewayServiceLocator();
			wgsl.setEndpoint("localhost:8080/epg-sso-gateway");
			ips = wgsl.getSSOIdentityProvider();
		} catch (Exception ex) {
			Logger.getLogger(SSOAuthenticationProvider.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public Authentication authenticate(Authentication a) throws AuthenticationException {
		if (!a.isAuthenticated()) {
			System.out.println(a.getCredentials());
			System.out.println(a.getPrincipal());
			try {
				System.out.println("..");
				System.out.print(ips.assertIdentityWithSimpleAuthentication(a.getPrincipal().toString(), a.getCredentials().toString(), a.getCredentials().toString()));
			} catch (IdentityProvisioningException e) {
				System.out.println(e.getMessage());
				throw new AuthenticationException("invalid username/password") {
				};
			}
		} else {
			System.out.println("hej!");
		}
		return a;
	}

	@Override
	public boolean supports(Class<? extends Object> type) {
		return true;
	}
}
