package app.daos;

import app.dtos.GuideTotalPriceDTO;
import app.dtos.TripDTO;
import app.entities.Guide;
import app.entities.Trip;
import app.enums.Category;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TripDAO implements IDAO<TripDTO, Integer>{
    private EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory emf){
    this.emf = emf;
    }
    @Override
    public TripDTO read(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
            Trip trip = em.find(Trip.class, integer);
           if(trip == null){
               throw new ApiException(404, "Couldnt find the Trip in the Database");
           }
            return new TripDTO(trip);
        }
    }

    @Override
    public List<TripDTO> readAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<TripDTO>query = em.createQuery("SELECT new app.dtos.TripDTO(t) FROM Trip t", TripDTO.class);

            List<TripDTO> trips = query.getResultList();
            if(trips.isEmpty()){
                throw new ApiException(404,"Could not find any trips in the database");
            }
            return trips;
        }
    }

    @Override
    public TripDTO create(TripDTO tripDTO) {
        Trip trip = new Trip(tripDTO);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();
        }catch (IllegalArgumentException ex) {
            throw new ApiException(400, "Invalid entity type");
        }
        return new TripDTO(trip);
    }

    @Override
    public TripDTO update(Integer integer, TripDTO tripDTO) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, integer);
            if(trip == null){
                throw new ApiException(404,"Trip not found with the given");
            }

            trip.setName(tripDTO.getName());
            trip.setStartTime(tripDTO.getStartTime());
            trip.setEndTime(tripDTO.getEndTime());
            trip.setLocation(tripDTO.getLocation());
            trip.setPrice(tripDTO.getPrice());
            trip.setCategory(tripDTO.getCategory());

            Trip mergedTrip = em.merge(trip);

            if(mergedTrip == null){
                throw new ApiException(400,"Something went wrong with updating the trip");
            }
            em.getTransaction().commit();


            return new TripDTO(mergedTrip);
        }
    }

    @Override
    public void delete(Integer integer) {
try(EntityManager em = emf.createEntityManager()){
    Trip trip = em.find(Trip.class, integer);
    if(trip == null){
        throw new ApiException(404,"Trip not found or does not exist in the database");
    }
    em.getTransaction().begin();
    em.remove(trip);
    em.getTransaction().commit();
    }
}


    public TripDTO updateGuide(Integer integer, Integer guideId){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, integer);
            Guide guide = em.find(Guide.class, guideId);

            if(trip == null && guide == null){
                throw new ApiException(404,"Trip or Guide not found with the given IDS: " + integer + guideId);
            }
            trip.setGuide(guide);
            em.merge(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        }
    }

    public List<TripDTO> readByCategory(String category){
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<TripDTO>query = em.createQuery("SELECT new app.dtos.TripDTO(t) FROM Trip t WHERE t.category = :cate", TripDTO.class);
            query.setParameter("cate", Category.valueOf(category.toUpperCase()));

            return query.getResultList();
        }
    }
    public List<GuideTotalPriceDTO> getTotalPricePerGuide() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<GuideTotalPriceDTO> query = em.createQuery(
                    "SELECT new app.dtos.GuideTotalPriceDTO(g.id, SUM(t.price)) " +
                            "FROM Guide g LEFT JOIN g.trips t " +
                            "GROUP BY g.id",
                    GuideTotalPriceDTO.class
            );
            return query.getResultList();
        }
    }
}
