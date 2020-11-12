package com.datadome.botdetector.analysis.infra;

import com.datadome.botdetector.analysis.domain.LogLine;
import com.datadome.botdetector.analysis.domain.LogLineRepository;
import com.datadome.botdetector.analysis.infra.jooq.codegen.Tables;
import com.datadome.botdetector.analysis.infra.jooq.codegen.tables.records.LogLineRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Repository
public class JooqLogLineRepository implements LogLineRepository {

    private final DSLContext dslContext;

    @Override
    public Stream<LogLine> findByIp(String ip) {
        return dslContext.selectFrom(Tables.T_LOG_LINE)
                .where(Tables.T_LOG_LINE.IP.eq(ip))
                .fetchStreamInto(Tables.T_LOG_LINE)
                .map(this::mapFromRecord);
    }

    @Override
    public LogLine insert(LogLine logLine) {
        dslContext.insertInto(Tables.T_LOG_LINE)
                .set(Tables.T_LOG_LINE.IP, logLine.getIp())
                .set(Tables.T_LOG_LINE.USER_AGENT, logLine.getUserAgent())
                .set(Tables.T_LOG_LINE.DATE, logLine.getDate())
                .set(Tables.T_LOG_LINE.REFERER, logLine.getReferer())
                .execute();
        return logLine;
    }

    @Override
    public void bulkInsert(List<LogLine> logLines) {
        dslContext
                .batchInsert(
                        logLines.stream()
                                .map(this::mapToRecord)
                                .collect(toList())
                )
                .execute();
    }

    private LogLine mapFromRecord(LogLineRecord record) {
        return new LogLine(
                record.getIp(),
                record.getUserAgent(),
                record.getDate(),
                record.getReferer()
        );
    }

    private LogLineRecord mapToRecord(LogLine logLine) {
        LogLineRecord record = new LogLineRecord();
        record.setIp(logLine.getIp());
        record.setUserAgent(logLine.getUserAgent());
        record.setDate(logLine.getDate());
        record.setReferer(logLine.getReferer());
        return record;
    }
}
