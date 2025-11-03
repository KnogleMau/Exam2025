package app.controllers;

import app.config.HibernateConfig;
import app.daos.GuideDAO;
import app.dtos.GuideDTO;
import app.exceptions.ApiException;
import app.exceptions.EntityNotFoundException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GuideController implements IController{
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    GuideDAO g = new GuideDAO(emf);

    private static final Logger logger = LoggerFactory.getLogger(GuideController.class);
    private static final Logger debugLogger = LoggerFactory.getLogger("app");

    @Override
    public void read(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            GuideDTO guideDTO = g.read(id);

            ctx.status(200).json(guideDTO);
        } catch(ApiException ex){
            System.out.println(ex.getMessage());
            ctx.status(404).result("Could not find the Guide with the given id" + ex.getMessage());
        }
    }

    @Override
    public void readAll(Context ctx) {

        try {
            List<GuideDTO> guides = g.readAll();
            ctx.status(200).json(guides);
        } catch (ApiException ex){
            ctx.status(404).result("Could not find any of the guides " + ex.getMessage());
        }
    }


    @Override
    public void create(Context ctx) {
        try {
            GuideDTO guideDTO = ctx.bodyAsClass(GuideDTO.class);
            g.create(guideDTO);
            ctx.status(201).json(guideDTO);
        }catch (BadRequestResponse ex) {
            ctx.status(400).result("Error: Invalid request body: " + ex.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            GuideDTO guideDTO = ctx.bodyAsClass(GuideDTO.class);
            GuideDTO updatedGuide = g.update(id, guideDTO);
            ctx.status(200).json(updatedGuide);
        } catch (ApiException ex){
            ctx.status(ex.getCode()).result(ex.getMessage());
        }

    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        g.delete(id);
        ctx.status(204).result("The guide with the "+ id + " have been deleted");
    }
}
