package today.also.hyuil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewTestController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/market/buyList")
    public String buyList() {
        return "market/buyList";
    }
    @GetMapping("/market/sellList")
    public String sellList() {
        return "market/sellList";
    }
    @GetMapping("/hyuil/intro")
    public String intro() {
        return "hyuil/intro";
    }
    @GetMapping("/hyuil/review")
    public String review() {
        return "hyuil/review";
    }
    @GetMapping("/hyuil/picture")
    public String picture() {
        return "hyuil/picture";
    }
    @GetMapping("/hyuil/music")
    public String music() {
        return "hyuil/music";
    }
    @GetMapping("/fanBoard/list")
    public String boardList() {
        return "fanletter/boardList";
    }
}
