package com.datadome.botdetector.read;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public final class LogAnalysisRequestedEvent implements Serializable {
    private List<String> logLines;
}
