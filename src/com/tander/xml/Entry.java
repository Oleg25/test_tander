package com.tander.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by velmOO on 12.04.2016.
 */
@XmlRootElement
public class Entry {

    private Integer field;

    public Entry() {}



    public Entry(Integer field) {
        this.field = field;
    }


    @XmlElement
    public Integer getField() {
        return field;
    }

    public void setField(Integer field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "field=" + field +
                '}';
    }
}
