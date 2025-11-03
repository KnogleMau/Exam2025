package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.exceptions.EntityNotFoundException;
import app.populate.Populate;
import app.routes.route;
import app.security.routes.SecurityRoutes;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Exam-2025");
EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
route route = new route();
Populate populate = new Populate(emf);


        ApplicationConfig
                .getInstance()
                .initiateServer()
                .checkSecurityRoles() // check for role when route is called
                .setRoute(new SecurityRoutes().getSecurityRoutes())
                .setRoute(SecurityRoutes.getSecuredRoutes())
                .setRoute(route.getRoutes())
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
