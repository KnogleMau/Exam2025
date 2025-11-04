package app.daos;

import app.dtos.SkillDTO;
import app.entities.Skill;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SkillDAO implements IDAO<SkillDTO, Integer>{
    private EntityManagerFactory emf;

    public SkillDAO(EntityManagerFactory emf){
    this.emf = emf;
    }

    @Override
    public SkillDTO read(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
            Skill skill = em.find(Skill.class, integer);
           if(skill == null){
               throw new ApiException(404, "Couldnt find the skill in the Database");
           }
            return new SkillDTO(skill);
        }
    }

    @Override
    public List<SkillDTO> readAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<SkillDTO>query = em.createQuery("SELECT new app.dtos.SkillDTO(t) FROM Skill t", SkillDTO.class);

            List<SkillDTO> skills = query.getResultList();
            if(skills.isEmpty()){
                throw new ApiException(404,"Could not find any skills in the database");
            }
            return skills;
        }
    }

    @Override
    public SkillDTO create(SkillDTO skillDTO) {
        Skill skill = new Skill(skillDTO);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(skill);
            em.getTransaction().commit();
        }catch (IllegalArgumentException ex) {
            throw new ApiException(400, "Invalid entity type");
        }
        return new SkillDTO(skill);
    }

    @Override
    public SkillDTO update(Integer integer, SkillDTO skillDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Skill skill = em.find(Skill.class, integer);
            if(skill == null){
                throw new ApiException(404,"Skill not found with the given");
            }

            skill.setName(skillDTO.getName());
            skill.setCategory(skillDTO.getCategory());

            Skill mergedSkill = em.merge(skill);

            if(mergedSkill == null){
                throw new ApiException(400,"Something went wrong with updating the skill");
            }
            em.getTransaction().commit();


            return new SkillDTO(mergedSkill);
        }
    }

    @Override
    public void delete(Integer integer) {
try(EntityManager em = emf.createEntityManager()){
    Skill skill = em.find(Skill.class, integer);
    if(skill == null){
        throw new ApiException(404,"skill not found or does not exist in the database");
    }
    em.getTransaction().begin();
    em.remove(skill);
    em.getTransaction().commit();
    }
}
}
