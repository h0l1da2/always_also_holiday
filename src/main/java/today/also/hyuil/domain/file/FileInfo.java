package today.also.hyuil.domain.file;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class FileInfo {

    @Id @GeneratedValue
    private Long id;
    private IsWhere isWhere;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private File file;
}
