package pl.java.sso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.josso.gateway.GatewayServiceLocator;
import org.josso.gateway.WebserviceGatewayServiceLocator;
import org.josso.gateway.identity.SSORole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class JOSSOAuthentication implements Authentication {

	private String id; //SSOSessionID
	private GatewayServiceLocator gsl;
	private boolean authenticated = true;

	public JOSSOAuthentication(String sid) {
		id = sid;
		WebserviceGatewayServiceLocator wgsl = new WebserviceGatewayServiceLocator();
		wgsl.setEndpoint("localhost:8080");
		wgsl.setServicesWebContext("sso");
		gsl = wgsl;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		try {
			for (SSORole role : gsl.getSSOIdentityManager().findRolesBySSOSessionId(id)) {
				roles.add(new GrantedAuthorityImpl(role.getName()));
			}
		} catch (Exception ex) {
			Logger.getLogger(JOSSOAuthentication.class.getName()).log(Level.SEVERE, null, ex);
		}
		return roles;
	}

	public Object getCredentials() {
		return null;
	}

	public Object getDetails() {
		return null;
	}

	public Object getPrincipal() {
		try {
			return gsl.getSSOIdentityManager().findUserInSession(id);
		} catch (Exception ex) {
			Logger.getLogger(JOSSOAuthentication.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean bln) throws IllegalArgumentException {
		authenticated = bln;
	}

	public String getName() {
		return id;
	}
}
