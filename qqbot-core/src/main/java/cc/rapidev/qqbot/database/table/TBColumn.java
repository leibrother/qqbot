package cc.rapidev.qqbot.database.table;

import java.lang.annotation.*;

/**
 * @author leibrother
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TBColumn {

    String name() default "";

    String type() default "";

    boolean unique() default false;

    boolean nullable() default true;

    String defaultValue() default "";

}
