package pl.java.sso;

import org.josso.Lookup;
import org.josso.SecurityDomain;
import org.josso.auth.Authenticator;
import org.josso.auth.Credential;
import org.josso.auth.SimplePrincipal;
import org.josso.auth.exceptions.AuthenticationFailureException;
import org.josso.auth.exceptions.SSOAuthenticationException;
import org.josso.gateway.MutableSSOContext;
import org.josso.gateway.SSOContext;
import org.josso.gateway.SSOException;
import org.josso.gateway.assertion.AssertionManager;
import org.josso.gateway.assertion.AuthenticationAssertion;
import org.josso.gateway.assertion.exceptions.AssertionNotValidException;
import org.josso.gateway.event.security.SSOSecurityEventManager;
import org.josso.gateway.identity.exceptions.IdentityProvisioningException;
import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.josso.gateway.identity.service.SSOIdentityManager;
import org.josso.gateway.identity.service.SSOIdentityProvider;
import org.josso.gateway.session.SSOSession;
import org.josso.gateway.session.exceptions.NoSuchSessionException;
import org.josso.gateway.session.exceptions.SSOSessionException;
import org.josso.gateway.session.service.SSOSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Set;

/**
 * Default SSO identity provider.
 */
public class DefaultSSOIdentityProvider implements SSOIdentityProvider {

    private static final Logger log = LoggerFactory.getLogger(DefaultSSOIdentityProvider.class);

    private static final String BASIC_AUTHENTICATION = "basic-authentication";

    public void initialize() {
    }

    /**
     * Request an authentication assertion using simple authentication through the
     * supplied username/password credentials.
     *
     * @param username - username
     * @param password - password
     * @return the assertion identifier
     */
    public String assertIdentityWithSimpleAuthentication(String username, String password)
            throws IdentityProvisioningException {
        try {
            Credential cUsername = newCredential(BASIC_AUTHENTICATION, "username", username);
            Credential cPassword = newCredential(BASIC_AUTHENTICATION, "password", password);
            Credential[] c = {cUsername, cPassword};
            // Perform the assertion and open the corresponding SSO session for the user
            // in case successful
            SSOSession userSsoSession = login(c, BASIC_AUTHENTICATION);
            // Return the assertion identifier which maps to the new session
            AssertionManager am = Lookup.getInstance().lookupAssertionManager();
            AuthenticationAssertion aa = am.requestAssertion(userSsoSession.getId());
            return aa.getId();
        } catch (SSOAuthenticationException e) {
            throw new IdentityProvisioningException("Failed to assert identity of user: " + username);
        } catch (SSOException e) {
            throw new IdentityProvisioningException("Error asserting identity of user: " + username);
        } catch (Exception e) {
            throw new IdentityProvisioningException("Unknown error asserting identity of user: " + username);
        }
    }

    /**
     * Resolves an authentication assertion given its identifier.
     */
    public String resolveAuthenticationAssertion(String authenticationAssertionId) throws IdentityProvisioningException {
        try {
            AssertionManager am = Lookup.getInstance().lookupAssertionManager();
            AuthenticationAssertion aa = am.consumeAssertion(authenticationAssertionId);
            if (aa == null) {
                throw new AssertionNotValidException(authenticationAssertionId);
            }
            return aa.getSSOSessionId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IdentityProvisioningException("Error dereferencing authentication assertion: " +
                    authenticationAssertionId, e
            );
        }
    }

    public void globalSignoff(String sessionId) throws IdentityProvisioningException {
        try {
            MutableSSOContext ctx = (MutableSSOContext) SSOContext.getCurrent();
            SecurityDomain domain = ctx.getSecurityDomain();
            SSOSessionManager sm = domain.getSessionManager();
            SSOSession session = sm.getSession(sessionId);
            ctx.setCurrentSession(session);
            ctx.setUserLocation("remote-application");
            ctx.setScheme(BASIC_AUTHENTICATION);
            logout();
        } catch (SSOException e) {
            throw new IdentityProvisioningException("Error signing off user with sessin: " + sessionId);
        } catch (Exception e) {
            throw new IdentityProvisioningException("Unknown error signing off user with session: " + sessionId);
        }
    }

