package cc.rapidev.qqbot.database.entity.annotations;

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

    boolean notnull() default false;

    String defaultValue() default "";

}
