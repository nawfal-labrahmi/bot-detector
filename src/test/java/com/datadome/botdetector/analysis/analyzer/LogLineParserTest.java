package com.datadome.botdetector.analysis.analyzer;

import com.datadome.botdetector.analysis.domain.LogLine;
import nl.basjes.parse.core.exceptions.DissectionFailure;
import nl.basjes.parse.core.exceptions.InvalidDissectorException;
import nl.basjes.parse.core.exceptions.MissingDissectorsException;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class LogLineParserTest {

    @Test
    void should_parse_log_line() {
        var logLineString  = "109.169.248.247 - - [12/Dec/2015:18:25:11 +0100] \"GET /administrator/ HTTP/1.1\" 200 4263 \"-\" \"Mozilla/5.0 (Windows NT 6.0; rv:34.0) Gecko/20100101 Firefox/34.0\" \"-\"";
        LogLine result = null;

        try {
            result = LogLineParser.parseLog(logLineString);
        } catch (InvalidDissectorException | MissingDissectorsException | DissectionFailure e) {
            e.printStackTrace();
        }

        assertThat(result).isNotNull();
        assertThat(result.getIp()).isEqualTo("109.169.248.247");
        assertThat(result.getUserAgent()).isEqualTo("Mozilla/5.0 (Windows NT 6.0; rv:34.0) Gecko/20100101 Firefox/34.0\" \"-");
        assertThat(result.getDate().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z"))).isEqualTo("12/Dec/2015:18:25:11 +0100");
        assertThat(result.getReferer()).isEqualTo(null);
    }

}