/*
 * This file is generated by jOOQ.
 */
package com.datadome.botdetector.analysis.infra.jooq.codegen;


import com.datadome.botdetector.analysis.infra.jooq.codegen.tables.JooqLogLineHit;
import com.datadome.botdetector.analysis.infra.jooq.codegen.tables.records.LogLineHitRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>PUBLIC</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<LogLineHitRecord> LOG_LINE_HIT_PK = UniqueKeys0.LOG_LINE_HIT_PK;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<LogLineHitRecord> LOG_LINE_HIT_PK = Internal.createUniqueKey(JooqLogLineHit.T_LOG_LINE_HIT, "LOG_LINE_HIT_PK", new TableField[] { JooqLogLineHit.T_LOG_LINE_HIT.IP }, true);
    }
}
