/*
 * This file is a part of End2End Payment Gateway (EPG).
 * Copyright (C) End2End VAS
 */
package com.end2endmobile.epg.sso.utils;

import org.josso.auth.BaseCredential;

/**
 * Salt credential.
 */
public class SaltCredential extends BaseCredential {

    public SaltCredential(Object credential) {
        super(credential);
    }
}
