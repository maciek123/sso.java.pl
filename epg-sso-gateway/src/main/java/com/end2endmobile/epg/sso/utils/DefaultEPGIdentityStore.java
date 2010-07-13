/*
 * This file is a part of End2End Payment Gateway (EPG).
 * Copyright (C) End2End VAS
 */
package com.end2endmobile.epg.sso.utils;

import com.end2endmobile.epg.core.admin.login.Role;
import com.end2endmobile.epg.core.common.dao.IGenericDAO;
import com.end2endmobile.epg.core.domain.Administrator;
import com.end2endmobile.epg.core.domain.MasterAdministrator;
import com.end2endmobile.epg.core.domain.ServiceProviderAdministrator;
import com.end2endmobile.epg.core.domain.SystemAdministrator;
import org.josso.auth.Credential;
import org.josso.auth.CredentialKey;
import org.josso.auth.CredentialProvider;
import org.josso.auth.CredentialStore;
import org.josso.auth.scheme.PasswordCredential;
import org.josso.auth.scheme.UsernameCredential;
import org.josso.gateway.identity.exceptions.NoSuchUserException;
import org.josso.gateway.identity.exceptions.SSOIdentityException;
import org.josso.gateway.identity.service.BaseRole;
import org.josso.gateway.identity.service.BaseRoleImpl;
import org.josso.gateway.identity.service.BaseUser;
import org.josso.gateway.identity.service.BaseUserImpl;
import org.josso.gateway.identity.service.store.IdentityStore;
import org.josso.gateway.identity.service.store.SimpleUserKey;
import org.josso.gateway.identity.service.store.UserKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of EPG SSO identity store.
 */
@Transactional
public class DefaultEPGIdentityStore implements IdentityStore, CredentialStore {

    private static final Logger log = LoggerFactory.getLogger(DefaultEPGIdentityStore.class);

    private IGenericDAO<Administrator> adminDao;

    public BaseUser loadUser(UserKey userKey) throws NoSuchUserException, SSOIdentityException {
        log.debug("Loading user for key: {}", userKey);
        final SimpleUserKey key = (SimpleUserKey) userKey;
        Administrator admin = findUserByLogin(key.getId());
        return new BaseUserImpl(admin.getLogin());
    }

    public BaseRole[] findRolesByUserKey(UserKey userKey) throws SSOIdentityException {
        log.debug("Loading roles for key: {}", userKey);
        SimpleUserKey key = (SimpleUserKey) userKey;
        Administrator administrator = findUserByLogin(key.getId());
        return getRolesForAdmin(administrator);
    }

    public boolean userExists(UserKey userKey) throws SSOIdentityException {
        log.debug("Checking user existence for key: {}", userKey);
        SimpleUserKey key = (SimpleUserKey) userKey;
        Administrator admin = findUserByLogin(key.getId());
        return admin != null;
    }

    public Credential[] loadCredentials(CredentialKey credentialKey, CredentialProvider credentialProvider) throws SSOIdentityException {
        log.debug("Loading credentials for credential key {}", credentialKey);
        SimpleUserKey key = (SimpleUserKey) credentialKey;
        Administrator admin = findUserByLogin(key.getId());
        PasswordCredential passwordCredential = new PasswordCredential(admin.getPasswordHash());
        UsernameCredential usernameCredential = new UsernameCredential(admin.getLogin());
        SaltCredential saltCredential = new SaltCredential(admin.getPasswordSalt());
        return new Credential[]{passwordCredential, usernameCredential, saltCredential};
    }

    private Administrator findUserByLogin(String login) throws NoSuchUserException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("login", login);
        List<Administrator> admins = adminDao.findByProperties(Administrator.class, params);
        if (admins.isEmpty()) {
            throw new NoSuchUserException("Can not find user with login: " + login);
        } else if (admins.size() > 1) {
            throw new NoSuchUserException("Can not find user with login: " + login
                    + " too many users with the same login!");
        }
        return admins.get(0);
    }

    private BaseRole[] getRolesForAdmin(Administrator administrator) {
        List<String> roles = new ArrayList<String>();
        if (administrator instanceof MasterAdministrator) {
            roles = Role.getAdminRoles((MasterAdministrator) administrator);
        } else if (administrator instanceof SystemAdministrator) {
            roles = Role.getAdminRoles((SystemAdministrator) administrator);
        } else if (administrator instanceof ServiceProviderAdministrator) {
            roles = Role.getAdminRoles((ServiceProviderAdministrator) administrator);
        }
        List<BaseRole> baseRoles = new ArrayList<BaseRole>();
        for (String role : roles) {
            baseRoles.add(new BaseRoleImpl(role));
        }
        return baseRoles.toArray(new BaseRole[baseRoles.size()]);
    }

    public void setAdminDao(IGenericDAO<Administrator> adminDao) {
        this.adminDao = adminDao;
    }
}
