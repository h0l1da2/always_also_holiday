package today.also.hyuil.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = -1554774556L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdmin admin = new QAdmin("admin");

    public final StringPath adminId = createString("adminId");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final QRole role;

    public QAdmin(String variable) {
        this(Admin.class, forVariable(variable), INITS);
    }

    public QAdmin(Path<? extends Admin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdmin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdmin(PathMetadata metadata, PathInits inits) {
        this(Admin.class, metadata, inits);
    }

    public QAdmin(Class<? extends Admin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role")) : null;
    }

}

