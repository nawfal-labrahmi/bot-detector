package com.datadome.botdetector.analysis.analyzer;

import com.datadome.botdetector.read.LogAnalysisRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogAnalysisHandler {

    private final LogLineAnalyzer logLineAnalyzer;

    @RabbitListener(queues = "log.analysis.requested.queue")
    public void onLogAnalysisRequestedEvent(LogAnalysisRequestedEvent event) {
        log.info("Handling LogAnalysisRequestedEvent");

        logLineAnalyzer.analyze(event.getLogLines());
    }

}
