package best.server.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import best.server.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByEmail(String email);

    /**
     * Returns a {@link Member} or empty given email
     */
    Optional<Member> findByEmail(String email);

    /**
     * Returns a {@link Member} with roles or empty given email
     * @param email
     * @return
     */
    @EntityGraph(attributePaths = "roles")
    Optional<Member> findWithRolesByEmail(String email);
}
