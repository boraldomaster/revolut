package com.revolut.test.container;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        return Response.status(404).entity(Collections.singletonMap("error", exception.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
}
