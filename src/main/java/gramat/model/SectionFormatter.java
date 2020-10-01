//package gramat.model;
//
//import gramat.actions.Action;
//import gramat.am.formatting.AmFormatter;
//
//import java.util.stream.Collectors;
//
//public class SectionFormatter extends AmFormatter {
//
//    private final SectionGrammar grammar;
//
//    public SectionFormatter(SectionGrammar grammar, Appendable output) {
//        super(output);
//        this.grammar = grammar;
//    }
//
//    public void write(Section section) {
//        for (var link : grammar.links) {
//            if (link.source == section.initial) {
//                write("->");
//                value(link.source.id);
//                newLine();
//            }
//
//            write(link);
//
//            if (link.source == section.accepted) {
//                value(link.source.id);
//                write("<=");
//                newLine();
//            }
//        }
//    }
//
//    public void write(Link link) {
//        if (link instanceof LinkSymbol) {
//            var linkSymbol = (LinkSymbol)link;
//
//            write(link.source.id);
//            write(" -> ");
//            write(link.target.id);
//            write(" : ");
//            write(amEditorString(linkSymbol.symbol.toString()));
//            newLine();
//
//            var beforeActions = linkSymbol.beforeActions.stream().map(Action::toString).collect(Collectors.joining("\n"));
//
//            if (beforeActions.length() > 0) {
//                write(link.source.id);
//                write(" -> ");
//                write(link.target.id);
//                write(" !< ");
//                write(amEditorString(beforeActions));
//                newLine();
//            }
//
//            var afterActions = linkSymbol.afterActions.stream().map(Action::toString).collect(Collectors.joining("\n"));
//
//            if (afterActions.length() > 0) {
//                write(link.source.id);
//                write(" -> ");
//                write(link.target.id);
//                write(" !> ");
//                write(amEditorString(afterActions));
//                newLine();
//            }
//        }
//        else {
//            write(link.source.id);
//            write(" -> ");
//            write(link.target.id);
//            newLine();
//        }
//    }
//
//    private String amEditorString(String str) {
//        return str
//                .replace("\\", "\\\\")
//                .replace(",", "\\,")
//                .replace(":", "\\:")
//                .replace("\n", "\\\n");
//    }
//
//}
