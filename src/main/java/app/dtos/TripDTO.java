package app.dtos;

import app.entities.Trip;
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

public class TripDTO {

    private Integer id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private double price;
    private Category category;
    private GuideDTO guide;

    private List<PackingItemDTO> packingItems = new ArrayList<>();

    public TripDTO(Trip trip){
        this.id = trip.getId();
        this.name = trip.getName();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.location = trip.getLocation();
        this.price = trip.getPrice();
        this.category = trip.getCategory();

        if(trip.getGuide() != null){
            this.guide = new GuideDTO(trip.getGuide());
        }
    }
}
