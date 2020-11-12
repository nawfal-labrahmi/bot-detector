package com.datadome.botdetector.analysis.analyzer;

import com.datadome.botdetector.analysis.domain.LogLine;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.core.Parser;
import nl.basjes.parse.core.exceptions.DissectionFailure;
import nl.basjes.parse.core.exceptions.InvalidDissectorException;
import nl.basjes.parse.core.exceptions.MissingDissectorsException;
import nl.basjes.parse.httpdlog.HttpdLoglineParser;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class LogLineParser {

    private static final String COMBINED_LOG_FORMAT = "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"";
    private static final String DATE_TIME_FORMAT_PATTERN = "dd/MMM/yyyy:HH:mm:ss Z";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);

    public static LogLine parseLog(String logLine) throws InvalidDissectorException, MissingDissectorsException, DissectionFailure {
        Parser<LogLineHolder> parser = new HttpdLoglineParser<>(LogLineHolder.class, COMBINED_LOG_FORMAT);
        LogLineHolder logLineHolder = parser.parse(logLine);
        return buildLogLine(logLineHolder);
    }

    private static LogLine buildLogLine(LogLineHolder holder) {
        return new LogLine(
                holder.getIp(),
                holder.getUserAgent(),
                parseTimestamp(holder.getTimestamp()),
                holder.getReferer()
        );
    }

    private static OffsetDateTime parseTimestamp(String timestamp) {
        return OffsetDateTime.parse(timestamp, DATE_TIME_FORMATTER);
    }

}
