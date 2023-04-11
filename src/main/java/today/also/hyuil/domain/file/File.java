package today.also.hyuil.domain.file;

import lombok.Getter;
import today.also.hyuil.domain.dto.fanLetter.FanLetterWriteDto;
import today.also.hyuil.domain.dto.fanLetter.ImageDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
public class File {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String uuid;
    private String path;

    @Enumerated(EnumType.STRING)
    private Type type;
    private String mimeType;
    private Long size;

    public File() {}

    public File(ImageDto imageDto) {
        this.name = imageDto.getFileName();
        this.size = imageDto.getFileSize();
        this.uuid = UUID.randomUUID().toString();
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
}