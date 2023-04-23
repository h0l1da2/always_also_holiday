package today.also.hyuil.repository.file;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.file.FileInfo;

import javax.persistence.EntityManager;

@Transactional
@Repository
public class FileRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public FileRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public FileInfo insertFileInfo(FileInfo fileInfo) {
        em.persist(fileInfo);
        em.close();
        return fileInfo;
    }
}
