package com.datadome.botdetector.analysis.infra;

import com.datadome.botdetector.analysis.domain.LogFileCursor;
import com.datadome.botdetector.analysis.domain.LogFileCursorRepository;
import com.datadome.botdetector.analysis.infra.jooq.codegen.Tables;
import com.datadome.botdetector.analysis.infra.jooq.codegen.tables.records.LogFileCursorRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Repository
public class JooqLogFileCursorRepository implements LogFileCursorRepository {

    private final DSLContext dslContext;

    @Override
    public void insert(LogFileCursor logFileCursor) {
        dslContext
                .insertInto(Tables.T_LOG_FILE_CURSOR)
                .set(new LogFileCursorRecord(
                        logFileCursor.getCursor(),
                        logFileCursor.getDate()
                ))
                .execute();
    }

    @Override
    public LogFileCursor selectForUpdateCursor() {
        return toLogFileCursor(
                dslContext.selectFrom(Tables.T_LOG_FILE_CURSOR)
                        .forUpdate()
                        .fetchOne());
    }

    @Override
    public void incrementCursor(long incrementedCursor) {
        dslContext
                .update(Tables.T_LOG_FILE_CURSOR)
                .set(Tables.T_LOG_FILE_CURSOR.CURSOR, incrementedCursor)
                .set(Tables.T_LOG_FILE_CURSOR.DATE, OffsetDateTime.now())
                .execute();
    }

    @Override
    public void clearCursor() {
        dslContext
                .truncate(Tables.T_LOG_FILE_CURSOR)
                .execute();
    }

    private LogFileCursor toLogFileCursor(LogFileCursorRecord record) {
        return new LogFileCursor(
                record.getCursor(),
                record.getDate()
        );
    }
}
