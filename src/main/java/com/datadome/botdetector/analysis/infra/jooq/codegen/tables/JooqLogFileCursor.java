/*
 * This file is generated by jOOQ.
 */
package com.datadome.botdetector.analysis.infra.jooq.codegen.tables;


import com.datadome.botdetector.analysis.infra.jooq.codegen.Public;
import com.datadome.botdetector.analysis.infra.jooq.codegen.tables.records.LogFileCursorRecord;

import java.time.OffsetDateTime;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JooqLogFileCursor extends TableImpl<LogFileCursorRecord> {

    private static final long serialVersionUID = 546302207;

    /**
     * The reference instance of <code>PUBLIC.LOG_FILE_CURSOR</code>
     */
    public static final JooqLogFileCursor T_LOG_FILE_CURSOR = new JooqLogFileCursor();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<LogFileCursorRecord> getRecordType() {
        return LogFileCursorRecord.class;
    }

    /**
     * The column <code>PUBLIC.LOG_FILE_CURSOR.CURSOR</code>.
     */
    public final TableField<LogFileCursorRecord, Long> CURSOR = createField(DSL.name("CURSOR"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>PUBLIC.LOG_FILE_CURSOR.DATE</code>.
     */
    public final TableField<LogFileCursorRecord, OffsetDateTime> DATE = createField(DSL.name("DATE"), org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE.precision(6).nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.LOG_FILE_CURSOR</code> table reference
     */
    public JooqLogFileCursor() {
        this(DSL.name("LOG_FILE_CURSOR"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.LOG_FILE_CURSOR</code> table reference
     */
    public JooqLogFileCursor(String alias) {
        this(DSL.name(alias), T_LOG_FILE_CURSOR);
    }

    /**
     * Create an aliased <code>PUBLIC.LOG_FILE_CURSOR</code> table reference
     */
    public JooqLogFileCursor(Name alias) {
        this(alias, T_LOG_FILE_CURSOR);
    }

    private JooqLogFileCursor(Name alias, Table<LogFileCursorRecord> aliased) {
        this(alias, aliased, null);
    }

    private JooqLogFileCursor(Name alias, Table<LogFileCursorRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> JooqLogFileCursor(Table<O> child, ForeignKey<O, LogFileCursorRecord> key) {
        super(child, key, T_LOG_FILE_CURSOR);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public JooqLogFileCursor as(String alias) {
        return new JooqLogFileCursor(DSL.name(alias), this);
    }

    @Override
    public JooqLogFileCursor as(Name alias) {
        return new JooqLogFileCursor(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public JooqLogFileCursor rename(String name) {
        return new JooqLogFileCursor(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JooqLogFileCursor rename(Name name) {
        return new JooqLogFileCursor(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, OffsetDateTime> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
