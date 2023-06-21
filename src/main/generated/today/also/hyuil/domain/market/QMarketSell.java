package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarketSell is a Querydsl query type for MarketSell
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarketSell extends EntityPathBase<MarketSell> {

    private static final long serialVersionUID = -1196578473L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketSell marketSell = new QMarketSell("marketSell");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMarketSellRemover marketSellRemover;

    public final QMd md;

    public final today.also.hyuil.domain.member.QMember member;

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final StringPath title = createString("title");

    public final EnumPath<Trade> trade = createEnum("trade", Trade.class);

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> uploadDate = createDateTime("uploadDate", java.util.Date.class);

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public QMarketSell(String variable) {
        this(MarketSell.class, forVariable(variable), INITS);
    }

    public QMarketSell(Path<? extends MarketSell> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarketSell(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarketSell(PathMetadata metadata, PathInits inits) {
        this(MarketSell.class, metadata, inits);
    }

    public QMarketSell(Class<? extends MarketSell> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.marketSellRemover = inits.isInitialized("marketSellRemover") ? new QMarketSellRemover(forProperty("marketSellRemover"), inits.get("marketSellRemover")) : null;
        this.md = inits.isInitialized("md") ? new QMd(forProperty("md")) : null;
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

