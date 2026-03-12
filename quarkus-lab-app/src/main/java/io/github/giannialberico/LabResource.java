package io.github.giannialberico;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Path("/lab")
public class LabResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/reflect")
    public String reflect() throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        Method method = Thread.currentThread().getContextClassLoader().loadClass("io.github.giannialberico.ReflectiveService").getDeclaredMethod("hello");
        return "Invoked ReflectiveService.hello(): " + method.invoke(new ReflectiveService());
    }
}
