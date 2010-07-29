package pl.java.sso;

import org.josso.gateway.GatewayServiceLocator;
import org.josso.gateway.assertion.exceptions.AssertionNotValidException;
import org.josso.gateway.identity.exceptions.IdentityProvisioningException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.FilterChainOrder;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationCredentialsNotFoundException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.GrantedAuthority;

public class JOSSOAuthenticationProcessingFilter extends AbstractProcessingFilter {

    private static final Logger log = LoggerFactory.getLogger(JOSSOAuthenticationProcessingFilter.class);

    private static final String JOSSO_SECURITY_CHECK_URI = "/josso_security_check";

    private GatewayServiceLocator gsl;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
        String assertionId = request.getParameter("josso_assertion_id");
        if (!StringUtils.hasText(assertionId)) {
            throw new AuthenticationCredentialsNotFoundException("HTTP parameter josso_assertion_id is missing or empty");
        }
        log.debug("josso_security_check received for uri: {} assertion id {}", StringUtils.quote(request.getRequestURI()),
                StringUtils.quote(assertionId));
        try {
            String jossoSessionId = gsl.getSSOIdentityProvider().resolveAuthenticationAssertion(assertionId);
            JOSSOAuthenticationToken authRequest = new JOSSOAuthenticationToken(jossoSessionId, new GrantedAuthority[0]);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (AssertionNotValidException e) {
            throw new AuthenticationServiceException("Unable to authenticate user with assertionId " + assertionId, e);
        } catch (IdentityProvisioningException e) {
            throw new AuthenticationServiceException("Unable to authenticate user with assertionId " + assertionId, e);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Unable to authenticate user with assertionId " + assertionId, e);
        }
    }

    @Override
    public String getDefaultFilterProcessesUrl() {
        return JOSSO_SECURITY_CHECK_URI;
    }

    @Override
    public int getOrder() {
        return FilterChainOrder.AUTHENTICATION_PROCESSING_FILTER;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
        JOSSOAuthenticationToken authentication = (JOSSOAuthenticationToken) authResult;
        JOSSOUtils.setCookie(request, response, authentication.getJossoSessionId());
    }

    public void setGatewayServiceLocator(GatewayServiceLocator gsl) throws Exception {
        try {
            this.gsl = gsl;
        } catch (Exception e) {
            log.error("Error while getting identity provider!", e);
            throw e;
        }
    }

}