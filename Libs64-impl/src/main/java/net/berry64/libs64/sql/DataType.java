/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

import net.berry64.libs64.utils.reflection.AnnotationHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public enum DataType {
    TEXT,
    BOOLEAN,
    UUID,
    DATE,
    INT,
    OTHER,
    AUTO,
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
            case INT: return "INTEGER";
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

    static void setAnnotationDataTypeField(Field field, DBData original, DataType type){
        DBData newdata = new DBData(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return original.annotationType();
            }

            @Override
            public String name() {
                return original.name();
            }

            @Override
            public DataType type() {
                return type;
            }

            @Override
            public boolean PrimaryKey() {
                return original.PrimaryKey();
            }

            @Override
            public boolean NotNull() {
                return original.NotNull();
            }

            @Override
            public String[] Tags() {
                return original.Tags();
            }
        };

        try {
            AnnotationHelper.setFieldAnnotation(field, newdata);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    static DataType getAutoDataType(Field field){
        Class<?> clz = field.getType();
        if(String.class.isAssignableFrom(clz))
            return DataType.TEXT;
        if(Boolean.class.isAssignableFrom(clz))
            return DataType.BOOLEAN;
        if(java.util.UUID.class.isAssignableFrom(clz))
            return DataType.UUID;
        if(Date.class.isAssignableFrom(clz))
            return DataType.DATE;
        if(Integer.class.isAssignableFrom(clz))
            return DataType.INT;
        if(Double.class.isAssignableFrom(clz) || Float.class.isAssignableFrom(clz))
            return DataType.DOUBLE;
        return DataType.OTHER;
    }
}
