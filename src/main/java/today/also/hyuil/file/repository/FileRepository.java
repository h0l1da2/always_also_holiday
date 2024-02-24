package today.also.hyuil.file.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import today.also.hyuil.file.domain.FileInfo;

import java.util.List;

import static today.also.hyuil.domain.file.QFileInfo.fileInfo;
import static today.also.hyuil.domain.file.QFiles.files;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final JPAQueryFactory query;

    public List<FileInfo> selectInfoListForFanBoard(Long letterNum) {
        return query.select(fileInfo)
                .from(fileInfo)
                .where(fileInfo.fanBoard.id.eq(letterNum))
                .leftJoin(fileInfo.file, files)
                .fetchJoin()
                .distinct()
                .fetch();
    }
    public List<FileInfo> selectInfoListForMarket(Long marketId) {
        return query.select(fileInfo)
                .from(fileInfo)
                .where(fileInfo.marketSell.id.eq(marketId))
                .leftJoin(fileInfo.file, files)
                .fetchJoin()
                .distinct()
                .fetch();
    }
}
