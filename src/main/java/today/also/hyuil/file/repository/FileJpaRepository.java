package today.also.hyuil.file.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import today.also.hyuil.file.domain.FileInfo;

public interface FileJpaRepository extends JpaRepository<FileInfo, Long> {
    @Query(value = "SELECT DISTINCT f FROM FileInfo f LEFT JOIN FETCH f.file WHERE f.marketSell IS NOT NULL",
            countQuery = "SELECT COUNT(DISTINCT f) FROM FileInfo f")
    Page<FileInfo> findAll(Pageable pageable);
}
