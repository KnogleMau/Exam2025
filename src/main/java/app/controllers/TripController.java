package app.controllers;

import app.config.HibernateConfig;
import app.daos.TripDAO;
import app.dtos.GuideTotalPriceDTO;
import app.dtos.PackingItemDTO;
import app.dtos.PackingResponseDTO;
import app.dtos.TripDTO;
import app.exceptions.ApiException;
import app.exceptions.EntityNotFoundException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static app.service.ApiService.fetchData;

public class TripController implements IController{
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    TripDAO t = new TripDAO(emf);

    private static final Logger logger = LoggerFactory.getLogger(TripController.class);
    private static final Logger debugLogger = LoggerFactory.getLogger("app");

    @Override
    public void read(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = t.read(id);

            if(tripDTO.getCategory() != null){
                String url = "https://packingapi.cphbusinessapps.dk/packinglist/" + tripDTO.getCategory().name().toLowerCase();
            PackingResponseDTO packingResponseDTO = fetchData(url, PackingResponseDTO.class);
                if(packingResponseDTO != null){
                    tripDTO.setPackingItems(packingResponseDTO.getItems());
                }
            }


            ctx.status(200).json(tripDTO);
        } catch(ApiException ex){
            ctx.status(ex.getCode()).result(ex.getMessage());
        }
    }

    @Override
    public void readAll(Context ctx) {

        try {
            List<TripDTO> trips = t.readAll();
            ctx.status(200).json(trips);
            } catch (ApiException ex){
            ctx.status(404).result("Could not find the trips " + ex.getMessage());
        }
    }


    @Override
    public void create(Context ctx) {
        try {
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            t.create(tripDTO);
            ctx.status(201).json(tripDTO);
        }catch (ApiException ex) {
            ctx.status(ex.getCode()).result(ex.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            TripDTO updatedTrip = t.update(id, tripDTO);
            ctx.status(200).json(updatedTrip);
        } catch (ApiException ex){
             ctx.status(ex.getCode()).result(ex.getMessage());
        }

    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        t.delete(id);
        ctx.status(200).result("The trip with the "+ id + " have been deleted");
    }

    public void addGuideToTrip(Context ctx){
        try {
      int tripId = Integer.parseInt(ctx.pathParam("tripId"));
      int guideId= Integer.parseInt(ctx.pathParam("guideId"));

            TripDTO updatedTrip = t.updateGuide(tripId, guideId);

            ctx.status(200).json(updatedTrip);
        }catch (ApiException ex){
            ctx.status(ex.getCode()).result(ex.getMessage());
        }
    }

    public void readByCategory(Context ctx){
        String category = ctx.queryParam("category");
        try{
            List<TripDTO> trips;
            if (category == null || category.isEmpty()) {
                trips = t.readAll();
            } else {
                trips = t.readByCategory(category);
            }
            ctx.status(200).json(trips);
        } catch (IllegalArgumentException ex){
            ctx.status(400).result("Invalid category " + category);
        }
    }

    public void getTotalPrice(Context ctx){
        try{
            List<GuideTotalPriceDTO> total = t.getTotalPricePerGuide();
            ctx.status(200).json(total);
        } catch(Exception ex){
            ctx.status(500).result("Could not calculate total prices " + ex.getMessage());
        }
    }

    public void getPackingWeight(Context ctx){
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = t.read(id);

            String url = "https://packingapi.cphbusinessapps.dk/packinglist/" + tripDTO.getCategory().name().toLowerCase();

            PackingResponseDTO packingResponseDTO = fetchData(url, PackingResponseDTO.class);


            int totalWeight = 0;
            if(packingResponseDTO != null && packingResponseDTO.getItems() != null){
                for (PackingItemDTO item : packingResponseDTO.getItems()){
                    totalWeight += item.getWeightInGrams() * item.getQuantity();
                }
            }

            ctx.status(200).result("The total packing weight for your trip to " + tripDTO.getLocation() + " Is " + totalWeight / 1000 + "kg");
        } catch(ApiException ex){
            System.out.println(ex.getMessage());
            ctx.status(404).result("Could not find the trip with the given id" + ex.getMessage());
        }
    }
}
