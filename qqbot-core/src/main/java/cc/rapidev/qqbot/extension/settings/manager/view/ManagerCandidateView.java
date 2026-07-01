package cc.rapidev.qqbot.extension.settings.manager.view;

import cc.rapidev.qqbot.common.markdown.MarkdownUI;
import cc.rapidev.qqbot.common.markdown.MarkdownView;
import cc.rapidev.qqbot.common.markdown.component.Listview;
import cc.rapidev.qqbot.message.member.Member;

import java.util.List;

/**
 * @author leibrother
 */
public class ManagerCandidateView extends MarkdownView {

    public ManagerCandidateView(List<Member> members) {
        add(MarkdownUI.h1("请选择"));
        add(MarkdownUI.blockQuote("找到多个匹配的成员，请选择"));
        add(MarkdownUI.separator());
        Listview list = MarkdownUI.list();
        for (Member member : members) {
            list.add(MarkdownUI.item(MarkdownUI.cmdInput(member.openid(), member.username(), false)));
        }
        add(list);
    }

}
