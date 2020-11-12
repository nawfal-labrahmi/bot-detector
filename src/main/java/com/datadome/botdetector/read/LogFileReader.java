package com.datadome.botdetector.read;

import com.datadome.botdetector.analysis.domain.LogFileCursorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogFileReader {

    // fixedRate 1ms
    // 1h -> 72000 lines analyzed
    // 27.7/s avg lines analyzed (calculate average)
    // Broker pub/sub rate 1000 -> 15 /s (strong decrease)
    // 50000 lines analyzed in 30 min

    // fixedRate 20ms
    // 27.2/s avg lines analyzed (calculate average)
    // Broker pub/sub rate 50 -> 15 /s (slow decrease)
    // 49000 lines analyzed in 30 min

    // fixedRate 10ms
    // 28.3/s avg lines analyzed (calculate average)
    // Broker pub/sub rate 100 -> 15 /s (average decrease)
    // 51000 lines analyzed in 30 min

    // fixedRate 10ms
    // chunk size 10
    // 90/s avg lines analyzed (calculate average)
    // Broker pub/sub rate 100 -> 5 /s (strong decrease)
    // 161000 lines analyzed in 30 min

    // fixedRate 10ms (run in docker)
    // chunk size 100 + batch insert of SQL data
    // 485/s avg lines analyzed (calculate average)
    // Broker pub/sub rate 100 -> 3/s (strong then slow decrease)
    // 873500 lines analyzed in 30 min


    private static final String LOF_FILE_PATH = "access.log";
    private static final int CHUNK_SIZE = 100;

    private final LogFileCursorRepository logFileCursorRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Executes task in parallel every 200ms after an initial delay of 3min
     */
    @Transactional
    @Scheduled(fixedRate = 10, initialDelayString = "PT3M")
    @Async
    public void readLogFile() throws IOException {
        long cursor = logFileCursorRepository.selectForUpdateCursor().getCursor();
        log.info("Reading log file at position: {}", cursor);

        try (Stream<String> lines = Files.lines(Paths.get(LOF_FILE_PATH))) {
            List<String> extractedLines = lines
                    .skip(cursor)
                    .limit(CHUNK_SIZE)
                    .collect(toList());

            if (extractedLines.isEmpty()) {
                log.info("No new line after position {} in file {} to be read.", cursor, LOF_FILE_PATH);
                return;
            }

            logFileCursorRepository.incrementCursor(cursor + extractedLines.size());
            sendLogAnalysisRequest(extractedLines);
        }
    }

    private void sendLogAnalysisRequest(List<String> logLine) {
        log.info("Firing the LogAnalysisRequestedEvent");

        rabbitTemplate.convertAndSend(
                "log.analysis.requested.queue",
                new LogAnalysisRequestedEvent(logLine)
        );
    }

}
