package cc.rapidev.qqbot.common.markdown.component;

import cc.rapidev.qqbot.common.utils.CastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author leibrother
 */
public class Block<T extends Component> implements BlockComponent {

    private final List<T> components = new ArrayList<>();

    public List<T> components() {
        return Collections.unmodifiableList(components);
    }

    @SafeVarargs
    public final Block<T> add(T... components) {
        this.components.addAll(List.of(components));
        return CastUtils.cast(this);
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        for (Component component : components) {
            builder.append(component.render());
        }
        builder.append("\n");
        return builder.toString();
    }

    @SafeVarargs
    public static <T extends Component> Block<T> of(T... components) {
        return new Block<T>().add(components);
    }

}