    /**
     * This method logins a user into de SSO infrastructure.
     *
     * @param cred   the user credentials used as user identity proof.
     * @param scheme the authentication scheme name to be used for logging in the user.
     * @throws org.josso.auth.exceptions.AuthenticationFailureException
     *                      if authentication fails.
     * @throws SSOException if an error occurs.
     */
    public SSOSession login(Credential[] cred, String scheme) throws SSOException, SSOAuthenticationException {
        SSOContext ctx = SSOContext.getCurrent();
        try {
            SecurityDomain domain = Lookup.getInstance().lookupSecurityDomain();
            // Configure this ...!
            SSOIdentityManager im = domain.getIdentityManager();
            SSOSessionManager sm = domain.getSessionManager();
            Authenticator au = domain.getAuthenticator();
            // 1. Invalidate current session
            SSOSession currentSession = ctx.getSession();
            if (currentSession != null) {
                try {
                    log.debug("Invalidating existing session: " + currentSession.getId());
                    sm.invalidate(currentSession.getId());
                } catch (Exception e) {
                    log.warn("Can't ivalidate current session: " + currentSession.getId() + "\n" + e.getMessage(), e);
                }
            }
            // 2. Authenticate using credentials :
            Subject s = au.check(cred, scheme);
            Set principals = s.getPrincipals(SimplePrincipal.class);
            if (principals.size() != 1) {
                // The Set should NEVER be empty or have more than one Principal ...
                // In the future, we could have more than one principal if authenticated with multiple schemes.
                throw new SSOException("Assertion failed : principals.size() != 1");
            }
            // 3. Find SSO User, authentication was successfull and we have only one principal
            // Check the username with the IdentityManager, just to be sure it's a valid user:
            Principal p = (Principal) principals.iterator().next();
            im.userExists(p.getName());
            // 4. Create a new sso session :
            String ssoSessionId = sm.initiateSession(p.getName());
            SSOSession session = sm.getSession(ssoSessionId);
            notifyLoginSuccess(session.getUsername(), session, scheme);
            return session;
        } catch (AuthenticationFailureException e) {
            log.debug(e.getMessage(), e);
            // Re-throw current exception ...
            notifyLoginFailed(cred, scheme, e);
            throw e;
        } catch (SSOAuthenticationException e) {
            log.debug(e.getMessage(), e);
            // Re-throw current exception ...
            notifyLoginFailed(cred, scheme, e);
            throw e;
        } catch (SSOIdentityException e) {
            log.debug(e.getMessage(), e);
            notifyLoginFailed(cred, scheme, e);
            throw new SSOException(e.getMessage(), e);
        } catch (SSOSessionException e) {
            log.debug(e.getMessage(), e);
            notifyLoginFailed(cred, scheme, e);
            throw new SSOException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            notifyLoginFailed(cred, scheme, e);
            throw new SSOException(e.getMessage(), e);
        }
    }

    /**
     * Create an authentication assertion based on the supplied credentials. If assertion is successful a new session
     * is created for the subject which can be referenced through the corresponding assertion identifier.
     *
     * @param credentials
     * @param scheme
     * @return
     * @throws AuthenticationFailureException if authentication fails
     * @throws SSOException
     * @throws SSOAuthenticationException
     */
    public AuthenticationAssertion assertIdentity(Credential[] credentials, String scheme) throws SSOException, SSOAuthenticationException {
        try {
            SSOSession session = login(credentials, scheme);
            AssertionManager assertionManager = Lookup.getInstance().lookupAssertionManager();
            return assertionManager.requestAssertion(session.getId());
        } catch (AuthenticationFailureException e) {
            throw e;
        } catch (SSOAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // TODO : Notify assertion failed event
            throw new SSOException(e.getMessage(), e);
        }
    }

