package best.server.member;

import com.google.common.base.Preconditions;

import best.server.member.domain.Member;
import best.server.member.dto.MemberDto;

/**
 * Converter between {@link Member} and {@link MemberDto}
 */
public class MemberAssembler {

    /**
     * Convert to {@link MemberDto} from {@link MemberDto}
     */
    public static MemberDto toDto(Member member) {
        Preconditions.checkNotNull(member, "member");

        return MemberDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .password(member.getPassword())
                        .firstName(member.getFirstName())
                        .lastName(member.getLastName())
                        .roles(member.getRoles())
                        .address(member.getAddress())
                        .build();
    }

    /**
     * Convert to {@link Member} from {@link MemberDto}
     */
    public static Member toEntity(MemberDto memberDto) {
        return Member.createMember(memberDto.getEmail(), memberDto.getPassword(),
                                   memberDto.getFirstName(), memberDto.getLastName(),
                                   memberDto.getRoles(), memberDto.getAddress());
    }

    private MemberAssembler() {
    }
}
