package today.also.hyuil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewTestController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/marketList")
    public String marketList() {
        return "market/marketList";
    }

    @GetMapping("/hyuil/intro")
    public String intro() {
        return "hyuil/intro";
    }
}
