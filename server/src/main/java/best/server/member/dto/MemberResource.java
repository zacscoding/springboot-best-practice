package best.server.member.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import best.server.member.rest.MemberApiController;

public class MemberResource extends EntityModel<MemberDto> {

    public MemberResource(MemberDto memberDto, Link... links) {
        super(memberDto, links);

        add(linkTo(MemberApiController.class)
                    .slash("account")
                    .slash("me")
                    .withSelfRel());
    }
}
