package app.controllers;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.dtos.CandidateDTO;
import app.dtos.CandidatePopularityDTO;
import app.dtos.SkillDTO;
import app.dtos.SkillStatsResponseDTO;
import app.exceptions.ApiException;
import app.service.ApiService;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CandidateController implements IController{
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    CandidateDAO c = new CandidateDAO(emf);
    ObjectMapper objectMapper = new Utils().getObjectMapper();


    @Override
    public void read(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            CandidateDTO candidateDTO = c.read(id);

            String slugs = candidateDTO.getSkills().stream()
                    .map(SkillDTO::getSlug)
                    .map(s -> s.split(","))
                    .flatMap(Arrays::stream)
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.joining(","));

            String url = "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" + slugs;

            SkillStatsResponseDTO statsResponseDTO = ApiService.fetchData(url, SkillStatsResponseDTO.class);

            if(statsResponseDTO != null){
                candidateDTO.setSkillStatsDTOS(statsResponseDTO.getData());
            }
            ctx.status(200).json(candidateDTO);
        } catch(ApiException ex){
            ObjectNode on = objectMapper.createObjectNode().put("msg",ex.getMessage()).put("status",ex.getCode());
            ctx.status(ex.getCode()).json(on);
        }
    }

    @Override
    public void readAll(Context ctx) {
        String category = ctx.queryParam("category");
        try {
            List<CandidateDTO> candidates;
            if(category == null || category.isEmpty()){
                candidates = c.readAll();
            }else{
                candidates = c.readByCategory(category);
            }

            ctx.status(200).json(candidates);
        } catch (ApiException ex){
            ObjectNode on = objectMapper.createObjectNode().put("msg",ex.getMessage()).put("status",ex.getCode());
            ctx.status(ex.getCode()).json(on);
        }
    }


    @Override
    public void create(Context ctx) {
        try {
            CandidateDTO candidateDTO = ctx.bodyAsClass(CandidateDTO.class);
            c.create(candidateDTO);
            ctx.status(201).json(candidateDTO);
        }catch (ApiException ex) {
            ObjectNode on = objectMapper.createObjectNode().put("msg",ex.getMessage()).put("status",ex.getCode());
            ctx.status(ex.getCode()).json(on);
        }
    }

    @Override
    public void update(Context ctx) {
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            CandidateDTO candidateDTO = ctx.bodyAsClass(CandidateDTO.class);
            CandidateDTO updatedCandidate = c.update(id, candidateDTO);
            ctx.status(200).json(updatedCandidate);
        } catch (ApiException ex){
            ObjectNode on = objectMapper.createObjectNode().put("msg",ex.getMessage()).put("status",ex.getCode());
            ctx.status(ex.getCode()).json(on);
        }

    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            c.delete(id);
            ctx.status(200).result("The Candidate with the " + id + " have been deleted");
        } catch (ApiException ex){
            ObjectNode on = objectMapper.createObjectNode().put("msg",ex.getMessage()).put("status",ex.getCode());
            ctx.status(ex.getCode()).json(on);
        }
    }

    public void addSkillToCandidate(Context ctx){
        try {
            int candidateId = Integer.parseInt(ctx.pathParam("candidateId"));
            int skillId= Integer.parseInt(ctx.pathParam("skillId"));

            CandidateDTO updatedCandidate = c.addSkill(skillId, candidateId);

            ctx.status(200).json(updatedCandidate);
        }catch (ApiException ex){
            ObjectNode on = objectMapper.createObjectNode().put("msg",ex.getMessage()).put("status",ex.getCode());
            ctx.status(ex.getCode()).json(on);
        }
    }
    public void getPopularityFromCandidates(Context ctx){
        try {
            List<CandidatePopularityDTO> candidates = c.getTopByPopularity();
            ctx.status(200).json(candidates);
        } catch (ApiException ex) {
            ObjectNode on = objectMapper.createObjectNode().put("msg",ex.getMessage()).put("status",ex.getCode());
            ctx.status(ex.getCode()).json(on);
        }
    }
}
