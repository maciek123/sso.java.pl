package pl.java.sso;


import org.josso.gateway.GatewayServiceLocator;
import org.josso.gateway.identity.SSORole;
import org.josso.gateway.identity.exceptions.NoSuchUserException;
import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

public class JOSSOAuthenticationProvider implements AuthenticationProvider {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(JOSSOAuthenticationProvider.class);
	private GatewayServiceLocator gsl;
	@Autowired
	private UserDetailsService authorizationService;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JOSSOAuthenticationToken auth = (JOSSOAuthenticationToken) authentication;
		String jossoSessionId = auth.getJossoSessionId();
		try {
			Principal principal = gsl.getSSOIdentityManager().findUserInSession(jossoSessionId);
			if (principal == null) {
				log.debug("No principal found for JOSSO Session ID: {}", StringUtils.quote(jossoSessionId));
				return null;
			}
			log.debug("Principal found for JOSSO Session ID: {} (principal: {})", StringUtils.quote(jossoSessionId), principal);
			UserDetails details = retrieveUser(jossoSessionId, principal.getName(), auth);
			log.debug("Roles of principal " + principal + ":" + StringUtils.arrayToCommaDelimitedString(details.getAuthorities()));

			return createSuccessAuthentication(auth, details);
		} catch (NoSuchUserException e) {
			log.info("Unable to find user in session: {}", jossoSessionId);
			throw new UsernameNotFoundException("Unable to find user in session " + jossoSessionId, e);
		} catch (SSOIdentityException e) {
			log.info("Unable to get user from session: {} because: {}", jossoSessionId, e.getMessage());
			throw new AuthenticationServiceException("Unable to get user in session " + jossoSessionId, e);
		} catch (Exception e) {
			throw new AuthenticationServiceException("Unable to get user in session " + jossoSessionId, e);
		}
	}

	private Authentication createSuccessAuthentication(JOSSOAuthenticationToken auth, UserDetails details) {
		JOSSOAuthenticationToken rv = new JOSSOAuthenticationToken(auth.getJossoSessionId(), details.getAuthorities());
		rv.setDetails(details);
		rv.setAuthenticated(true);
		return rv;
	}

	protected UserDetails retrieveUser(String jossoSessionId, String username, JOSSOAuthenticationToken authentication) throws Exception, SSOIdentityException {
		if (authorizationService == null) {
			return getSSOUser(jossoSessionId, username, authentication);
		} else {
			return authorizationService.loadUserByUsername(username);
		}
	}

	private UserDetails getSSOUser(String jossoSessionId, String username, JOSSOAuthenticationToken authentication) throws Exception {
		SSORole[] roles = gsl.getSSOIdentityManager().findRolesBySSOSessionId(jossoSessionId);
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if (roles != null) {
			for (SSORole role : roles) {
				authorities.add(new GrantedAuthorityImpl(role.getName().toUpperCase()));
			}
		}
		if (authentication.getAuthorities().length > 0) {
			for (int i = 0; i < authentication.getAuthorities().length; i++) {
				authorities.add(authentication.getAuthorities()[i]);
			}
		}
		return new User(username, "", true, true, true, true, authorities.toArray(new GrantedAuthority[authorities.size()]));
	}

	@Required
	public void setGatewayServiceLocator(GatewayServiceLocator gsl) throws Exception {
		this.gsl = gsl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class authentication) {
		return JOSSOAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
