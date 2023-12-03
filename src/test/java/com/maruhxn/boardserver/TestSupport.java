package com.maruhxn.boardserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maruhxn.boardserver.common.Constants;
import com.maruhxn.boardserver.domain.Member;
import com.maruhxn.boardserver.domain.Role;
import com.maruhxn.boardserver.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfiguration.class)
public class TestSupport {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Member member;

    protected Member admin;

    protected String MEMBER_API_PATH;

    @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();

        member = memberRepository.save(
                Member.builder()
                        .email("test@test.com")
                        .username("tester")
                        .password(passwordEncoder.encode("test"))
                        .role(Role.ROLE_USER)
                        .profileImage(Constants.BASIC_PROFILE_IMAGE_NAME)
                        .build());

        admin = memberRepository.save(
                Member.builder()
                        .email("admin@test.com")
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ROLE_ADMIN)
                        .profileImage(Constants.BASIC_PROFILE_IMAGE_NAME)
                        .build());

        MEMBER_API_PATH = "/members/" + member.getId();
    }
}
