package best.server.member.rest;

import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import best.server.member.AuthPrincipal;
import best.server.member.MemberAssembler;
import best.server.member.domain.Member;
import best.server.member.dto.MemberResource;
import best.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/account/me")
    public MemberResource getCurrentMember(@AuthPrincipal Member currentMember) {
        return new MemberResource(MemberAssembler.toDto(currentMember));
    }
}
