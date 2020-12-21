package kz.shop.e_shop.repositories;

import kz.shop.e_shop.entities.Brands;
import kz.shop.e_shop.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Items, Long> {

    List<Items> findAllByIdIsNotNullOrderByInTopPageDesc();
    List<Items> findAllByBrandNameEquals(String brand_name);
    //List<Items> findAllByNameAndBrandNameEqualsOrderByPriceAsc(String name, String brand_name);
    List<Items> findAllByNameIsStartingWithAndBrandNameOrderByPriceAsc(String name , String brand_name);
    List<Items> findAllByNameIsStartingWithOrderByPriceAsc(String name);
    List<Items> findAllByNameIsStartingWithOrderByPriceDesc(String name);
    List<Items> findAllByNameIsStartingWithAndPriceBetweenOrderByPriceAsc(String name, double price1, double price2);
    List<Items> findAllByNameIsStartingWithAndPriceBetweenOrderByPriceDesc(String name, double price1, double price2);

}
