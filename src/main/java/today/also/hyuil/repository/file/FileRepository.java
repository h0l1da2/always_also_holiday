package today.also.hyuil.repository.file;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.file.QFileInfo;
import today.also.hyuil.domain.file.QFiles;

import javax.persistence.EntityManager;

import java.util.List;

import static today.also.hyuil.domain.file.QFileInfo.*;
import static today.also.hyuil.domain.file.QFiles.*;

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

    public List<FileInfo> selectInfoList(Long letterNum) {
        return query.select(fileInfo)
                .from(fileInfo)
                .where(fileInfo.fanBoard.id.eq(letterNum))
                .leftJoin(fileInfo.file, files)
                .fetchJoin()
                .distinct()
                .fetch();
    }
}
