package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;

@Data
public class ImageDto {
    // file
    private String fileName;
    // jpg png gif ...
    private String mimeType;
    private Long fileSize;
}
