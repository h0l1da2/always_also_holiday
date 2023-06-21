package today.also.hyuil.domain.market;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMd is a Querydsl query type for Md
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMd extends EntityPathBase<Md> {

    private static final long serialVersionUID = 1920013888L;

    public static final QMd md = new QMd("md");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public QMd(String variable) {
        super(Md.class, forVariable(variable));
    }

    public QMd(Path<? extends Md> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMd(PathMetadata metadata) {
        super(Md.class, metadata);
    }

}

