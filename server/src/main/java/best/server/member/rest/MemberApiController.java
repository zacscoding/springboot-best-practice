package best.server.member.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import best.server.common.response.CommonApiResponse;
import best.server.member.AuthPrincipal;
import best.server.member.MemberAssembler;
import best.server.member.domain.Member;
import best.server.member.dto.MemberResource;
import best.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/account/me")
    public CommonApiResponse<MemberResource> getCurrentMember(@AuthPrincipal Member currentMember) {
        return CommonApiResponse.createOK(new MemberResource(MemberAssembler.toDto(currentMember)));
    }
}
