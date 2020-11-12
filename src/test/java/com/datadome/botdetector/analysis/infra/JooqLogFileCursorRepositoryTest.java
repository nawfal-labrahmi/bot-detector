package com.datadome.botdetector.analysis.infra;

import com.datadome.botdetector.analysis.domain.LogFileCursor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JooqTest
@ContextConfiguration(classes = {
        DataSource.class,
        JooqLogFileCursorRepository.class
})
class JooqLogFileCursorRepositoryTest {

    @Autowired
    private JooqLogFileCursorRepository cursorRepository;

    @BeforeEach
    void setUp() {
        cursorRepository.clearCursor();
    }

    @AfterEach
    void cleanUp() {
        cursorRepository.clearCursor();
    }

    @Test
    void select_for_update_log_file_cursor() {
        LogFileCursor fileCursor = new LogFileCursor(13L, OffsetDateTime.now());
        cursorRepository.insert(fileCursor);

        LogFileCursor persistedFileCursor = cursorRepository.selectForUpdateCursor();

        assertThat(persistedFileCursor.getCursor()).isEqualTo(fileCursor.getCursor());
        assertThat(persistedFileCursor.getDate()).isEqualTo(fileCursor.getDate());
    }

    @Test
    void increment_log_file_cursor() {
        LogFileCursor fileCursor = new LogFileCursor(12L, OffsetDateTime.now());
        cursorRepository.insert(fileCursor);
        long newCursor = 13L;

        cursorRepository.incrementCursor(newCursor);

        LogFileCursor persistedFileCursor = cursorRepository.selectForUpdateCursor();
        assertThat(persistedFileCursor.getCursor()).isEqualTo(newCursor);
        assertThat(persistedFileCursor.getDate()).isAfter(fileCursor.getDate());
    }

}