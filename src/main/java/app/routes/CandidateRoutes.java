package app.routes;

import app.controllers.CandidateController;
import app.controllers.SkillController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CandidateRoutes {

    public EndpointGroup getRoutes(){
        CandidateController candidateController = new CandidateController();
        return () -> {
        get("/", candidateController::readAll, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER);
        get("/top-by-popularity", candidateController::getPopularityFromCandidates, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER);
        get("/{id}", candidateController::read, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER);
        post("/", candidateController::create, SecurityRoutes.Role.ADMIN);
        put("/{id}", candidateController::update, SecurityRoutes.Role.ADMIN);
        delete("/{id}", candidateController::delete, SecurityRoutes.Role.ADMIN);
        put("/{candidateId}/skills/{skillId}", candidateController::addSkillToCandidate, SecurityRoutes.Role.ADMIN);

        };
    }
}
