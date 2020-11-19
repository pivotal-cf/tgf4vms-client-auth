package io.pivotal.cloudcache;

import static io.pivotal.cloudcache.ClientAuthInitialize.GEMFIRE_SECURITY_PASSWORD;
import static io.pivotal.cloudcache.ClientAuthInitialize.GEMFIRE_SECURITY_USERNAME;
import static io.pivotal.cloudcache.ClientAuthInitialize.PASSWORD;
import static io.pivotal.cloudcache.ClientAuthInitialize.USERNAME;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;
import org.apache.geode.security.AuthInitialize;
import org.apache.geode.security.AuthenticationFailedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ClientAuthInitializeTest {

    @AfterEach
    public void afterEach() {
        System.getProperties().remove(GEMFIRE_SECURITY_USERNAME);
        System.getProperties().remove(GEMFIRE_SECURITY_PASSWORD);
    }

    @Test
    public void createIsNotNull() {
        final AuthInitialize authInitialize = ClientAuthInitialize.create();
        assertNotNull(authInitialize);
    }

    @Test
    public void createIsUnique() {
        final AuthInitialize first = ClientAuthInitialize.create();
        final AuthInitialize second = ClientAuthInitialize.create();
        assertNotSame(first, second);
    }

    @Test
    public void getCredentialsWithoutUsernameThrows() {
        System.getProperties().remove(GEMFIRE_SECURITY_USERNAME);
        System.setProperty(GEMFIRE_SECURITY_PASSWORD, "password1");

        final AuthInitialize authInitialize = ClientAuthInitialize.create();
        assertThrows(
                AuthenticationFailedException.class,
                () -> authInitialize.getCredentials(null, null, false));
    }

    @Test
    public void getCredentialsWithoutPasswordThrows() {
        System.setProperty(GEMFIRE_SECURITY_USERNAME, "user1");
        System.getProperties().remove(GEMFIRE_SECURITY_PASSWORD);

        final AuthInitialize authInitialize = ClientAuthInitialize.create();
        assertThrows(
                AuthenticationFailedException.class,
                () -> authInitialize.getCredentials(null, null, false));
    }

    @Test
    public void getCredentialsWithUsernamePassword() {
        System.setProperty(GEMFIRE_SECURITY_USERNAME, "user1");
        System.setProperty(GEMFIRE_SECURITY_PASSWORD, "password1");

        final AuthInitialize authInitialize = ClientAuthInitialize.create();
        final Properties credentials = authInitialize.getCredentials(null, null, false);

        assertEquals("user1", credentials.get(USERNAME));
        assertEquals("password1", credentials.get(PASSWORD));
    }
}
