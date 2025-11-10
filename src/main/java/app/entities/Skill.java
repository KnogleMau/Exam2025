package app.entities;

import app.dtos.SkillDTO;
import app.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String description;



    @Builder.Default
    @ManyToMany(mappedBy = "skills")
    private List<Candidate> candidates = new ArrayList<>();



    public Skill(SkillDTO skillDTO){
        this.name = skillDTO.getName();
        this.category = skillDTO.getCategory();
        this.description = skillDTO.getDescription();
    }
}
