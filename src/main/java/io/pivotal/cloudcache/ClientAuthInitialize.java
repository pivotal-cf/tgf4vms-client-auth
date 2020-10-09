/*
 * Copyright (C) 2017-Present Pivotal Software, Inc. All rights reserved.
 * This program and the accompanying materials are made available under
 * the terms of the under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.cloudcache;

import java.util.Properties;
import org.apache.geode.distributed.DistributedMember;
import org.apache.geode.security.AuthInitialize;
import org.apache.geode.security.AuthenticationFailedException;

public class ClientAuthInitialize implements AuthInitialize {
    static final String USERNAME = "security-username";
    static final String PASSWORD = "security-password";
    static final String GEMFIRE_SECURITY_USERNAME = "gemfire.security-username";
    static final String GEMFIRE_SECURITY_PASSWORD = "gemfire.security-password";

    public static AuthInitialize create() {
        return new ClientAuthInitialize();
    }

    @Override
    public Properties getCredentials(
            final Properties securityProps, final DistributedMember server, final boolean isPeer)
            throws AuthenticationFailedException {
        final String username = System.getProperty(GEMFIRE_SECURITY_USERNAME);
        if (null == username) {
            throw new AuthenticationFailedException("Missing " + GEMFIRE_SECURITY_USERNAME);
        }

        final String password = System.getProperty(GEMFIRE_SECURITY_PASSWORD);
        if (null == password) {
            throw new AuthenticationFailedException("Missing " + GEMFIRE_SECURITY_PASSWORD);
        }

        final Properties credentials = new Properties();
        credentials.put(USERNAME, username);
        credentials.put(PASSWORD, password);
        return credentials;
    }
}
