package com.datadome.botdetector.analysis.domain;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class LogLineHit {
    private String ip;
    private OffsetDateTime date;
}
