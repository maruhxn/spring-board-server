package com.maruhxn.boardserver.repository.jpa;

import com.maruhxn.boardserver.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findOne(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public Optional<Member> findByEmail(String email) {
        List<Member> findMember = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
        return findMember.stream().findFirst();
    }

    public List<Member> findByEmailOrUsername(String email, String username) {
        return em.createQuery("select m from Member m where m.email = :email or  m.username = :username", Member.class)
                .setParameter("email", email)
                .setParameter("username", username)
                .getResultList();
    }

    public void removeOne(Member member) {
        em.remove(member);
    }

}
