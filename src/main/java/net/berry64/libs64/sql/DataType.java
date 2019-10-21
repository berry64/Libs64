/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

public enum DataType {
    TEXT,
    BOOLEAN,
    UUID,
    DATE,
    INT,
    OTHER,
    DOUBLE;

    public static String uidSuffix="[l64uid]";


    /**
     * {@link java.sql.Types}
     */
    public static DataType fromType(int type, String name){
        switch (type){
            case 4: return INT;
            case 8: return DOUBLE;
            case 91: return DATE;
            case 16: return BOOLEAN;
            case 12 | -16:
            {
                if(isUIDName(name)){
                    return UUID;
                }
                return TEXT;
            }
        }
        return OTHER;
    }

    public String getType(){
        switch (this){
            case INT: return "INT";
            case DATE: return "DATE";
            case TEXT: return "TEXT";
            case UUID: return "VARCHAR(36)";
            case DOUBLE: return "DOUBLE";
            case BOOLEAN: return "BOOLEAN";
            default: return "TEXT"; //defaults to text, will change
        }
    }

    public static boolean isUIDName(String name){
        return name.endsWith(uidSuffix);
    }
    public static String toUIDName(String name){
        return name+uidSuffix;
    }
}
