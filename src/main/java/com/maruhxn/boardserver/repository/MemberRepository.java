package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    List<Member> findByEmailOrUsername(String email, String username);
}
