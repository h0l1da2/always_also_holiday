package today.also.hyuil.service.file.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import today.also.hyuil.domain.dto.market.sell.SellListDto;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.market.MarketSell;

import java.util.List;

public interface FileService {

    FileInfo saveFileInfo(FileInfo fileInfo);
    List<FileInfo> fileInfoListForFanBoard(Long letterNum);
    List<FileInfo> fileInfoListForMarket(Long marketId);
    Page<FileInfo> fileInfoListForMarketSellList(Pageable pageable);
}
