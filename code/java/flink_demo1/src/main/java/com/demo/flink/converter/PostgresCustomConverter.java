package com.demo.flink.converter;

import io.debezium.common.annotation.Incubating;
import io.debezium.spi.converter.CustomConverter;
import io.debezium.spi.converter.RelationalColumn;
import org.apache.kafka.connect.data.SchemaBuilder;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

@Incubating
public class PostgresCustomConverter implements CustomConverter<SchemaBuilder, RelationalColumn> {
    private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private String dateFormat = "yyyy-MM-dd";
    private String timeFormat = "HH:mm:ss";

    @Override
    public void configure(Properties props) {
        String dateTimeFormat = props.getProperty("datetime.format");
        if (dateTimeFormat != null && !dateTimeFormat.isEmpty()) {
            this.dateTimeFormat = dateTimeFormat;
        }
        String dateFormat = props.getProperty("date.format");
        if (dateFormat != null && !dateFormat.isEmpty()) {
            this.dateFormat = dateFormat;
        }
        String timeFormat = props.getProperty("date.format");
        if (timeFormat != null && !timeFormat.isEmpty()) {
            this.timeFormat = timeFormat;
        }
    }

    @Override
    public void converterFor(RelationalColumn field, ConverterRegistration<SchemaBuilder> registration) {
        if ("timestamp".equals(field.typeName())) {
            registration.register(SchemaBuilder.string(), value -> {
                SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
                if (value instanceof Timestamp) {
                    return sdf.format(new Date(((Timestamp) value).getTime()));
                } else if (value instanceof Instant) {
                    return sdf.format(((Instant) value).toEpochMilli());
                } else if (value instanceof Long) {
                    return sdf.format(new Date((Long) value));
                }
                return value;
            });
        } else if ("date".equals(field.typeName())) {
            registration.register(SchemaBuilder.string(), value -> {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                if (value instanceof Date) {
                    return sdf.format(((Date) value).getTime());
                } else if (value instanceof Long) {
                    return sdf.format(new Date((Long) value));
                }
                return value;
            });
        } else if ("time".equals(field.typeName())) {
            registration.register(SchemaBuilder.string(), value -> {
                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                if (value instanceof Time) {
                    return sdf.format(new Date(((Time) value).getTime()));
                } else if (value instanceof Long) {
                    return sdf.format(new Date((Long) value));
                }
                return value;
            });
        } else if ("bytea".equals(field.typeName())) {
            registration.register(SchemaBuilder.string(), value -> {
                if (value instanceof byte[]) {
                    return new String((byte[]) value);
                }
                return value;
            });
        }
    }
}