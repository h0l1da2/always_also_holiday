package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarketCom is a Querydsl query type for MarketCom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarketCom extends EntityPathBase<MarketCom> {

    private static final long serialVersionUID = -2116824356L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketCom marketCom = new QMarketCom("marketCom");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMarket market;

    public final QMarketComRemover marketComRemover;

    public final today.also.hyuil.domain.member.QMember member;

    public final EnumPath<today.also.hyuil.domain.fanLetter.ReplyType> replyType = createEnum("replyType", today.also.hyuil.domain.fanLetter.ReplyType.class);

    public final NumberPath<Long> rootId = createNumber("rootId", Long.class);

    public final DateTimePath<java.util.Date> uploadDate = createDateTime("uploadDate", java.util.Date.class);

    public QMarketCom(String variable) {
        this(MarketCom.class, forVariable(variable), INITS);
    }

    public QMarketCom(Path<? extends MarketCom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarketCom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarketCom(PathMetadata metadata, PathInits inits) {
        this(MarketCom.class, metadata, inits);
    }

    public QMarketCom(Class<? extends MarketCom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.market = inits.isInitialized("market") ? new QMarket(forProperty("market"), inits.get("market")) : null;
        this.marketComRemover = inits.isInitialized("marketComRemover") ? new QMarketComRemover(forProperty("marketComRemover"), inits.get("marketComRemover")) : null;
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

