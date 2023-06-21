package today.also.hyuil.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -608904539L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final QAddress address;

    public final QAdmin admin;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> joinDate = createDateTime("joinDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastLogin = createDateTime("lastLogin", java.util.Date.class);

    public final StringPath memberId = createString("memberId");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final DateTimePath<java.util.Date> pwdModifyDate = createDateTime("pwdModifyDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> removeDate = createDateTime("removeDate", java.util.Date.class);

    public final QRole role;

    public final EnumPath<Sns> sns = createEnum("sns", Sns.class);

    public final DateTimePath<java.util.Date> stopDate = createDateTime("stopDate", java.util.Date.class);

    public final StringPath whyStop = createString("whyStop");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
        this.admin = inits.isInitialized("admin") ? new QAdmin(forProperty("admin"), inits.get("admin")) : null;
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role")) : null;
    }

}

