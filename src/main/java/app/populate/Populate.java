package app.populate;

import app.daos.GuideDAO;
import app.entities.Guide;
import app.entities.Trip;
import app.enums.Category;
import app.exceptions.EntityNotFoundException;
import app.security.daos.SecurityDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Populate {

    private EntityManagerFactory emf;
    public Populate(EntityManagerFactory emf){
        this.emf = emf;
    }

    GuideDAO guideDAO = new GuideDAO(emf);

    public void populateData() throws EntityNotFoundException {
        Guide g1 = Guide.builder()
                .name("Anders")
                .email("And@hotmail.com")
                .phoneNumber("12121212")
                .yearsOfExperience(5)
                .trips(new ArrayList<>())
                .build();

        Guide g2 = Guide.builder()
                .name("Sander")
                .email("Sander@hotmail.com")
                .phoneNumber("12321212")
                .yearsOfExperience(7)
                .trips(new ArrayList<>())
                .build();

        Guide g3 = Guide.builder()
                .name("Bo")
                .email("Bo@hotmail.com")
                .phoneNumber("98721212")
                .yearsOfExperience(13)
                .trips(new ArrayList<>())
                .build();


        Guide g4 = Guide.builder()
                .name("Magnus")
                .email("Magnus@hotmail.com")
                .phoneNumber("91919191")
                .yearsOfExperience(2)
                .trips(new ArrayList<>())
                .build();


        Guide g5 = Guide.builder()
                .name("Frank")
                .email("Frank@hotmail.com")
                .phoneNumber("52352312")
                .yearsOfExperience(1)
                .trips(new ArrayList<>())
                .build();


        Trip trip1 = Trip.builder()
                .name("Sunny-Beach")
                .startTime(LocalDateTime.of(2026, 04, 20, 13, 30))
                .endTime(LocalDateTime.of(2026, 04, 20, 17, 30))
                .location("Bulgaria")
                .price(1500)
                .category(Category.BEACH)
                .build();

        Trip trip2 = Trip.builder()
                .name("Porto")
                .startTime(LocalDateTime.of(2026, 05, 20, 07, 30))
                .endTime(LocalDateTime.of(2026, 05, 22, 21, 30))
                .location("Portugal")
                .price(8000)
                .category(Category.CITY)
                .build();

        Trip trip3 = Trip.builder()
                .name("Miami")
                .startTime(LocalDateTime.of(2026, 06, 20, 13, 30))
                .endTime(LocalDateTime.of(2026, 07, 20, 17, 30))
                .location("USA")
                .price(45000)
                .category(Category.CITY)
                .build();

        Trip trip4 = Trip.builder()
                .name("Mountains")
                .startTime(LocalDateTime.of(2026, 02, 20, 04, 30))
                .endTime(LocalDateTime.of(2026, 02, 27, 13, 30))
                .location("Sweden")
                .price(15000)
                .category(Category.LAKE)
                .build();

        Trip trip5 = Trip.builder()
                .name("Megaluf")
                .startTime(LocalDateTime.of(2026, 07, 17, 13, 30))
                .endTime(LocalDateTime.of(2026, 07, 24, 17, 30))
                .location("Spain")
                .price(19500)
                .category(Category.BEACH)
                .build();

        g1.addTrip(trip5);
        g2.addTrip(trip4);
        g3.addTrip(trip1);
        g4.addTrip(trip2);
        g5.addTrip(trip3);

       try(EntityManager em = emf.createEntityManager()){
           em.getTransaction().begin();
           em.persist(g1);
           em.persist(g2);
           em.persist(g3);
           em.persist(g4);
           em.persist(g5);
           em.getTransaction().commit();

       }

        SecurityDAO securityDAO = new SecurityDAO(emf);

        securityDAO.createRole("User");
        securityDAO.createRole("Anyone");
        securityDAO.createRole("Admin");

        securityDAO.createUser("ADMIN", "1234");
        securityDAO.addUserRole("ADMIN", "Admin");



    }
}
