package app.controllers;

import app.config.HibernateConfig;
import app.daos.SkillDAO;
import app.dtos.SkillDTO;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SkillController implements IController{
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    SkillDAO s = new SkillDAO(emf);

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);
    private static final Logger debugLogger = LoggerFactory.getLogger("app");

    @Override
    public void read(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            SkillDTO skillDTO = s.read(id);
            ctx.status(200).json(skillDTO);
        } catch(ApiException ex){
            ctx.status(ex.getCode()).result(ex.getMessage());
        }
    }

    @Override
    public void readAll(Context ctx) {

        try {
            List<SkillDTO> skills = s.readAll();
            ctx.status(200).json(skills);
            } catch (ApiException ex){
            ctx.status(ex.getCode()).result(ex.getMessage());
        }
    }


    @Override
    public void create(Context ctx) {
        try {
            SkillDTO skillDTO = ctx.bodyAsClass(SkillDTO.class);
            s.create(skillDTO);
            ctx.status(201).json(skillDTO);
        }catch (ApiException ex) {
            ctx.status(ex.getCode()).result(ex.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            SkillDTO skillDTO = ctx.bodyAsClass(SkillDTO.class);
            SkillDTO updatedSkill = s.update(id, skillDTO);
            ctx.status(200).json(updatedSkill);
        } catch (ApiException ex){
             ctx.status(ex.getCode()).result(ex.getMessage());
        }

    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            s.delete(id);
            ctx.status(200).result("The skill with the " + id + " have been deleted");
        } catch (ApiException ex){
            ctx.status(ex.getCode()).result(ex.getMessage());
        }
    }
}
