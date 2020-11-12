package com.datadome.botdetector.analysis.domain;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class LogFileCursor {
    private long cursor;
    private OffsetDateTime date;
}
