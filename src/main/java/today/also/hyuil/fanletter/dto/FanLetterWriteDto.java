package today.also.hyuil.fanletter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FanLetterWriteDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public FanLetterWriteDto() {

    }

    public FanLetterWriteDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