    /**
     * Create an authentication assertion from a previous existing and valid one.
     *
     * @param sessionId SSO session identifier for the session to be bound to the new assertion.
     * @return
     * @throws SSOException
     */
    public AuthenticationAssertion assertIdentity(String sessionId) throws SSOException {
        try {
            SSOSessionManager sm = Lookup.getInstance().lookupSecurityDomain().getSessionManager();
            AssertionManager assertionManager = Lookup.getInstance().lookupAssertionManager();
            SSOSession session = sm.getSession(sessionId);
            return assertionManager.requestAssertion(session.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // TODO: Notify assertion failed event
            throw new SSOException(e.getMessage(), e);
        }

    }

    /**
     * Logouts a user from the SSO infrastructure.
     *
     * @throws SSOException
     */
    public void logout() throws SSOException {
        SSOContext ctx = SSOContext.getCurrent();
        SSOSession session = ctx.getSession();
        if (session == null) return;
        String ssoSessionId = session.getId();
        try {
            SecurityDomain domain = Lookup.getInstance().lookupSecurityDomain();
            SSOSessionManager sm = domain.getSessionManager();
            sm.invalidate(ssoSessionId);
            notifyLogoutSuccess(session);
        } catch (NoSuchSessionException e) {
            // Ignore this ....
            log.debug("[logout()] Session is not valid: " + ssoSessionId);
        } catch (SSOSessionException e) {
            log.error(e.getMessage(), e);
            notifyLogoutFail(e);
            throw new SSOException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            notifyLogoutFail(e);
            throw new SSOException(e.getMessage(), e);
        }
    }

    protected void notifyLoginFailed(Credential[] credentials, String scheme, Throwable error) {
        SSOContext ctx = SSOContext.getCurrent();
        try {
            // We expect a spetial Event Manager ...
            SSOSecurityEventManager em = (SSOSecurityEventManager) Lookup.getInstance().lookupSecurityDomain().getEventManager();
            em.fireAuthenticationFailureEvent(ctx.getUserLocation(), scheme, credentials, error);
        } catch (Exception e) {
            log.error("Can't notify login failure: " + e.getMessage(), e);
        }
    }

    protected void notifyLoginSuccess(String username, SSOSession session, String scheme) {
        SSOContext ctx = SSOContext.getCurrent();
        try {
            // We expect a spetial Event Manager ...
            SSOSecurityEventManager em = (SSOSecurityEventManager) Lookup.getInstance().lookupSecurityDomain().getEventManager();
            em.fireAuthenticationSuccessEvent(ctx.getUserLocation(), scheme, username, session.getId());
        } catch (Exception e) {
            log.error("Can't notify login success: " + e.getMessage(), e);
        }
    }

    private void notifyLogoutFail(Throwable error) {
        SSOContext ctx = SSOContext.getCurrent();
        try {
            // We expect a spetial Event Manager ...
            SSOSecurityEventManager em = (SSOSecurityEventManager) Lookup.getInstance().lookupSecurityDomain().getEventManager();
            em.fireLogoutFailureEvent(ctx.getUserLocation(), ctx.getSession().getUsername(), ctx.getSession().getId(), error);
        } catch (Exception e) {
            log.error("Can't notify logout failure: " + e.getMessage(), e);
        }
    }

    protected void notifyLogoutSuccess(SSOSession session) {
        SSOContext ctx = SSOContext.getCurrent();
        try {
            // We expect a spetial Event Manager ...
            SSOSecurityEventManager em = (SSOSecurityEventManager) Lookup.getInstance().lookupSecurityDomain().getEventManager();
            em.fireLogoutSuccessEvent(ctx.getUserLocation(), session.getUsername(), session.getId());
        } catch (Exception e) {
            log.error("Can't notify logout success: " + e.getMessage(), e);
        }
    }

    protected Credential newCredential(String schemeName, String name, Object value) throws SSOAuthenticationException {
        try {
            SecurityDomain domain = Lookup.getInstance().lookupSecurityDomain();
            Authenticator au = domain.getAuthenticator();
            return au.newCredential(schemeName, name, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
