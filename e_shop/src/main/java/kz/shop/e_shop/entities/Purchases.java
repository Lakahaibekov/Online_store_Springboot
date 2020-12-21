package kz.shop.e_shop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "sold_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Items item;

    @Column(name = "price")
    private int price;

    @Column(name = "purchase_date")
    private Date date;

    @Column(name = "buyer")
    private String buyer;

    @Column(name = "amount")
    private int amount;
}