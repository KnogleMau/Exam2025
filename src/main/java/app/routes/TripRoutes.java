package app.routes;

import app.controllers.GuideController;
import app.controllers.TripController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoutes {

    public EndpointGroup getRoutes(){
        TripController tripController = new TripController();
        GuideController guideController = new GuideController();
        return () -> {
          get("/",tripController::readByCategory, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER); // this can read all trips and search with a given category
          get("/{id}", tripController::read, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER); // this can read with given id
          get("/guides/totalprice", tripController::getTotalPrice, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER); // this give your total price for all the guides
          get("/{id}/packing/weight", tripController::getPackingWeight, SecurityRoutes.Role.ADMIN, SecurityRoutes.Role.USER); // this give you the packing weight for where you are going
          post("/", tripController::create, SecurityRoutes.Role.ADMIN); // This create a trip
            post("/guides", guideController::create);
          put("/{id}", tripController::update, SecurityRoutes.Role.ADMIN); // this updates a trip
          delete("/{id}", tripController::delete, SecurityRoutes.Role.ADMIN); // this deletes a trip
          put("/{tripId}/guides/{guideId}", tripController::addGuideToTrip, SecurityRoutes.Role.ADMIN); // this adds a guide to the trip

        };
    }
}
