package io.github.giannialberico;

import io.github.giannialberico.quarkus.lab.extension.runtime.Colored;
import io.github.giannialberico.quarkus.lab.extension.runtime.ExtensionBean;
import io.quarkus.arc.Arc;
import io.smallrye.common.annotation.Identifier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.lang.classfile.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Path("/lab")
public class LabResource {

    @Inject
    public Colored colored;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/reflect")
    public String reflect() throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        Method method = Thread.currentThread().getContextClassLoader().loadClass("io.github.giannialberico.ReflectiveService").getDeclaredMethod("hello");
        return "Invoked ReflectiveService.hello(): " + method.invoke(new ReflectiveService());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/color")
    @RunOnVirtualThread
    public String color() {
        return colored.getColor();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/extensionbean/{name}")
    public String extensionBean(@PathParam("name") String name) {
        Identifier.Literal identifier = Identifier.Literal.of(name);
        ExtensionBean extensionBean = Arc.container().select(ExtensionBean.class, identifier).get();
        return extensionBean.greet();
    }

}
