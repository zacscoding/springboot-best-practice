package best.server.member.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import best.server.member.AuthPrincipal;
import best.server.member.MemberAssembler;
import best.server.member.domain.Member;
import best.server.member.dto.MemberDto;
import best.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/account/me")
    public MemberDto getCurrentMember(@AuthPrincipal Member currentMember) {
        return MemberAssembler.toDto(currentMember);
    }
}
