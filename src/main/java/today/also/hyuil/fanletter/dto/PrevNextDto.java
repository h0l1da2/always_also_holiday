package today.also.hyuil.fanletter.dto;

import lombok.Data;

@Data
public class PrevNextDto {

    private Long id;
    private String title;

    protected PrevNextDto() {

    }

    public PrevNextDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
