package com.datadome.botdetector.analysis.infra;

import com.datadome.botdetector.analysis.domain.LogLineHit;
import com.datadome.botdetector.analysis.domain.LogLineHitRepository;
import com.datadome.botdetector.analysis.infra.jooq.codegen.Tables;
import com.datadome.botdetector.analysis.infra.jooq.codegen.tables.records.LogLineHitRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JooqLogLineHitRepository implements LogLineHitRepository {

    private final DSLContext dslContext;

    @Override
    public Optional<LogLineHit> findByIp(String ip) {
        return Optional.ofNullable(
                dslContext.selectFrom(Tables.T_LOG_LINE_HIT)
                        .where(Tables.T_LOG_LINE_HIT.IP.eq(ip))
                        .fetchOneInto(Tables.T_LOG_LINE_HIT))
                .map(this::mapRecord);
    }

    @Override
    public LogLineHit insert(LogLineHit logFileHit) {
        dslContext.insertInto(Tables.T_LOG_LINE_HIT)
                .set(Tables.T_LOG_LINE_HIT.IP, logFileHit.getIp())
                .set(Tables.T_LOG_LINE_HIT.DATE, logFileHit.getDate())
                .onConflict(Tables.T_LOG_LINE_HIT.IP).doNothing()
                .execute();
        return logFileHit;
    }

    @Override
    public void bulkInsert(List<LogLineHit> logLineHits) {
        Query[] queries = logLineHits.stream()
                .map(this::mapToRecord)
                .map(record ->
                        dslContext.insertInto(Tables.T_LOG_LINE_HIT)
                                .set(record)
                                .onConflict(Tables.T_LOG_LINE_HIT.IP).doNothing())
                .toArray(Query[]::new);

        dslContext.batch(queries).execute();
    }

    private LogLineHit mapRecord(LogLineHitRecord record) {
        return new LogLineHit(
                record.getIp(),
                record.getDate()
        );
    }

    private LogLineHitRecord mapToRecord(LogLineHit logLineHit) {
        return new LogLineHitRecord(logLineHit.getIp(), logLineHit.getDate());
    }
}
