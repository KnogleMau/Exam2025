package app.populate;

import app.daos.CandidateDAO;
import app.entities.Candidate;
import app.entities.Skill;
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

    CandidateDAO candidateDAO = new CandidateDAO(emf);

    public void populateData() throws EntityNotFoundException {
        Candidate c1 = Candidate.builder()
                .name("Anders")
                .phoneNumber("12121212")
                .educationBackground("Ek")
                .build();

        Candidate c2 = Candidate.builder()
                .name("Sander")
                .phoneNumber("12321212")
                .educationBackground("Ek")
                .build();

        Candidate c3 = Candidate.builder()
                .name("Bo")
                .phoneNumber("98721212")
                .educationBackground("Ek")
                .build();


        Candidate c4 = Candidate.builder()
                .name("Magnus")
                .phoneNumber("91919191")
                .educationBackground("Ek")
                .build();


        Candidate c5 = Candidate.builder()
                .name("Frank")
                .phoneNumber("52352312")
                .educationBackground("Ek")
                .build();


        Skill skill1 = Skill.builder()
                .name("Framework")
                .category(Category.FRAMEWORK)
                .description(Category.FRAMEWORK.getDescription())
                .slug(Category.FRAMEWORK.getSlugList())
                .build();

        Skill skill2 = Skill.builder()
                .name("Frontend")
                .category(Category.FRONTEND)
                .description(Category.FRONTEND.getDescription())
                .slug(Category.FRONTEND.getSlugList())
                .build();

        Skill skill3 = Skill.builder()
                .name("Programming Language")
                .category(Category.PROG_LANG)
                .description(Category.PROG_LANG.getDescription())
                .slug(Category.PROG_LANG.getSlugList())
                .build();

        Skill skill4 = Skill.builder()
                .name("Database")
                .category(Category.DB)
                .description(Category.DB.getDescription())
                .slug(Category.DB.getSlugList())
                .build();

        Skill skill5 = Skill.builder()
                .name("Devops")
                .category(Category.DEVOPS)
                .description(Category.DEVOPS.getDescription())
                .slug(Category.DEVOPS.getSlugList())
                .build();

        Skill skill6 = Skill.builder()
                .name("Testing")
                .category(Category.TESTING)
                .description(Category.TESTING.getDescription())
                .slug(Category.TESTING.getSlugList())
                .build();

        Skill skill7 = Skill.builder()
                .name("Data")
                .category(Category.DATA)
                .description(Category.DATA.getDescription())
                .slug(Category.DATA.getSlugList())
                .build();



        c1.addSkill(skill1);
        c1.addSkill(skill7);
        c1.addSkill(skill3);

        c2.addSkill(skill3);
        c2.addSkill(skill4);
        c2.addSkill(skill5);

        c3.addSkill(skill2);
        c3.addSkill(skill6);
        c3.addSkill(skill5);

        c4.addSkill(skill7);
        c4.addSkill(skill3);
        c4.addSkill(skill1);

        c5.addSkill(skill7);
        c5.addSkill(skill2);
        c5.addSkill(skill3);


       try(EntityManager em = emf.createEntityManager()){
           em.getTransaction().begin();
           em.persist(c1);
           em.persist(c2);
           em.persist(c3);
           em.persist(c4);
           em.persist(c5);
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
