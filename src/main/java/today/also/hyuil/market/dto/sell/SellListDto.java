package today.also.hyuil.market.dto.sell;

import lombok.Data;
import today.also.hyuil.file.domain.FileInfo;
import today.also.hyuil.market.domain.MarketSell;

import java.util.Date;

@Data
public class SellListDto {

    private Long id;
    private String title;
    private String nickname;
    private Date uploadDate;
    private Long view;
    private String filePath;

    public SellListDto() {
    }

    public SellListDto(MarketSell marketSell) {
        this.id = marketSell.getId();
        this.title = marketSell.getTitle();
        this.nickname = marketSell.getMember().getNickname();
        this.uploadDate = marketSell.getUploadDate();
        this.view = marketSell.getView();
    }

    public void pathForList(FileInfo fileInfo) {
        this.filePath = fileInfo.getFile().getPath() + fileInfo.getFile().getUuid() + fileInfo.getFile().getMimeType();
        int startPath = "/Users/holiday/IdeaProjects/also_hyuil/src/main/resources".length();
        this.filePath = filePath.substring(startPath);
    }
}
