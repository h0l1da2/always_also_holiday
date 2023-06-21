package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMarketSellCom is a Querydsl query type for MarketSellCom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMarketSellCom extends EntityPathBase<MarketSellCom> {

    private static final long serialVersionUID = 959335594L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketSellCom marketSellCom = new QMarketSellCom("marketSellCom");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMarketSell market;

    public final QMarketSellComRemover marketSellComRemover;

    public final today.also.hyuil.domain.member.QMember member;

    public final EnumPath<today.also.hyuil.domain.fanLetter.ReplyType> replyType = createEnum("replyType", today.also.hyuil.domain.fanLetter.ReplyType.class);

    public final NumberPath<Long> rootId = createNumber("rootId", Long.class);

    public final DateTimePath<java.util.Date> uploadDate = createDateTime("uploadDate", java.util.Date.class);

    public QMarketSellCom(String variable) {
        this(MarketSellCom.class, forVariable(variable), INITS);
    }

    public QMarketSellCom(Path<? extends MarketSellCom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMarketSellCom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMarketSellCom(PathMetadata metadata, PathInits inits) {
        this(MarketSellCom.class, metadata, inits);
    }

    public QMarketSellCom(Class<? extends MarketSellCom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.market = inits.isInitialized("market") ? new QMarketSell(forProperty("market"), inits.get("market")) : null;
        this.marketSellComRemover = inits.isInitialized("marketSellComRemover") ? new QMarketSellComRemover(forProperty("marketSellComRemover"), inits.get("marketSellComRemover")) : null;
        this.member = inits.isInitialized("member") ? new today.also.hyuil.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

