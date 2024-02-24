package today.also.hyuil.file.dto;

import lombok.Data;
import today.also.hyuil.file.domain.FileInfo;

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
