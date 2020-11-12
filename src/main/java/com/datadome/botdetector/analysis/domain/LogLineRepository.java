package com.datadome.botdetector.analysis.domain;

import java.util.List;
import java.util.stream.Stream;

public interface LogLineRepository {

    Stream<LogLine> findByIp(String ip);

    LogLine insert(LogLine logLine);

    void bulkInsert(List<LogLine> logLines);

}
