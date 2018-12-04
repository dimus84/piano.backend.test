
package ru.piano.backend.test.web;

import io.swagger.annotations.Api;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.transaction.annotation.Transactional;

@Path("heartbeat")
@Api("heartbeat")
public class Heartbeat {

    @GET
    @Transactional
    public String heartbeat() {
        // http://wiki.eclipse.org/EclipseLink/Examples/JPA/EMAPI#Getting_a_JDBC_Connection_from_an_EntityManager
        // @Transactional required

        return "OK";
    }

}
