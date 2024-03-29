package today.also.hyuil.member.service;

import today.also.hyuil.member.domain.type.Mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailService {
    /**
     * 1. 유저의 이메일을 받음
     * 2. 메일 보낼 준비
     * 2-1. 코드 생성
     * 2-2. 템플릿 생성
     * 2-3. 전송
     * 3. return 코드
     */
    String mailSend(Mail mail, String userEmail) throws MessagingException; // 1, 3
    String createCode(); // 2-1
    Message setTemplate(Mail type, String userEmail, String randomCode) throws MessagingException;
    String getContext(String key, String value, String template);
    void sendMail(MimeMessage message); // 2-3

}
