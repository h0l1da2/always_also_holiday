package today.also.hyuil.domain.file;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class FileInfo {

    @Id @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private IsWhere isWhere;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private File file;

    public FileInfo() {}

    public FileInfo(File file) {
        this.file = file;
    }

    public void whereFileIs(IsWhere isWhere) {
        this.isWhere = isWhere;
    }
}
