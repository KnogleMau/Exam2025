package app.dtos;

import app.entities.Skill;
import app.enums.Category;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class SkillDTO {

    private Integer id;
    private String name;
    private Category category;
    private String description;



    public SkillDTO(Skill skill){
        this.id = skill.getId();
        this.name = skill.getName();
        this.category = skill.getCategory();
        this.description = skill.getDescription();
    }
}
