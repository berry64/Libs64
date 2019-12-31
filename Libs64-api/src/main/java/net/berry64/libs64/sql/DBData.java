package net.berry64.libs64.sql;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DBData {

    String name() default "";

    DataType type();

    boolean PrimaryKey() default false;

    boolean NotNull() default false;

    String[] Tags() default {};
}
