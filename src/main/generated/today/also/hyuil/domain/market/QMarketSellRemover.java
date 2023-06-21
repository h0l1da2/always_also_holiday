package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarketSellRemover is a Querydsl query type for MarketSellRemover
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarketSellRemover extends EntityPathBase<MarketSellRemover> {

    private static final long serialVersionUID = 858072855L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketSellRemover marketSellRemover = new QMarketSellRemover("marketSellRemover");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final today.also.hyuil.domain.member.QMember member;

    public final DateTimePath<java.util.Date> removeDate = createDateTime("removeDate", java.util.Date.class);

    public final EnumPath<today.also.hyuil.domain.Who> who = createEnum("who", today.also.hyuil.domain.Who.class);

    public QMarketSellRemover(String variable) {
        this(MarketSellRemover.class, forVariable(variable), INITS);
    }

    public QMarketSellRemover(Path<? extends MarketSellRemover> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarketSellRemover(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarketSellRemover(PathMetadata metadata, PathInits inits) {
        this(MarketSellRemover.class, metadata, inits);
    }

    public QMarketSellRemover(Class<? extends MarketSellRemover> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

