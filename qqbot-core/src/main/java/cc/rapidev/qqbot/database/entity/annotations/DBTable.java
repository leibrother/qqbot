package cc.rapidev.qqbot.database.entity.annotations;

import java.lang.annotation.*;

/**
 * @author leibrother
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DBTable {

    String name() default "";

}
