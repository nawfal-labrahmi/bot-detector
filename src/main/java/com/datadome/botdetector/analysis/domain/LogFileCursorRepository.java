package com.datadome.botdetector.analysis.domain;

public interface LogFileCursorRepository {

    void insert(LogFileCursor logFileCursor);

    /**
     * Lock the cursor table record while incrementing it.
     */
    LogFileCursor selectForUpdateCursor();

    void incrementCursor(long incrementedCursor);

    void clearCursor();

}
