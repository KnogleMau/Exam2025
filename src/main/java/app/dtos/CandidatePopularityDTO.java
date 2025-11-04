package app.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


public class CandidatePopularityDTO {
    private int candidateId;
    private String candidateName;
    private Double averagePopularityScore;


    public CandidatePopularityDTO(int candidateId, String candidateName,Double averagePopularityScore){
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.averagePopularityScore = averagePopularityScore;
    }
}
