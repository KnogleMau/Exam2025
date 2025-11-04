package app.dtos;

import app.entities.Candidate;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CandidateDTO {

private Integer id;
private String name;
private String phoneNumber;
private String educationBackground;


private List<SkillDTO> skills = new ArrayList<>();

private List<SkillStatsDTO> skillStatsDTOS = new ArrayList<>();

public CandidateDTO(Candidate candidate){
    this.id = candidate.getId();
    this.name = candidate.getName();
    this.phoneNumber = candidate.getPhoneNumber();
    this.educationBackground = candidate.getEducationBackground();
    if (candidate.getSkills() != null) {
        this.skills = candidate.getSkills().stream()
                .map(SkillDTO::new)
                .collect(Collectors.toList());
    }
}
}
