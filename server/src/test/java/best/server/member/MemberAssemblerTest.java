package best.server.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import best.server.common.domain.Address;
import best.server.member.domain.Member;
import best.server.member.domain.MemberRole;
import best.server.member.dto.MemberDto;

public class MemberAssemblerTest {

    @Test
    @DisplayName("convert to dto")
    public void testToDto() {
        // given
        final Member member = createMember("zac@gmail.com", "pass", "zac", "kim",
                                           Address.createAddress("city", "street1", "1234"));

        // when
        final MemberDto dto = MemberAssembler.toDto(member);

        // then
        assertThat(member.getEmail()).isEqualTo(dto.getEmail());
        assertThat(member.getPassword()).isEqualTo(dto.getPassword());
        assertThat(member.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(member.getLastName()).isEqualTo(dto.getLastName());
        assertThat(member.getRoles()).isEqualTo(dto.getRoles());
        assertThat(member.getAddress()).isEqualTo(dto.getAddress());
    }

    @Test
    @DisplayName("convert to entity")
    public void testToEntity() {
        // given
        final MemberDto dto = createMemberDto("zac@gmail.com", "pass", "zac", "kim",
                                              Address.createAddress("city", "street1", "1234"));

        // when
        final Member member = MemberAssembler.toEntity(dto);

        // then
        assertThat(dto.getEmail()).isEqualTo(member.getEmail());
        assertThat(dto.getPassword()).isEqualTo(member.getPassword());
        assertThat(dto.getFirstName()).isEqualTo(member.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(member.getLastName());
        assertThat(dto.getRoles()).isEqualTo(member.getRoles());
        assertThat(dto.getAddress()).isEqualTo(member.getAddress());
    }

    private Member createMember(String email, String pass, String firstName, String lastName,
                                Address address, MemberRole... roles) {

        return Member.createMember(
                email,
                pass,
                firstName,
                lastName,
                new HashSet<>(Arrays.asList(roles)),
                address
        );
    }

    private MemberDto createMemberDto(String email, String pass, String firstName, String lastName,
                                      Address address, MemberRole... roles) {

        return MemberDto.builder()
                        .email(email)
                        .password(pass)
                        .firstName(firstName)
                        .lastName(lastName)
                        .address(address)
                        .roles(new HashSet<>(Arrays.asList(roles)))
                        .build();
    }
}
