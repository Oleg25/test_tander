package com.tander.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by velmOO on 12.04.2016.
 */
@XmlRootElement
public class Entries {

    private List<Entry> entire;

    @XmlElement
    public List<Entry> getEntire() {
        return entire;
    }

    public void setEntires(List<Entry> entire) {
        this.entire = entire;
    }
}
