package best.server.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import best.server.common.ReflectionUtil;
import best.server.common.domain.Address;
import best.server.common.domain.BaseEntity;
import best.server.member.MemberDetails;
import best.server.member.domain.Member;
import best.server.member.domain.MemberRole;
import best.server.member.repository.MemberRepository;

public class MemberServiceTest {

    MemberRepository memberRepository = mock(MemberRepository.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    MemberService memberService = new MemberService(passwordEncoder, memberRepository);

    Member user1;
    Member user2;

    @BeforeEach
    public void setUp() {
        user1 = createMember(1L, "user1@gmail.com", "pass", "gildong", "ha",
                             Address.createAddress("seoul", "st2", "521"));

        user2 = createMember(2L, "user2@gmail.com", "pass", "best", "park",
                             Address.createAddress("seoul", "st3", "1234"));
    }

    @Test
    @DisplayName("load member")
    public void testLoadUserByUsername() throws Exception {
        // given
        when(memberRepository.findWithRolesByEmail(eq(user1.getEmail()))).thenReturn(Optional.of(user1));

        // when
        UserDetails userDetails = memberService.loadUserByUsername(user1.getEmail());

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails instanceof MemberDetails).isTrue();
        final Member find = ((MemberDetails) userDetails).getMember();

        assertThat(find)
                .usingRecursiveComparison()
                .ignoringFields(ReflectionUtil.getFieldNames(BaseEntity.class))
                .isEqualTo(user1);
    }

    @Test
    @DisplayName("load member not found")
    public void testLoadUserByUsernameThrowUserNotFoundIfNotExist() {
        // given
        final String email = "not_exist@gmail.com";
        when(memberRepository.findWithRolesByEmail(eq(email))).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> memberService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(email);
    }

    private Member createMember(Long id, String email, String pass, String firstName, String lastName,
                                Address address, MemberRole... roles) {

        final Member member = Member.createMember(email, pass, firstName, lastName,
                                                  new HashSet<>(Arrays.asList(roles)), address);

        try {
            Field field = member.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(member, id);
            assertThat(member.getId()).isEqualTo(id);
            return member;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
