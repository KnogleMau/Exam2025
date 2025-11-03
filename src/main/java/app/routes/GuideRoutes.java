package app.routes;

import app.controllers.GuideController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;

public class GuideRoutes {
GuideController guideController = new GuideController();
    public EndpointGroup getRoutes(){
        return () ->{
        get("/", guideController::readAll, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER); // this give you all the guides
        };
    }
}
