package com.datadome.botdetector.analysis.analyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import nl.basjes.parse.core.Field;

/**
 * ALL possible values in the combined log format:
 *
 * - STRING:connection.client.user
 * - IP:connection.client.host.last
 * - TIME.STAMP:request.receive.time.last
 * - TIME.DAY:request.receive.time.last.day
 * - TIME.MONTHNAME:request.receive.time.last.monthname
 * - TIME.MONTH:request.receive.time.last.month
 * - TIME.WEEK:request.receive.time.last.weekofweekyear
 * - TIME.YEAR:request.receive.time.last.weekyear
 * - TIME.YEAR:request.receive.time.last.year
 * - TIME.HOUR:request.receive.time.last.hour
 * - TIME.MINUTE:request.receive.time.last.minute
 * - TIME.SECOND:request.receive.time.last.second
 * - TIME.MILLISECOND:request.receive.time.last.millisecond
 * - TIME.MICROSECOND:request.receive.time.last.microsecond
 * - TIME.NANOSECOND:request.receive.time.last.nanosecond
 * - TIME.DATE:request.receive.time.last.date
 * - TIME.TIME:request.receive.time.last.time
 * - TIME.ZONE:request.receive.time.last.timezone
 * - TIME.EPOCH:request.receive.time.last.epoch
 * - TIME.DAY:request.receive.time.last.day_utc
 * - TIME.MONTHNAME:request.receive.time.last.monthname_utc
 * - TIME.MONTH:request.receive.time.last.month_utc
 * - TIME.WEEK:request.receive.time.last.weekofweekyear_utc
 * - TIME.YEAR:request.receive.time.last.weekyear_utc
 * - TIME.YEAR:request.receive.time.last.year_utc
 * - TIME.HOUR:request.receive.time.last.hour_utc
 * - TIME.MINUTE:request.receive.time.last.minute_utc
 * - TIME.SECOND:request.receive.time.last.second_utc
 * - TIME.MILLISECOND:request.receive.time.last.millisecond_utc
 * - TIME.MICROSECOND:request.receive.time.last.microsecond_utc
 * - TIME.NANOSECOND:request.receive.time.last.nanosecond_utc
 * - TIME.DATE:request.receive.time.last.date_utc
 * - TIME.TIME:request.receive.time.last.time_utc
 * - HTTP.URI:request.referer
 * - HTTP.PROTOCOL:request.referer.protocol
 * - HTTP.USERINFO:request.referer.userinfo
 * - HTTP.HOST:request.referer.host
 * - HTTP.PORT:request.referer.port
 * - HTTP.PATH:request.referer.path
 * - HTTP.QUERYSTRING:request.referer.query
 * - STRING:request.referer.query.*
 * - HTTP.REF:request.referer.ref
 * - TIME.STAMP:request.receive.time
 * - TIME.DAY:request.receive.time.day
 * - TIME.MONTHNAME:request.receive.time.monthname
 * - TIME.MONTH:request.receive.time.month
 * - TIME.WEEK:request.receive.time.weekofweekyear
 * - TIME.YEAR:request.receive.time.weekyear
 * - TIME.YEAR:request.receive.time.year
 * - TIME.HOUR:request.receive.time.hour
 * - TIME.MINUTE:request.receive.time.minute
 * - TIME.SECOND:request.receive.time.second
 * - TIME.MILLISECOND:request.receive.time.millisecond
 * - TIME.MICROSECOND:request.receive.time.microsecond
 * - TIME.NANOSECOND:request.receive.time.nanosecond
 * - TIME.DATE:request.receive.time.date
 * - TIME.TIME:request.receive.time.time
 * - TIME.ZONE:request.receive.time.timezone
 * - TIME.EPOCH:request.receive.time.epoch
 * - TIME.DAY:request.receive.time.day_utc
 * - TIME.MONTHNAME:request.receive.time.monthname_utc
 * - TIME.MONTH:request.receive.time.month_utc
 * - TIME.WEEK:request.receive.time.weekofweekyear_utc
 * - TIME.YEAR:request.receive.time.weekyear_utc
 * - TIME.YEAR:request.receive.time.year_utc
 * - TIME.HOUR:request.receive.time.hour_utc
 * - TIME.MINUTE:request.receive.time.minute_utc
 * - TIME.SECOND:request.receive.time.second_utc
 * - TIME.MILLISECOND:request.receive.time.millisecond_utc
 * - TIME.MICROSECOND:request.receive.time.microsecond_utc
 * - TIME.NANOSECOND:request.receive.time.nanosecond_utc
 * - TIME.DATE:request.receive.time.date_utc
 * - TIME.TIME:request.receive.time.time_utc
 * - HTTP.URI:request.referer.last
 * - HTTP.PROTOCOL:request.referer.last.protocol
 * - HTTP.USERINFO:request.referer.last.userinfo
 * - HTTP.HOST:request.referer.last.host
 * - HTTP.PORT:request.referer.last.port
 * - HTTP.PATH:request.referer.last.path
 * - HTTP.QUERYSTRING:request.referer.last.query
 * - STRING:request.referer.last.query.*
 * - HTTP.REF:request.referer.last.ref
 * - NUMBER:connection.client.logname
 * - BYTESCLF:response.body.bytes
 * - BYTES:response.body.bytes
 * - HTTP.USERAGENT:request.user-agent.last
 * - STRING:request.status.last
 * - HTTP.USERAGENT:request.user-agent
 * - STRING:connection.client.user.last
 * - HTTP.FIRSTLINE:request.firstline.original
 * - HTTP.METHOD:request.firstline.original.method
 * - HTTP.URI:request.firstline.original.uri
 * - HTTP.PROTOCOL:request.firstline.original.uri.protocol
 * - HTTP.USERINFO:request.firstline.original.uri.userinfo
 * - HTTP.HOST:request.firstline.original.uri.host
 * - HTTP.PORT:request.firstline.original.uri.port
 * - HTTP.PATH:request.firstline.original.uri.path
 * - HTTP.QUERYSTRING:request.firstline.original.uri.query
 * - STRING:request.firstline.original.uri.query.*
 * - HTTP.REF:request.firstline.original.uri.ref
 * - HTTP.PROTOCOL_VERSION:request.firstline.original.protocol
 * - HTTP.PROTOCOL:request.firstline.original.protocol
 * - HTTP.PROTOCOL.VERSION:request.firstline.original.protocol.version
 * - BYTESCLF:response.body.bytes.last
 * - BYTES:response.body.bytes.last
 * - NUMBER:connection.client.logname.last
 * - HTTP.FIRSTLINE:request.firstline
 * - HTTP.METHOD:request.firstline.method
 * - HTTP.URI:request.firstline.uri
 * - HTTP.PROTOCOL:request.firstline.uri.protocol
 * - HTTP.USERINFO:request.firstline.uri.userinfo
 * - HTTP.HOST:request.firstline.uri.host
 * - HTTP.PORT:request.firstline.uri.port
 * - HTTP.PATH:request.firstline.uri.path
 * - HTTP.QUERYSTRING:request.firstline.uri.query
 * - STRING:request.firstline.uri.query.*
 * - HTTP.REF:request.firstline.uri.ref
 * - HTTP.PROTOCOL_VERSION:request.firstline.protocol
 * - HTTP.PROTOCOL:request.firstline.protocol
 * - HTTP.PROTOCOL.VERSION:request.firstline.protocol.version
 * - BYTES:response.body.bytesclf
 * - BYTESCLF:response.body.bytesclf
 * - IP:connection.client.host
 */
public class LogLineHolder {

    private final Map<String, String> results = new HashMap<>(32);

    @Field("IP:connection.client.host")
    public void setIP(final String value) {
        results.put("ip", value);
    }

    @Field("HTTP.USERAGENT:request.user-agent")
    public void setUserAgent(final String value) {
        results.put("user-agent", value);
    }

    @Field("HTTP.URI:request.referer")
    public void setReferer(final String value) {
        results.put("referer", value);
    }

    @Field("TIME.STAMP:request.receive.time.last")
    public void setTimestamp(final String value) {
        results.put("timestamp", value);
    }

    public Map<String, String> getResults() {
        return results;
    }

    public String getIp() {
        return results.get("ip");
    }

    public String getUserAgent() {
        return results.get("user-agent");
    }

    public String getTimestamp() {
        return results.get("timestamp");
    }

    public String getReferer() {
        return results.get("referer");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        TreeSet<String> keys = new TreeSet<>(results.keySet());
        for (String key : keys) {
            sb.append(key).append(" = ").append(results.get(key)).append('\n');
        }
        return sb.toString();
    }

    public void clear() {
        results.clear();
    }

}
