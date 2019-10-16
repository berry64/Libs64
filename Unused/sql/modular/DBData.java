package net.berry64.libs64.sql.modular;

import net.berry64.libs64.sql.DataType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DBData {
    String name() default "";
    DataType type() default DataType.TEXT;
    boolean PrimaryKey() default false;
    boolean NotNull() default false;
    String[] Tags() default {};
}
