package today.also.hyuil.common.exception;

import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
public enum Error {

    BOARD_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.SC_NOT_FOUND),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.SC_NOT_FOUND),
    MEMBER_NOT_FOUND("멤버를 찾을 수 없습니다.", HttpStatus.SC_NOT_FOUND),

    ACCESS_DENIED("접근 권한 없음.", HttpStatus.SC_FORBIDDEN),
    FILE_COUNT_UPLOAD_FAILED("파일 업로드 실패 : 업로드 갯수를 확인하세요.", HttpStatus.SC_BAD_REQUEST),

    ;

    private final String message;
    private final int code;

    Error(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
