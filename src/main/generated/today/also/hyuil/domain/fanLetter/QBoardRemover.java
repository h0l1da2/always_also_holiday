package today.also.hyuil.domain.fanLetter;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardRemover is a Querydsl query type for BoardRemover
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardRemover extends EntityPathBase<BoardRemover> {

    private static final long serialVersionUID = -909491898L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardRemover boardRemover = new QBoardRemover("boardRemover");

    public final today.also.hyuil.domain.member.QAdmin admin;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final today.also.hyuil.domain.member.QMember member;

    public final DateTimePath<java.util.Date> removeDate = createDateTime("removeDate", java.util.Date.class);

    public QBoardRemover(String variable) {
        this(BoardRemover.class, forVariable(variable), INITS);
    }

    public QBoardRemover(Path<? extends BoardRemover> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardRemover(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardRemover(PathMetadata metadata, PathInits inits) {
        this(BoardRemover.class, metadata, inits);
    }

    public QBoardRemover(Class<? extends BoardRemover> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new today.also.hyuil.domain.member.QAdmin(forProperty("admin"), inits.get("admin")) : null;
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

