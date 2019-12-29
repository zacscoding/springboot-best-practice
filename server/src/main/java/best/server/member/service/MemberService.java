package best.server.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import best.server.member.MemberDetails;
import best.server.member.domain.Member;
import best.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Member member = memberRepository.findWithRolesByEmail(username)
                                              .orElseThrow(() -> new UsernameNotFoundException(username));

        return new MemberDetails(member);
    }

    @Transactional
    public Long joinMember(Member member) {
        Preconditions.checkNotNull(member, "member");

        if (memberRepository.existsMemberByEmail(member.getEmail())) {
            logger.warn("already exist member with " + member.getEmail());
            // TODO : throw EX
            return null;
        }

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.updatePassword(encryptedPassword);

        return memberRepository.save(member).getId();
    }
}
