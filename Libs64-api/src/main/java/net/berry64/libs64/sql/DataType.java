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

    public static String uidSuffix;

    /**
     * {@link java.sql.Types}
     */
    public static DataType fromType(int type, String name) {
        return null;
    }

    public String getType() {
        return null;
    }

    public static boolean isUIDName(String name) {
        return true;
    }

    public static String toUIDName(String name) {
        return null;
    }
}
