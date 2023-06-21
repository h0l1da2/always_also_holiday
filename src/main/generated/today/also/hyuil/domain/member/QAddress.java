package today.also.hyuil.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = 502222153L;

    public static final QAddress address1 = new QAddress("address1");

    public final StringPath address = createString("address");

    public final StringPath detail = createString("detail");

    public final StringPath extraAddress = createString("extraAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath postcode = createString("postcode");

    public QAddress(String variable) {
        super(Address.class, forVariable(variable));
    }

    public QAddress(Path<? extends Address> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddress(PathMetadata metadata) {
        super(Address.class, metadata);
    }

}

