package com.datadome.botdetector.analysis.analyzer;


import com.datadome.botdetector.analysis.domain.LogLine;
import com.datadome.botdetector.analysis.domain.LogLineHit;
import com.datadome.botdetector.analysis.domain.LogLineHitRepository;
import com.datadome.botdetector.analysis.domain.LogLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LogLineAnalyzerTest {

    private BadBotDecider badBotDecider;
    private LogLineRepository logLineRepository;
    private LogLineHitRepository logLineHitRepository;
    private LogLineAnalyzer logLineAnalyzer;

    @Captor
    private ArgumentCaptor<LogLineHit> logLineHitCaptor;

    @BeforeEach
    void setUp() {
        badBotDecider = mock(BadBotDecider.class);
        logLineRepository = mock(LogLineRepository.class);
        logLineHitRepository = mock(LogLineHitRepository.class);
        logLineAnalyzer = new LogLineAnalyzer(
                badBotDecider,
                logLineRepository,
                logLineHitRepository
        );
        logLineHitCaptor = ArgumentCaptor.forClass(LogLineHit.class);
    }

    @Test
    void insert_analyzed_log_line() {
        LogLine logLine = new LogLine(
                "109.169.248.247",
                "Mozilla/5.0 (Windows NT 6.0; rv:34.0) Gecko/20100101 Firefox/34.0\" \"-",
                OffsetDateTime.now(),
                null
        );

        logLineAnalyzer.analyze(logLine);

        verify(logLineRepository).insert(logLine);
    }

    @Test
    void record_hit_when_bad_bot_detected() {
        LogLine logLine = new LogLine(
                "109.169.248.247",
                null,
                OffsetDateTime.now(),
                null
        );
        when(badBotDecider.isBadBot(logLine)).thenReturn(true);

        logLineAnalyzer.analyze(logLine);

        verify(logLineHitRepository).insert(logLineHitCaptor.capture());
        assertThat(logLineHitCaptor.getValue().getIp()).isEqualTo(logLine.getIp());
    }

    @Test
    void do_not_record_hit_when_bad_bot_not_detected() {
        LogLine logLine = new LogLine(
                "109.169.248.247",
                "Mozilla/5.0 (Windows NT 6.0; rv:34.0) Gecko/20100101 Firefox/34.0\" \"-",
                OffsetDateTime.now(),
                null
        );
        when(badBotDecider.isBadBot(logLine)).thenReturn(false);

        logLineAnalyzer.analyze(logLine);

        verifyNoInteractions(logLineHitRepository);
    }

}