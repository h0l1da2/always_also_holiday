package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarket is a Querydsl query type for Market
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarket extends EntityPathBase<Market> {

    private static final long serialVersionUID = 193405029L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarket market = new QMarket("market");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMarketRemover marketRemover;

    public final QMd md;

    public final today.also.hyuil.domain.member.QMember member;

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final StringPath title = createString("title");

    public final EnumPath<Trade> trade = createEnum("trade", Trade.class);

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> uploadDate = createDateTime("uploadDate", java.util.Date.class);

    public final NumberPath<Long> view = createNumber("view", Long.class);

    public QMarket(String variable) {
        this(Market.class, forVariable(variable), INITS);
    }

    public QMarket(Path<? extends Market> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarket(PathMetadata metadata, PathInits inits) {
        this(Market.class, metadata, inits);
    }

    public QMarket(Class<? extends Market> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.marketRemover = inits.isInitialized("marketRemover") ? new QMarketRemover(forProperty("marketRemover"), inits.get("marketRemover")) : null;
        this.md = inits.isInitialized("md") ? new QMd(forProperty("md")) : null;
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

