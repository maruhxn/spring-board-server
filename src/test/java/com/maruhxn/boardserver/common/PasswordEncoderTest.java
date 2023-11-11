package com.maruhxn.boardserver.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {
    @Test
    void shouldReturnHashedPassword() {
        // Given
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String email = "test@test.com";
        String password = "password!";

        // When
        String hashedPassword = passwordEncoder.encode(email, password);

        // Then
        assertThat(hashedPassword.length()).isEqualTo(24);
    }

    @Test
    void test() {
        // Given
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String email = "test@test.com";
        String password = "password!";
        String hackedPassword = "hacked!!";
        String hashedPassword = "l5oTolPhAdIMsponHwsoEQ==";
        // When
        Boolean isMatch1 = passwordEncoder.isMatch(email, password, hashedPassword);
        Boolean isMatch2 = passwordEncoder.isMatch(email, hackedPassword, hashedPassword);
        // Then
        assertThat(isMatch1).isTrue();
        assertThat(isMatch2).isFalse();

    }
}