package net.berry64.libs64.sql;

import java.util.Map;

public class Row {
    Map<Column, Object> data;

    public Row(){}

    public Row appendData(Column column, Object value){
        data.put(column, value);
        return this;
    }
}
