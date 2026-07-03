package cc.rapidev.qqbot.common.utils.el;

import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;

/**
 * @author leibrother
 */
public class Evaluator {

    public static Object eval(String expression, Map<String, Object> context) {
        Serializable compiled = MVEL.compileExpression(expression);
        return MVEL.executeExpression(compiled, context);
    }

}
