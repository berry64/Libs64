package net.berry64.libs64.sql;

import java.util.Arrays;
import java.util.List;

public class Column implements Cloneable{
    private String name;
    private DataType type;
    private List<net.berry64.libs64.sql.Tags> tags;


    public enum Tags implements net.berry64.libs64.sql.Tags {
        NOT_NULL("NOT NULL"),
        PRIMARY_KEY("PRIMARY KEY"),
        AUTO_INCREMENT("AUTO_INCREMENT");

        public class CUSTOM implements net.berry64.libs64.sql.Tags {
            private String str;
            public CUSTOM(final String customTag){
                str = customTag;
            }

            public String getStr() {
                return str;
            }
        }

        private String str;
        Tags(String id){
            str=id;
        }

        public String getStr() {
            return str;
        }
    }

    public Column(String name, DataType type){
        this(name, type, (List<net.berry64.libs64.sql.Tags>) null);
    }
    public Column(String name, DataType type, net.berry64.libs64.sql.Tags... tags){
        this(name, type, Arrays.asList(tags));
    }
    public Column(String name, DataType type, List<net.berry64.libs64.sql.Tags> tags) {
        this.name = name;
        this.type = type;
        this.tags = tags;
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

    public String getTypeString(){
        if(tags == null)
            return type.getType();
        StringBuilder builder = new StringBuilder(type.getType());
        for(net.berry64.libs64.sql.Tags t : tags){
            builder.append(" ").append(t.getStr());
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Column){
            Column c = (Column) obj;
            return getName().equals(c.getName()) && getType().equals(c.getType());
        }
        return false;
    }
}
interface Tags{
    String getStr();
}
