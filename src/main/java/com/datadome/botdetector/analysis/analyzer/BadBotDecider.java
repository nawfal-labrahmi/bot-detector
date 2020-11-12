package com.datadome.botdetector.analysis.analyzer;

import com.datadome.botdetector.analysis.domain.LogLine;
import com.datadome.botdetector.analysis.domain.LogLineHitRepository;
import com.datadome.botdetector.analysis.domain.LogLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class BadBotDecider {

    private static final int TIME_THRESHOLD_IN_DAYS = 1;
    private static final int NUMBER_OF_HITS_THRESHOLD = 20;

    private final LogLineRepository logLineRepository;
    private final LogLineHitRepository logLineHitRepository;

    /**
     * Order of evaluated rules matters here.
     * Evaluate less costly rules first.
     */
    public boolean isBadBot(LogLine logLine) {
        return userAgentNull(logLine.getUserAgent())
                || blackListedIp(logLine.getIp())
                || tooManyHits(logLine.getIp());
    }

    /**
     * If there's no user agent, it is assumed a bad bot.
     */
    private boolean userAgentNull(String userAgent) {
        return StringUtils.isBlank(userAgent);
    }

    /**
     * If the log IP is already blacklisted, it is assumed a bad bot.
     */
    private boolean blackListedIp(String ip) {
        return logLineHitRepository.findByIp(ip).isPresent();
    }

    /**
     * If number of hits in the last day are over threshold, it is assumed a bad bot.
     */
    private boolean tooManyHits(String ip) {
        return logLineRepository.findByIp(ip)
                .filter(log -> log.getDate().isBefore(OffsetDateTime.now().minusDays(TIME_THRESHOLD_IN_DAYS)))
                .count() > NUMBER_OF_HITS_THRESHOLD;
    }
}
