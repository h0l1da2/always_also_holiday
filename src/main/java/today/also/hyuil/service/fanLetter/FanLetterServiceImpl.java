package today.also.hyuil.service.fanLetter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.repository.fanLetter.FanLetterRepository;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;
import today.also.hyuil.service.member.inter.MemberJoinService;

@Transactional
@Service
public class FanLetterServiceImpl implements FanLetterService {

    private final FanLetterRepository fanLetterRepository;
    private final MemberJoinService memberJoinService;

    public FanLetterServiceImpl(FanLetterRepository fanLetterRepository, MemberJoinService memberJoinService) {
        this.fanLetterRepository = fanLetterRepository;
        this.memberJoinService = memberJoinService;
    }

    @Override
    public FanBoard writeLetter(String memberId, FanBoard fanBoard) throws MemberNotFoundException {
        Member findMember = memberJoinService.findMyAccount(memberId);
        if (findMember == null) {
            throw new MemberNotFoundException("멤버가 없습니다");
        }
        fanBoard.writeMember(findMember);

        return fanLetterRepository.insertFanBoard(fanBoard);
    }
}
