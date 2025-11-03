package app.daos;

import app.dtos.GuideDTO;
import app.dtos.TripDTO;
import app.entities.Guide;
import app.entities.Trip;
import app.exceptions.ApiException;
import app.exceptions.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GuideDAO implements IDAO<GuideDTO, Integer>{
    private EntityManagerFactory emf;

    public GuideDAO(EntityManagerFactory emf){
        this.emf = emf;
    }


    @Override
    public GuideDTO read(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
            Guide guide = em.find(Guide.class, integer);
            if(guide == null){
                throw new ApiException(404, "Couldnt find the Trip in the Database");
            }
            return new GuideDTO(guide);
        }
    }

    @Override
    public List<GuideDTO> readAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<GuideDTO> query = em.createQuery("SELECT new app.dtos.GuideDTO(g) FROM Guide g", GuideDTO.class);
            List<GuideDTO> guides = query.getResultList();
            if(guides.isEmpty()){
                throw new ApiException(404,"Could not find any guides in the database");
            }
            return query.getResultList();
        }
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) {
        Guide guide = new Guide(guideDTO);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(guide);
            em.getTransaction().commit();
        }catch (IllegalArgumentException ex) {
            throw new ApiException(400, "Invalid entity type");
        }
            return new GuideDTO(guide);
    }


    @Override
    public GuideDTO update(Integer integer, GuideDTO guideDTO){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, integer);
            if(guide == null){
                throw new ApiException(404,"Guide not found with Id");
            }

            guide.setName(guideDTO.getName());
            guide.setEmail(guideDTO.getEmail());
            guide.setPhoneNumber(guideDTO.getPhoneNumber());
            guide.setYearsOfExperience(guideDTO.getYearsOfExperience());


            Guide mergedGuide = em.merge(guide);
            if(mergedGuide == null){
                throw new ApiException(400,"Something went wrong with updating the guide");
            }
            em.getTransaction().commit();
            return new GuideDTO(mergedGuide);

        }
    }

    @Override
    public void delete(Integer integer) {
    try(EntityManager em = emf.createEntityManager()){
        Guide guide = em.find(Guide.class, integer);
        if(guide == null){
            throw new ApiException(404,"Guide not found or does not exist in the database");
        }
        em.getTransaction().begin();
        em.remove(guide);
        em.getTransaction().commit();
        }
    }

}
