package com.example.slezica.dailyself.model;


import io.requery.Converter;
import io.requery.Nullable;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class ZonedDateTimeConverter implements Converter<ZonedDateTime, String> {

    @Override
    public Class<ZonedDateTime> getMappedType() {
        return ZonedDateTime.class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Override
    public String convertToPersisted(ZonedDateTime value) {
        return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Override
    public ZonedDateTime convertToMapped(Class<? extends ZonedDateTime> type, String value) {
        return ZonedDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return null;
    }
}
