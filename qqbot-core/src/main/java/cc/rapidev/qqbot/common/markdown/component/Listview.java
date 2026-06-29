package cc.rapidev.qqbot.common.markdown.component;

import cc.rapidev.qqbot.common.utils.StringUtils;

import java.util.stream.Collectors;

/**
 * @author leibrother
 */
public class Listview implements BlockComponent {

    private final Block<BlockComponent> block = new Block<>();

    public Listview add(BlockComponent... components) {
        this.block.add(components);
        return this;
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        int sequence = 0;
        for (Component component : this.block.components()) {
            sequence += 1;
            if (component instanceof Item item) {
                if (item.isOrderly()) {
                    builder.append(sequence);
                }
                builder.append(component.render());
            } else {
                String rendered = component.render()
                        .lines()
                        .map(line -> StringUtils.isEmpty(line) ? "" : "    " + line)
                        .collect(Collectors.joining("\n"));
                builder.append(rendered);
            }
        }
        builder.append("\n");
        return builder.toString();
    }

}
