package app.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillStatsDTO {

    private String id;
    private String slug;
    private String name;
    private String categoryKey;
    private String description;
    private Integer popularityScore;
    private Integer averageSalary;
    private ZonedDateTime updatedAt;
}

