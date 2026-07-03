package cc.rapidev.qqbot.rocokingdom.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.rocokingdom.repository.RocokingdomDex;

import java.util.List;

/**
 * @author leibrother
 */
public class RocokingdomDexListView extends MarkdownView {

    public RocokingdomDexListView(List<RocokingdomDex> dexList) {
        Listview list = MarkdownUI.list();
        for (RocokingdomDex dex : dexList) {
            list.add(MarkdownUI.item(MarkdownUI.cmdInput("洛克精灵图鉴 " + dex.getFullname(), dex.getNo() + " " + dex.getFullname(), false)));
        }
        add(list);
    }

}
