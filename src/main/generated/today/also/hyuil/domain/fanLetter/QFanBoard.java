package today.also.hyuil.domain.fanLetter;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFanBoard is a Querydsl query type for FanBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFanBoard extends EntityPathBase<FanBoard> {

    private static final long serialVersionUID = -1717873359L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFanBoard fanBoard = new QFanBoard("fanBoard");

    public final QBoardRemover boardRemover;

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final today.also.hyuil.domain.member.QMember member;

    public final StringPath title = createString("title");

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> uploadDate = createDateTime("uploadDate", java.util.Date.class);

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public QFanBoard(String variable) {
        this(FanBoard.class, forVariable(variable), INITS);
    }

    public QFanBoard(Path<? extends FanBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFanBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFanBoard(PathMetadata metadata, PathInits inits) {
        this(FanBoard.class, metadata, inits);
    }

    public QFanBoard(Class<? extends FanBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.boardRemover = inits.isInitialized("boardRemover") ? new QBoardRemover(forProperty("boardRemover"), inits.get("boardRemover")) : null;
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

