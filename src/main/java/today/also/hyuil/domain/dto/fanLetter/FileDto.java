package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;
import today.also.hyuil.domain.file.FileInfo;

@Data
public class FileDto {

    private Long lastModified;
    private String name;

    public FileDto() {
    }

    public FileDto(FileInfo fileInfo) {
        this.lastModified = fileInfo.getFile().getId();
        this.name = fileInfo.getFile().getName();
    }
}
