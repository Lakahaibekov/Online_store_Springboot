package kz.shop.e_shop.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int amount;
    private int stars; // Just rating, from 0 to 5
    private String pictureUrl;
}
