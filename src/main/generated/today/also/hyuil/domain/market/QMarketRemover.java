package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarketRemover is a Querydsl query type for MarketRemover
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarketRemover extends EntityPathBase<MarketRemover> {

    private static final long serialVersionUID = 72893513L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketRemover marketRemover = new QMarketRemover("marketRemover");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final today.also.hyuil.domain.member.QMember member;

    public final DateTimePath<java.util.Date> removeDate = createDateTime("removeDate", java.util.Date.class);

    public final EnumPath<today.also.hyuil.domain.Who> who = createEnum("who", today.also.hyuil.domain.Who.class);

    public QMarketRemover(String variable) {
        this(MarketRemover.class, forVariable(variable), INITS);
    }

    public QMarketRemover(Path<? extends MarketRemover> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarketRemover(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarketRemover(PathMetadata metadata, PathInits inits) {
        this(MarketRemover.class, metadata, inits);
    }

    public QMarketRemover(Class<? extends MarketRemover> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

