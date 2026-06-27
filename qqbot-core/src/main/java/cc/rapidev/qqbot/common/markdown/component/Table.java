package cc.rapidev.qqbot.common.markdown.component;

import cc.rapidev.qqbot.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author leibrother
 */
public class Table implements BlockComponent {

    public enum Align {
        LEFT(":----"),
        CENTER(":---:"),
        RIGHT("----:");

        private final String value;

        Align(String text) {
            this.value = text;
        }

        public String value() {
            return value;
        }
    }

    private final List<Block<Text>> heads;
    private final List<Align> aligns;
    private final List<List<Block<Text>>> rows;

    public Table(List<Block<Text>> heads, List<Align> aligns, List<List<Block<Text>>> rows) {
        this.heads = heads;
        this.aligns = aligns;
        this.rows = new ArrayList<>();
        for (List<Block<Text>> row : rows) {
            this.addRow(row);
        }
    }

    private void addRow(List<Block<Text>> row) {
        int diff = Math.max(0, row.size() - this.heads.size());
        IntStream.range(0, diff).forEach((_) -> {
            this.heads.add(Block.of(Text.of("-")));
            this.aligns.add(Align.LEFT);
        });
        this.rows.add(row);
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        // head
        builder.append("|");
        builder.append(this.heads.stream().map(th -> StringUtils.packing(" ", th.render().strip())).collect(Collectors.joining("|")));
        builder.append("|");
        builder.append("\n");
        builder.append("|");
        builder.append(this.aligns.stream().map(align -> StringUtils.packing(" ", align.value())).collect(Collectors.joining("|")));
        builder.append("|");
        // rows
        for (List<Block<Text>> row : this.rows) {
            builder.append("\n");
            builder.append("|");
            builder.append(row.stream().map(col -> StringUtils.packing(" ", col.render().strip())).collect(Collectors.joining("|")));
            builder.append("|");
        }
        builder.append("\n");
        return builder.toString();
    }

    public static TableBuilder builder() {
        return new TableBuilder();
    }

    public static class TableBuilder {

        private final List<Block<Text>> heads = new ArrayList<>();
        private final List<Align> aligns = new ArrayList<>();
        private final List<List<Block<Text>>> rows = new ArrayList<>();

        public TableBuilder head(List<Block<Text>> heads, List<Align> aligns) {
            this.heads.clear();
            this.aligns.clear();
            this.heads.addAll(heads);
            this.aligns.addAll(aligns);
            return this;
        }

        public TableBuilder rows(List<List<Block<Text>>> rows) {
            this.rows.clear();
            this.rows.addAll(rows);
            return this;
        }

        public Table build() {
            return new Table(heads, aligns, rows);
        }

        public TableHeaderBuilder header() {
            return new TableHeaderBuilder(this);
        }

        public TableBodyBuilder body() {
            return new TableBodyBuilder(this);
        }

    }

    public static class TableHeaderBuilder {

        private final TableBuilder builder;
        private final List<Block<Text>> heads = new ArrayList<>();
        private final List<Align> aligns = new ArrayList<>();

        public TableHeaderBuilder(TableBuilder builder) {
            this.builder = builder;
        }

        public TableHeaderBuilder head(Block<Text> head) {
            return this.head(head, Align.LEFT);
        }

        public TableHeaderBuilder head(Block<Text> head, Align align) {
            this.heads.add(head);
            this.aligns.add(align);
            return this;
        }

        public TableBodyBuilder body() {
            return this.builder.head(this.heads, this.aligns).body();
        }

    }

    public static class TableBodyBuilder {
        private final TableBuilder builder;
        private final List<List<Block<Text>>> rows = new ArrayList<>();

        public TableBodyBuilder(TableBuilder builder) {
            this.builder = builder;
        }

        public TableRowBuilder row() {
            return new TableRowBuilder(this);
        }

        public TableBodyBuilder row(List<Block<Text>> cols) {
            this.rows.add(cols);
            return this;
        }

        public Table build() {
            return this.builder.rows(this.rows).build();
        }

    }

    public static class TableRowBuilder {
        private final TableBodyBuilder builder;
        private final List<Block<Text>> cols = new ArrayList<>();

        public TableRowBuilder(TableBodyBuilder builder) {
            this.builder = builder;
        }

        public TableRowBuilder col(Block<Text> col) {
            this.cols.add(col);
            return this;
        }

        public TableRowBuilder row() {
            return this.builder.row(cols).row();
        }

        public Table build() {
            return this.builder.row(this.cols).build();
        }

    }

}
