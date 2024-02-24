package today.also.hyuil.fanletter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import today.also.hyuil.fanletter.domain.FanBoard;

public interface FanLetterJpaRepository extends JpaRepository<FanBoard, Long> {
    // 셀렉트올 + 디스팅크트 + 페치조인 + 카운팅을 하면 자동 페이징 가능
    @Query(value = "SELECT DISTINCT f FROM FanBoard f LEFT JOIN FETCH f.member WHERE f.boardRemover IS NULL",
            countQuery = "SELECT COUNT(DISTINCT f) FROM FanBoard f")
    Page<FanBoard> findAll(Pageable pageable);
}
