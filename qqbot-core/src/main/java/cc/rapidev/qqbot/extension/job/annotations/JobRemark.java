package cc.rapidev.qqbot.extension.job.annotations;

import java.lang.annotation.*;

/**
 * @author leibrother
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobRemark {

    String value();

}
