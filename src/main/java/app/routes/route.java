package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class route {
    GuideRoutes guideRoutes = new GuideRoutes();
    TripRoutes tripRoutes = new TripRoutes();
    public EndpointGroup getRoutes(){
        return () -> {

          get("/", ctx -> ctx.result("Hello World"));
          path("/trips", tripRoutes.getRoutes());
          path("/guides",guideRoutes.getRoutes());
        };
    }
}
