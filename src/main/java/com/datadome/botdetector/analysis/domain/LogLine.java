package com.datadome.botdetector.analysis.domain;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class LogLine {
    private String ip;
    private String userAgent;
    private OffsetDateTime date;
    private String referer;
}
