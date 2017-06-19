package com.katkada.Other;
/**
 * Created by prakash on 21-12-2016.
 */
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
public class SMTPAuthenticator extends Authenticator {
    public SMTPAuthenticator() {
        super();
    }
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = "prakash.techzar@gmail.com";
        String password = "prakash.techzar1!";
        if ((username != null) && (username.length() > 0) && (password != null)
                && (password.length() > 0)) {
            return new PasswordAuthentication(username, password);
        }
        return null;
    }
}