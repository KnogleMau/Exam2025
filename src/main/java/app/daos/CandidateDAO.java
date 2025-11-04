package app.daos;

import app.dtos.*;
import app.entities.Candidate;
import app.entities.Skill;
import app.enums.Category;
import app.exceptions.ApiException;
import app.service.ApiService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CandidateDAO implements IDAO<CandidateDTO, Integer>{
    private EntityManagerFactory emf;

    public CandidateDAO(EntityManagerFactory emf){
        this.emf = emf;
    }


    @Override
    public CandidateDTO read(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
           Candidate candidate = em.find(Candidate.class, integer);
            if(candidate == null){
                throw new ApiException(404, "Could not find the candidate in the database");
            }
            return new CandidateDTO(candidate);
        }
    }

    @Override
    public List<CandidateDTO> readAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<CandidateDTO> query = em.createQuery("SELECT new app.dtos.CandidateDTO(c) FROM Candidate c", CandidateDTO.class);
            List<CandidateDTO> candidates = query.getResultList();
            if(candidates.isEmpty()){
                throw new ApiException(404,"Could not find any candidates in the database");
            }
            return query.getResultList();
        }
    }

    @Override
    public CandidateDTO create(CandidateDTO candidateDTO) {
        Candidate candidate = new Candidate(candidateDTO);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(candidate);
            em.getTransaction().commit();
        }catch (IllegalArgumentException ex) {
            throw new ApiException(400, "Invalid entity type");
        }
            return new CandidateDTO(candidate);
    }


    @Override
    public CandidateDTO update(Integer integer, CandidateDTO candidateDTO){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Candidate candidate = em.find(app.entities.Candidate.class, integer);
            if(candidate == null){
                throw new ApiException(404,"Candidate not found with the given Id");
            }

            candidate.setName(candidateDTO.getName());
            candidate.setEducationBackground(candidateDTO.getEducationBackground());
            candidate.setPhoneNumber(candidateDTO.getPhoneNumber());


            Candidate mergedCandidate = em.merge(candidate);
            if(mergedCandidate == null){
                throw new ApiException(400,"Something went wrong with updating the Candidate");
            }
            em.getTransaction().commit();
            return new CandidateDTO(mergedCandidate);

        }
    }

    @Override
    public void delete(Integer integer) {
    try(EntityManager em = emf.createEntityManager()){
        Candidate candidate = em.find(Candidate.class, integer);
        if(candidate == null){
            throw new ApiException(404,"Candidate not found or does not exist in the database");
        }
        em.getTransaction().begin();
        em.remove(candidate);
        em.getTransaction().commit();
        }
    }


    public CandidateDTO addSkill(Integer skillId, Integer candidateId){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Skill skill = em.find(Skill.class, skillId);
            Candidate candidate = em.find(Candidate.class, candidateId);

            if(skill == null || candidate == null){
                throw new ApiException(404,"Skill or Candidate not found with the given IDs: skillId=" + skillId + ", candidateId=" + candidateId);
            }
            candidate.addSkill(skill);
            em.merge(candidate);
            em.getTransaction().commit();
            return new CandidateDTO(candidate);
        }
    }
    public List<CandidateDTO> readByCategory(String category){

        Category cat = Category.valueOf(category.toUpperCase());

        try(EntityManager em = emf.createEntityManager()){
            List<Candidate> allCandidates = em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList();

            return allCandidates.stream()
                    .filter(c -> c.getSkills().stream().anyMatch(s -> s.getCategory() == cat))
                    .map(CandidateDTO::new)
                    .toList();
        }
    }


    public List<CandidatePopularityDTO> getTopByPopularity(){
        try (EntityManager em = emf.createEntityManager()) {
            List<Candidate> candidates = em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList();

            List<CandidatePopularityDTO> popularityList = new ArrayList<>();

            for (Candidate candidate : candidates) {
                if (candidate.getSkills().isEmpty()) continue;
                String slugs = candidate.getSkills().stream()
                        .flatMap(skill -> Arrays.stream(skill.getSlug().split(",")))
                        .map(String::trim)
                        .collect(Collectors.joining(","));


                String url = "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" + slugs;
                SkillStatsResponseDTO response = ApiService.fetchData(url, SkillStatsResponseDTO.class);

                double avgPopularity = response.getData().stream()
                        .filter(s -> s.getPopularityScore() != null)
                        .mapToInt(SkillStatsDTO::getPopularityScore)
                        .average()
                        .orElse(0);

                popularityList.add(new CandidatePopularityDTO(candidate.getId(),candidate.getName(), avgPopularity));
            }

            popularityList.sort((a, b) -> Double.compare(b.getAveragePopularityScore(), a.getAveragePopularityScore()));

            return popularityList;
        }
    }
}
