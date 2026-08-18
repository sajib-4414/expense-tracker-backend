package com.sajib_4414.expense.tracker.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseLogAppender extends AppenderBase<ILoggingEvent> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        System.out.println(">>> DatabaseLogAppender init() called");
        // Register this appender with Logback
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.setContext(loggerContext);
        this.setName("DATABASE");
        this.start();

        // Attach to root logger or specific logger
        loggerContext.getLogger("org.hibernate.SQL").addAppender(this);
        loggerContext.getLogger("org.springframework.jdbc").addAppender(this);
        loggerContext.getLogger("com.sajib_4414.expense.tracker.models").addAppender(this);
        // ADD THIS — root logger catches everything else
        loggerContext.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(this);
    }

    @Override
    protected void append(ILoggingEvent event) {
        System.out.println(">>> DatabaseLogAppender triggered: " + event.getFormattedMessage());

        if (jdbcTemplate == null) {
            System.out.println(">>> jdbcTemplate is NULL, skipping");
            return;
        }

        try {
            String sql = """
                INSERT INTO logging_event 
                    (timestmp, formatted_message, logger_name, level_string, thread_name, caller_filename, caller_class, caller_method, caller_line)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

            StackTraceElement caller = event.getCallerData().length > 0
                    ? event.getCallerData()[0]
                    : null;

            long eventId = jdbcTemplate.queryForObject(
                    sql + " RETURNING event_id",
                    Long.class,
                    event.getTimeStamp(),
                    event.getFormattedMessage(),
                    event.getLoggerName(),
                    event.getLevel().toString(),
                    event.getThreadName(),
                    caller != null ? caller.getFileName() : "unknown",
                    caller != null ? caller.getClassName() : "unknown",
                    caller != null ? caller.getMethodName() : "unknown",
                    caller != null ? String.valueOf(caller.getLineNumber()) : "0"
            );

            // Insert MDC properties
            Map<String, String> mdcMap = event.getMDCPropertyMap();
            if (mdcMap != null && !mdcMap.isEmpty()) {
                mdcMap.forEach((key, value) -> {
                    jdbcTemplate.update(
                            "INSERT INTO logging_event_property (event_id, mapped_key, mapped_value) VALUES (?, ?, ?)",
                            eventId, key, value
                    );
                });
            }

            // Insert exception if present
            IThrowableProxy throwable = event.getThrowableProxy();
            if (throwable != null) {
                StackTraceElementProxy[] lines = throwable.getStackTraceElementProxyArray();
                for (int i = 0; i < lines.length; i++) {
                    jdbcTemplate.update(
                            "INSERT INTO logging_event_exception (event_id, i, trace_line) VALUES (?, ?, ?)",
                            eventId, i, lines[i].toString()
                    );
                }
            }

        } catch (Exception e) {
            addError("Failed to insert log into DB", e);
        }
    }
}
