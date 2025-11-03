package app.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackingItemDTO {
    private String name;
    private int weightInGrams;
    private int quantity;
    private String description;
    private String category;
    private String createdAt; // Kan parse til ZonedDateTime senere
    private String updatedAt;
    private List<BuyingOption> buyingOptions;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuyingOption {
        private String shopName;
        private String shopUrl;
        private double price;
    }
}

