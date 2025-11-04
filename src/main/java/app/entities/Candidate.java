package app.entities;


import app.dtos.CandidateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity
public class Candidate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "candidate_id")
  private Integer id;

  @Column(nullable = false)
  private String name;
  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;
  @Column(name = "education_background",nullable = false)
  private String educationBackground;


  @Builder.Default
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
          name = "candidate_skill",
          joinColumns = @JoinColumn(name = "candidate_id"),
          inverseJoinColumns = @JoinColumn(name = "skill_id")
  )
  private List<Skill> skills = new ArrayList<>();

  public Candidate(CandidateDTO candidateDTO){
      this.id = candidateDTO.getId();
      this.name = candidateDTO.getName();
      this.phoneNumber = candidateDTO.getPhoneNumber();
      this.educationBackground = candidateDTO.getEducationBackground();

  }

    public void addSkill(Skill skill) {
        if ( skill != null) {
            this.skills.add(skill);
            skill.getCandidates().add(this);
        }
    }

}
