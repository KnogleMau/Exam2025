package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    SkillRoutes skillRoutes = new SkillRoutes();
    CandidateRoutes candidateRoutes = new CandidateRoutes();
    public EndpointGroup getRoutes(){
        return () -> {

          get("/", ctx -> ctx.result("Hello World"));
          path("/candidates", candidateRoutes.getRoutes());
          path("/skill", skillRoutes.getRoutes());
        };
    }
}
