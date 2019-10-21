/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.sql;

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
