package net.berry64.libs64.sql;

import java.util.Arrays;
import java.util.List;

public class Column implements Cloneable, Comparable<Column> {
    private Table table;
    private String name;
    private DataType type;
    private List<Tag> tags;

    public Column(String name, DataType type) {
        this(null, name, type);
    }

    public Column(String name, DataType type, Tag... tags) {
        this(null, name, type, Arrays.asList(tags));
    }

    public Column(String name, DataType type, List<Tag> tags) {
        this(null, name, type, tags);
    }

    public Column(Table table, String name, DataType type) {
        this(table, name, type, (List<Tag>) null);
    }

    public Column(Table table, String name, DataType type, Tag... tags) {
        this(table, name, type, Arrays.asList(tags));
    }

    public Column(Table table, String name, DataType type, List<Tag> tags) {
        this.table = table;
        this.name = name;
        this.type = type;
        this.tags = tags;
    }

    public Column setTable(Table t) {
        table = t;
        return this;
    }

    public Table getTable() {
        return table;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Column setNullTable(Table t) {
        if (table == null)
            table = t;
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public String getTypeString() {
        if (tags == null)
            return type.getType();
        StringBuilder builder = new StringBuilder(type.getType());
        for (Tag t : tags) {
            builder.append(" ").append(t.getStr());
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Column) {
            Column c = (Column) obj;
            return getName().equals(c.getName()) && getType().equals(c.getType());
        }
        return false;
    }

    @Override
    public int compareTo(Column o) {
        return getName().compareTo(o.getName());
    }
}