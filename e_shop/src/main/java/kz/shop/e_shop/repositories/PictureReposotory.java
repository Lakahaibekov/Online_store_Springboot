package kz.shop.e_shop.repositories;

import kz.shop.e_shop.entities.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PictureReposotory extends JpaRepository<Pictures, Long> {
    List<Pictures> findAllByItemId(Long Item_id);
}
