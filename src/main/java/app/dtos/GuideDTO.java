package app.dtos;

import app.entities.Guide;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class GuideDTO {

private Integer id;
private String name;
private String email;
private String phoneNumber;
private int yearsOfExperience;


//private List<TripDTO> trips = new ArrayList<>();


public GuideDTO(Guide guide){
    this.id = guide.getId();
    this.name = guide.getName();
    this.email = guide.getEmail();
    this.phoneNumber = guide.getPhoneNumber();
    this.yearsOfExperience = guide.getYearsOfExperience();
}
}
