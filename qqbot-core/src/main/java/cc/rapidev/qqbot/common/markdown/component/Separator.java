package cc.rapidev.qqbot.common.markdown.component;

/**
 * @author leibrother
 */
public class Separator implements BlockComponent {

    @Override
    public String render() {
        return "\n---\n\n";
    }

}
