package pl.java.sso;

import org.josso.auth.BaseCredential;

/**
 * Salt credential.
 */
public class SaltCredential extends BaseCredential {

    public SaltCredential(Object credential) {
        super(credential);
    }
}
