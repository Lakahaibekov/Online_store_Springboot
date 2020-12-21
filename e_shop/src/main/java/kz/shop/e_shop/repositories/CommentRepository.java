package kz.shop.e_shop.repositories;

import kz.shop.e_shop.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByItemId(Long item_id);
}
