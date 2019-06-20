package net.berry64.libs64.sql;

public class Tags implements Tag{
    public static Tags NOT_NULL = new Tags("NOT NULL"),
    PRIMARY_KEY = new Tags("PRIMARY KEY"),
    AUTO_INCREMENT = new Tags("AUTO_INCREMENT");

    public static class CUSTOM implements Tag {
        private String str;
        public CUSTOM(final String customTag){
            str = customTag;
        }

        public String getStr() {
            return str;
        }
    }

    private String str;
    private Tags(String id){
        str=id;
    }

    public String getStr() {
        return str;
    }
}
interface Tag{
    String getStr();
}
