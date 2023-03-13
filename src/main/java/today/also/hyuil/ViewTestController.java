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
//    @GetMapping("/fanMeet")
//    public String fanMeet() {
//        return "fanmeet/fanMeetMain";
//    }
//    @GetMapping("/fanBoard/page")
//    public String viewPage() {
//        return "fanletter/viewPage";
//    }
//    @GetMapping("/market/write")
//    public String marketWrite() {
//        return "market/write";
//    }
    @GetMapping("/fanMeet/list")
    public String fanMeetList() {
        return "fanmeet/meetList";
    }
    @GetMapping("/loginForm")
    public String loginForm() {
        return "member/loginForm";
    }
    @GetMapping("/info")
    public String memberInfo() {
        return "member/info/infoMain";
    }
    @GetMapping("/infoForm")
    public String memberInfoForm() {
        return "member/info/information/infoForm";
    }
    @GetMapping("/info/password")
    public String passwordForm() {
        return "member/info/information/passwordForm";
    }
    @GetMapping("/info/email")
    public String emailForm() {
        return "member/info/information/emailForm";
    }
    @GetMapping("/info/phone")
    public String phoneForm() {
        return "member/info/information/phoneForm";
    }
    @GetMapping("/info/fanLetter")
    public String fanLetterList() {
        return "member/info/fanLetterList";
    }
    @GetMapping("/info/delivery")
    public String deliveryList() {
        return "member/info/delivery/main";
    }
    @GetMapping("/info/orderForm")
    public String orderForm() {
        return "member/info/delivery/orderForm";
    }
    @GetMapping("/info/marketList")
    public String marketList() {
        return "member/info/marketList";
    }
    @GetMapping("/info/delete")
    public String deleteForm() {
        return "member/info/deleteForm";
    }
    @GetMapping("/market/view1")
    public String sellView() {
        return "market/sellView";
    }
    @GetMapping("/market/view2")
    public String buyView() {
        return "market/buyView";
    }


}
