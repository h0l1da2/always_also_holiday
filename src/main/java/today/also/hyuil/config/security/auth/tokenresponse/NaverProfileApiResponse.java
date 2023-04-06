package today.also.hyuil.config.security.auth.tokenresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverProfileApiResponse {

    @JsonProperty("resultcode")
    private String resultCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("response/id")
    private String id;
    @JsonProperty("response/nickname")
    private String nickname;
    @JsonProperty("response/name")
    private String name;
    @JsonProperty("response/email")
    private String email;
    @JsonProperty("response/gender")
    private String gender;
    @JsonProperty("response/age")
    private String age;
    @JsonProperty("response/birthday")
    private String birthday;
    @JsonProperty("response/profile_image")
    private String profileImage;
    @JsonProperty("response/birthyear")
    private String birthYear;
    @JsonProperty("response/mobile")
    private String mobile;







}
