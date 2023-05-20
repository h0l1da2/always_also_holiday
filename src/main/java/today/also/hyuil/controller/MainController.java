package today.also.hyuil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final String BASE_URL = "https://alwaysalsoholiday.com";

    @GetMapping
    public String main() {
        return "index";
    }

    @GetMapping("/login/oauth2/{sns}")
    public String snsLoginMain() {
        return "index";
    }


    // TODO 로그인 폼 로그인 버튼 안 눌리는 거 수정 (널포인트뜸)
}
