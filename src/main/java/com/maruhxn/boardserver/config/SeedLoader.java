package com.maruhxn.boardserver.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
//@Component
@RequiredArgsConstructor
public class SeedLoader {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private int TOTAL_SIZE = 100;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {// 더미 데이터 생성
        long beforeTime = System.currentTimeMillis();
        log.info("더미 데이터 생성 시작!");
        batchInsertMember();
        log.info("Member 생성 완료");
        batchInsertPost();
        log.info("Post 생성 완료");
        batchInsertComment();
        log.info("Comment 생성 완료");
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime = afterTime - beforeTime; // 두 개의 실행 시간
        log.info("실행 시간(ms) = {}", diffTime);
    }

    private void batchInsertMember() {
        String sql = "INSERT INTO member" +
                " (username, email, password, profile_image, created_at, updated_at)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String email = String.format("test%d@test.com", i + 1);
                ps.setString(1, String.format("tester%d", i + 1));
                ps.setString(2, email);
                ps.setString(3, passwordEncoder.encode("test"));
                ps.setString(4, "/img/defaultProfileImage.jfif");
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return TOTAL_SIZE;
            }
        });
    }

    private void batchInsertPost() {
        String sql = "INSERT INTO post" +
                " (member_id, title, content, view_count, created_at, updated_at)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, i + 1);
                ps.setString(2, "title");
                ps.setString(3, "content");
                ps.setLong(4, 0);
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return TOTAL_SIZE;
            }
        });
    }

    private void batchInsertComment() {
        String sql = "INSERT INTO comment" +
                " (post_id, member_id, content, created_at)" +
                " VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, (i + 1) % TOTAL_SIZE == 0 ? TOTAL_SIZE : (i + 1) % TOTAL_SIZE);
                ps.setLong(2, (long) (Math.random() * TOTAL_SIZE + 1));
                ps.setString(3, "content");
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return 2 * TOTAL_SIZE;
            }
        });
    }
}
