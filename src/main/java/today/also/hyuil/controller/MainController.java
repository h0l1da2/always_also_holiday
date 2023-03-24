package today.also.hyuil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String main() {
        System.out.println("메인컨트롤러");
        return "index";
    }
}
