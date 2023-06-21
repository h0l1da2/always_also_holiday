package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarketComRemover is a Querydsl query type for MarketComRemover
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarketComRemover extends EntityPathBase<MarketComRemover> {

    private static final long serialVersionUID = -76861966L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketComRemover marketComRemover = new QMarketComRemover("marketComRemover");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final today.also.hyuil.domain.member.QMember member;

    public final DateTimePath<java.util.Date> removeDate = createDateTime("removeDate", java.util.Date.class);

    public final EnumPath<today.also.hyuil.domain.Who> who = createEnum("who", today.also.hyuil.domain.Who.class);

    public QMarketComRemover(String variable) {
        this(MarketComRemover.class, forVariable(variable), INITS);
    }

    public QMarketComRemover(Path<? extends MarketComRemover> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarketComRemover(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarketComRemover(PathMetadata metadata, PathInits inits) {
        this(MarketComRemover.class, metadata, inits);
    }

    public QMarketComRemover(Class<? extends MarketComRemover> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

