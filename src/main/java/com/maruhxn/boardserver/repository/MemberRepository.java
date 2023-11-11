package com.maruhxn.boardserver.repository;

import com.maruhxn.boardserver.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findOne(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findByEmail(String email) {
        try {
            return Optional.ofNullable(em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
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
