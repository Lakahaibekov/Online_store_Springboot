package kz.shop.e_shop.repositories;


import kz.shop.e_shop.entities.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BrandRepository  extends JpaRepository<Brands, Long> {
}
