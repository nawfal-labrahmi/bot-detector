package com.datadome.botdetector.analysis.analyzer;

import com.datadome.botdetector.analysis.domain.LogLine;
import com.datadome.botdetector.analysis.domain.LogLineHit;
import com.datadome.botdetector.analysis.domain.LogLineHitRepository;
import com.datadome.botdetector.analysis.domain.LogLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BadBotDeciderTest {

    private LogLineRepository logLineRepository;
    private LogLineHitRepository logLineHitRepository;
    private BadBotDecider badBotDecider;

    @BeforeEach
    void setUp() {
        logLineRepository = mock(LogLineRepository.class);
        logLineHitRepository = mock(LogLineHitRepository.class);
        badBotDecider = new BadBotDecider(logLineRepository, logLineHitRepository);
    }

    @Test
    void bad_bot_when_user_agent_null() {
        LogLine logLine = new LogLine(
                "109.169.248.247",
                null,
                OffsetDateTime.now(),
                null
        );

        assertThat(badBotDecider.isBadBot(logLine)).isTrue();
    }

    @Test
    void bad_bot_when_blacklisted_ip() {
        LogLine logLine = new LogLine(
                "109.169.248.247",
                "Mozilla/5.0 (Windows NT 6.0; rv:34.0) Gecko/20100101 Firefox/34.0\" \"-",
                OffsetDateTime.now(),
                null
        );
        when(logLineHitRepository.findByIp(logLine.getIp()))
                .thenReturn(Optional.of(new LogLineHit(logLine.getIp(), logLine.getDate())));

        assertThat(badBotDecider.isBadBot(logLine)).isTrue();
    }

    @Test
    void bad_bot_when_too_many_hits() {
        LogLine logLine = new LogLine(
                "109.169.248.247",
                "Mozilla/5.0 (Windows NT 6.0; rv:34.0) Gecko/20100101 Firefox/34.0\" \"-",
                OffsetDateTime.now().minusDays(1L),
                null
        );
        when(logLineHitRepository.findByIp(logLine.getIp()))
                .thenReturn(Optional.empty());
        when(logLineRepository.findByIp(logLine.getIp()))
                .thenReturn(Stream.of(
                        logLine, logLine, logLine, logLine, logLine, logLine, logLine, logLine, logLine, logLine,
                        logLine, logLine, logLine, logLine, logLine, logLine, logLine, logLine, logLine, logLine,
                        logLine));

        assertThat(badBotDecider.isBadBot(logLine)).isTrue();
    }

}