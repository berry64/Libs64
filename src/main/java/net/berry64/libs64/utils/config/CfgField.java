package net.berry64.libs64.utils.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CfgField {
    String name() default "";
}
