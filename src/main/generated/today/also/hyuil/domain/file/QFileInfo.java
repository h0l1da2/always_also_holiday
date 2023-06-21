package today.also.hyuil.domain.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFileInfo is a Querydsl query type for FileInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileInfo extends EntityPathBase<FileInfo> {

    private static final long serialVersionUID = 1834747571L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFileInfo fileInfo = new QFileInfo("fileInfo");

    public final today.also.hyuil.domain.fanLetter.QFanBoard fanBoard;

    public final QFiles file;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<IsWhere> isWhere = createEnum("isWhere", IsWhere.class);

    public final today.also.hyuil.domain.market.QMarket market;

    public final today.also.hyuil.domain.market.QMarketSell marketSell;

    public QFileInfo(String variable) {
        this(FileInfo.class, forVariable(variable), INITS);
    }

    public QFileInfo(Path<? extends FileInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFileInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFileInfo(PathMetadata metadata, PathInits inits) {
        this(FileInfo.class, metadata, inits);
    }

    public QFileInfo(Class<? extends FileInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fanBoard = inits.isInitialized("fanBoard") ? new today.also.hyuil.domain.fanLetter.QFanBoard(forProperty("fanBoard"), inits.get("fanBoard")) : null;
        this.file = inits.isInitialized("file") ? new QFiles(forProperty("file")) : null;
        this.market = inits.isInitialized("market") ? new today.also.hyuil.domain.market.QMarket(forProperty("market"), inits.get("market")) : null;
        this.marketSell = inits.isInitialized("marketSell") ? new today.also.hyuil.domain.market.QMarketSell(forProperty("marketSell"), inits.get("marketSell")) : null;
    }

}

