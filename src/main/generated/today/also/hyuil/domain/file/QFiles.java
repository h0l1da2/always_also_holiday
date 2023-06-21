package today.also.hyuil.domain.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFiles is a Querydsl query type for Files
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFiles extends EntityPathBase<Files> {

    private static final long serialVersionUID = -1025563474L;

    public static final QFiles files = new QFiles("files");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mimeType = createString("mimeType");

    public final StringPath name = createString("name");

    public final StringPath path = createString("path");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final EnumPath<Type> type = createEnum("type", Type.class);

    public final StringPath uuid = createString("uuid");

    public QFiles(String variable) {
        super(Files.class, forVariable(variable));
    }

    public QFiles(Path<? extends Files> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFiles(PathMetadata metadata) {
        super(Files.class, metadata);
    }

}

