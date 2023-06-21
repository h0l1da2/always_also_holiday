package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarketSellComRemover is a Querydsl query type for MarketSellComRemover
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarketSellComRemover extends EntityPathBase<MarketSellComRemover> {

    private static final long serialVersionUID = 809021540L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketSellComRemover marketSellComRemover = new QMarketSellComRemover("marketSellComRemover");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final today.also.hyuil.domain.member.QMember member;

    public final DateTimePath<java.util.Date> removeDate = createDateTime("removeDate", java.util.Date.class);

    public final EnumPath<today.also.hyuil.domain.Who> who = createEnum("who", today.also.hyuil.domain.Who.class);

    public QMarketSellComRemover(String variable) {
        this(MarketSellComRemover.class, forVariable(variable), INITS);
    }

    public QMarketSellComRemover(Path<? extends MarketSellComRemover> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarketSellComRemover(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarketSellComRemover(PathMetadata metadata, PathInits inits) {
        this(MarketSellComRemover.class, metadata, inits);
    }

    public QMarketSellComRemover(Class<? extends MarketSellComRemover> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

