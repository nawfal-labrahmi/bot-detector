package com.datadome.botdetector.analysis.analyzer;

import com.datadome.botdetector.analysis.domain.LogLine;
import com.datadome.botdetector.analysis.domain.LogLineHit;
import com.datadome.botdetector.analysis.domain.LogLineHitRepository;
import com.datadome.botdetector.analysis.domain.LogLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.core.exceptions.DissectionFailure;
import nl.basjes.parse.core.exceptions.InvalidDissectorException;
import nl.basjes.parse.core.exceptions.MissingDissectorsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

import static java.util.stream.Collectors.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LogLineAnalyzer {

    private final BadBotDecider badBotDecider;
    private final LogLineRepository logLineRepository;
    private final LogLineHitRepository logLineHitRepository;

    public void analyze(LogLine logLine) {
        logLineRepository.insert(logLine);

        if (badBotDecider.isBadBot(logLine)) {
            logLineHitRepository.insert(new LogLineHit(logLine.getIp(), OffsetDateTime.now()));
            log.info("Recording bad bot hit: {}", logLine.getIp());
        }
    }

    public void analyze(List<String> rawLogLines) {
        List<LogLine> logLines = rawLogLines.stream()
                .map(this::safeParseLog)
                .filter(Objects::nonNull)
                .collect(toList());

        logLineRepository.bulkInsert(logLines);

        List<LogLineHit> hits = new ArrayList<>();
        logLines.forEach(logLine -> {
            if (badBotDecider.isBadBot(logLine)) {
                log.info("Recording bad bot hit: {}", logLine.getIp());
                hits.add(new LogLineHit(logLine.getIp(), OffsetDateTime.now()));
            }
        });

        List<LogLineHit> uniqueHits = hits.stream()
                .collect(
                        collectingAndThen(
                                toCollection(() -> new TreeSet<>(Comparator.comparing(LogLineHit::getIp))),
                                ArrayList::new)
                );
        logLineHitRepository.bulkInsert(uniqueHits);
    }

    public LogLine safeParseLog(String rawLogLine) {
        LogLine logLine;
        try {
            logLine = LogLineParser.parseLog(rawLogLine);
        } catch (InvalidDissectorException | MissingDissectorsException | DissectionFailure e) {
            log.error("Encountered exception while parsing log line: {}", rawLogLine);
            log.error("Encountered exception is: {}", e.getMessage());
            return null;
        }
        return logLine;
    }

}
