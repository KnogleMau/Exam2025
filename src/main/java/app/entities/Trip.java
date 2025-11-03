package app.entities;

import app.dtos.TripDTO;
import app.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(nullable = false)
    private LocalDateTime endTime;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;


    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;



    public Trip(TripDTO tripDTO){
        this.name = tripDTO.getName();
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.location = tripDTO.getLocation();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
    }
}
