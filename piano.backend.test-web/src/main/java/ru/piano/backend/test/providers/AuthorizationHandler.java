
package ru.piano.backend.test.providers;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;

public class AuthorizationHandler implements ContainerRequestFilter {

    private final ThreadLocal currentAuditor = new ThreadLocal();

    private String xremoteUsersGroup;

    public void setXremoteUsersGroup(String xremoteUsersGroup) {
        this.xremoteUsersGroup = xremoteUsersGroup;
    }

    public String getCurrentAuditor() {
        return (String) currentAuditor.get();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext securityContext = requestContext.getSecurityContext();
        String userName = null;
        //uncomment container security block in web.xml to work
        if (securityContext.getUserPrincipal() != null) {
            userName = securityContext.getUserPrincipal().getName();
            if (xremoteUsersGroup != null && securityContext.isUserInRole(xremoteUsersGroup)) {
                String xremoteUserName = requestContext.getHeaderString("X-Remote-User");
                if (xremoteUserName != null) {
                    userName = xremoteUserName;
                }
            }
        }
        currentAuditor.set(userName);
    }
}
