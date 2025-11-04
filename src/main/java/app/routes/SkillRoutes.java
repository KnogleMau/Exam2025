package app.routes;

import app.controllers.CandidateController;
import app.controllers.SkillController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class SkillRoutes {
 SkillController skillController = new SkillController();
    public EndpointGroup getRoutes(){
        return () ->{
        get("/", skillController::readAll, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER); // this give you all the skills in the database
        post("/", skillController::create, SecurityRoutes.Role.ADMIN); // this create a new skill
        };
    }
}
