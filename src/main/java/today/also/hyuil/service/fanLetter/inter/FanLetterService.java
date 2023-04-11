package today.also.hyuil.service.fanLetter.inter;

import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.exception.MemberNotFoundException;

public interface FanLetterService {

    FanBoard writeLetter(String memberId, FanBoard fanBoard) throws MemberNotFoundException;
}
