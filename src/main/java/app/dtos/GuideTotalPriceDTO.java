package app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


public class GuideTotalPriceDTO {
    private int guideId;
    private Double totalPrice;


    public GuideTotalPriceDTO(int guideId, Double totalPrice){
        this.guideId = guideId;
        this.totalPrice = totalPrice;
    }
}
