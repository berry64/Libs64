/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AnnotationHelper {
    private static final String fieldFieldName = "declaredAnnotations",
            fieldMethodName = "declaredAnnotations";
    private static Field fieldField = null;
    private static Method fieldMethod = null;

    static {
        try {
            fieldField = Field.class.getDeclaredField(fieldFieldName);
            if (!fieldField.isAccessible())
                fieldField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            fieldMethod = Field.class.getDeclaredMethod(fieldMethodName);
            if (!fieldMethod.isAccessible())
                fieldMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Annotation> T setFieldAnnotation(Field field, T annotation) throws IllegalAccessException, InvocationTargetException {
        Map<Class<? extends Annotation>, Annotation> annotations;
        if ((annotations = (Map<Class<? extends Annotation>, Annotation>) (fieldField.get(field))) == null)
            annotations = (Map<Class<? extends Annotation>, Annotation>) fieldMethod.invoke(field);
        if (annotations != null)
            return (T) annotations.put(annotation.getClass(), annotation);
        return null;
    }
}
