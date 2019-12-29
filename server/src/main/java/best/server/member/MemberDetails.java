package best.server.member;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import best.server.member.domain.Member;
import best.server.member.domain.MemberRole;

public class MemberDetails extends User {

    private final Member member;

    public MemberDetails(Member member) {
        super(member.getEmail(), member.getPassword(), authorities(member.getRoles()));
        this.member = member;
    }

    private static Collection<? extends GrantedAuthority> authorities(Set<MemberRole> roles) {
        return roles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                    .collect(Collectors.toSet());
    }

    public Member getMember() {
        return member;
    }
}
