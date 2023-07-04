package today.also.hyuil.repository.market.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import today.also.hyuil.domain.market.MarketSell;

public interface MarketSellJpaRepository extends JpaRepository<MarketSell, Long> {
    @Query(value = "SELECT DISTINCT m FROM MarketSell m LEFT JOIN FETCH m.member",
            countQuery = "SELECT COUNT(DISTINCT m) FROM Member m")
    Page<MarketSell> findAll(Pageable pageable);
}
