package com.example.slezica.dailyself.model;


import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import org.threeten.bp.ZonedDateTime;

@Entity
public abstract class AbstractPursuitEntry {

    @Key
    @Generated
    public int id;

    @ManyToOne
    Pursuit pursuit;

    @Convert(ZonedDateTimeConverter.class)
    public ZonedDateTime datetime;

    public int score;
    public String comment;
}
