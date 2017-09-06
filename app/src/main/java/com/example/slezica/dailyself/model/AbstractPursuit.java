package com.example.slezica.dailyself.model;


import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

@Entity
public abstract class AbstractPursuit {

    @Key
    @Generated
    public int id;

    public String name;

}
