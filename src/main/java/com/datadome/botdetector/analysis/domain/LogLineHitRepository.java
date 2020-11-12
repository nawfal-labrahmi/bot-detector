package com.datadome.botdetector.analysis.domain;

import java.util.List;
import java.util.Optional;

public interface LogLineHitRepository {

    Optional<LogLineHit> findByIp(String ip);

    LogLineHit insert(LogLineHit logFileHit);

    void bulkInsert(List<LogLineHit> logLineHits);

}
