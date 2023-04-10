package today.also.hyuil.domain.file;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class File {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String uuid;
    private String path;
    private Type type;
    private Integer size;
}