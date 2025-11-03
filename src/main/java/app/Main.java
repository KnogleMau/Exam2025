package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.exceptions.EntityNotFoundException;
import app.populate.Populate;
import app.routes.Routes;
import app.security.routes.SecurityRoutes;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Exam-2025");
EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
Routes routes = new Routes();
Populate populate = new Populate(emf);


        ApplicationConfig
                .getInstance()
                .initiateServer()
                .checkSecurityRoles() // check for role when route is called
                .setRoute(new SecurityRoutes().getSecurityRoutes())
                .setRoute(SecurityRoutes.getSecuredRoutes())
                .setRoute(routes.getRoutes())
                .startServer(7007)
                .setCORS()
                .setGeneralExceptionHandling();

try {
    populate.populateData();
} catch (EntityNotFoundException e) {
    throw new RuntimeException(e);
}
}

}
