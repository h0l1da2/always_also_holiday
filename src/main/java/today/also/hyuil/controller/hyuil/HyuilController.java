package today.also.hyuil.controller.hyuil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HyuilController {

    @GetMapping("/intro")
    public String intro() {
        return "hyuil/intro";
    }

    @GetMapping("/friends")
    public String friends() {
        return "hyuil/review";
    }
    @GetMapping("/music")
    public String music() {
        return "hyuil/music";
    }
    @GetMapping("/drawing")
    public String drawing() {
        return "hyuil/drawing";
    }
}
