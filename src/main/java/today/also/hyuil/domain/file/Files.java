package today.also.hyuil.domain.file;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "file")
public class Files {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String uuid;
    private String path;

    @Enumerated(EnumType.STRING)
    private Type type;
    private String mimeType;
    private Long size;

    public Files() {}

    public Files(MultipartFile multipartFile) {
        this.name = multipartFile.getOriginalFilename();
        this.size = multipartFile.getSize();
    }

    public void filePath(String path) {
        this.path = path;
    }

    public void fileType(Type type) {
        this.type = type;
    }

    public void imgMimeType(String imgMimeType) {
        this.mimeType = imgMimeType;
    }

    public void fileUUID(String uuid) {
        this.uuid = uuid;
    }
}