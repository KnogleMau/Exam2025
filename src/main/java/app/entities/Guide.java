package app.entities;


import app.dtos.GuideDTO;
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
public class Guide {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "guide_id")
  private Integer id;

  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String email;
  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;
  @Column(name = "years_of_experience",nullable = false)
  private int yearsOfExperience;


  @OneToMany(mappedBy = "guide", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private List<Trip> trips = new ArrayList<>();

  public Guide(GuideDTO guideDTO){
      this.id = guideDTO.getId();
      this.name = guideDTO.getName();
      this.email = guideDTO.getEmail();
      this.phoneNumber = guideDTO.getPhoneNumber();
      this.yearsOfExperience = guideDTO.getYearsOfExperience();
  }

    public void addTrip(Trip trip) {
        if ( trip != null) {
            this.trips.add(trip);
            trip.setGuide(this);
        }
    }

}
