package today.also.hyuil.repository.fanLetter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import today.also.hyuil.domain.fanLetter.FanBoard;

public interface FanLetterJpaRepository extends Repository<FanBoard, Long> {
    // 셀렉트올 + 디스팅크트 + 페치조인 + 카운팅을 하면 자동 페이징 가능
    @Query(value = "SELECT DISTINCT f FROM FanBoard f LEFT JOIN FETCH f.member WHERE f.boardRemover IS NULL",
            countQuery = "SELECT COUNT(DISTINCT f) FROM FanBoard f")
    Page<FanBoard> findAll(Pageable pageable);
}
