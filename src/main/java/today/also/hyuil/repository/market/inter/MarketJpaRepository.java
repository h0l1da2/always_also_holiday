package today.also.hyuil.repository.market.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import today.also.hyuil.domain.market.Market;

public interface MarketJpaRepository extends JpaRepository<Market, Long> {

    @Query(value = "SELECT DISTINCT m FROM Market m LEFT JOIN FETCH m.member",
            countQuery = "SELECT COUNT(DISTINCT m) FROM Member m")
    Page<Market> findAll(Pageable pageable);
}
