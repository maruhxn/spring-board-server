package com.maruhxn.boardserver.auth;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AjaxUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.NOT_FOUND_USER.getMessage()));
        return new AccountContext(findMember);
    }
}
