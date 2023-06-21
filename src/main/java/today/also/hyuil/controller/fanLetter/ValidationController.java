package today.also.hyuil.controller.fanLetter;

import groovy.util.logging.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ValidationController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, Object> invalidRequestHnadler(MethodArgumentNotValidException e) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> invalid = new HashMap<>();

        result.put("code", 400);
        result.put("message", e.getMessage());

        for (FieldError erros : e.getFieldErrors()) {
            invalid.put(erros.getField(), erros.getDefaultMessage());
        }

        result.put("validation", invalid);

        return result;
    }

}
