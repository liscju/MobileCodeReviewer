package pl.edu.agh.mobilecodereviewer.view.activities.utilities;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.mobilecodereviewer.view.activities.utilities.syntax.PrettifyHighlighter;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.syntax.SyntaxHighlighter;

public class HtmlPrettifier {
    private String extension;
    private List<String> content;

    public HtmlPrettifier(String extension, List<String> content) {
        this.extension = extension;
        this.content = new LinkedList<String>(content);
        if ( extension.toLowerCase().equals("py") ) {
            addAdditionalSemicolonToLinesToDealWithBugInJavaPrettifyLibraryWithPythonFiles();
        }
    }

    // Python stwarza na ten moment zajebiste problemy,trza sie bedzie zastanowic czy jest sens
    // zeby on wogle byl....
    private void addAdditionalSemicolonToLinesToDealWithBugInJavaPrettifyLibraryWithPythonFiles() {
        for (int i = 0; i < content.size(); i++) {
                content.set(i , content.get(i) + ";");
        }
    }

    public String[] buildHtmlContent() {
        SyntaxHighlighter prettifyHighlighter = new PrettifyHighlighter();
        StringBuilder joinedSourceBuilder = new StringBuilder();
        for (int i=0;i<content.size()-1;i++) {
            joinedSourceBuilder.append( content.get(i) + "\n");
        }
        joinedSourceBuilder.append( content.get( content.size()-1) );

        String joinedSourceCode = joinedSourceBuilder.toString();
        String prettifiedSourceCode = prettifyHighlighter.highlight(joinedSourceCode, extension);
        String[] sourceCodeAfterPrettyfying = prettifiedSourceCode.split("\n");
        if (this.extension.toLowerCase().equals("py")) {
            removeAdditionalSemicolonFromLineToDealWithBugInJavaPrettifyLibraryWithPythonFiles(sourceCodeAfterPrettyfying);
        }
        return sourceCodeAfterPrettyfying;
    }

    private void removeAdditionalSemicolonFromLineToDealWithBugInJavaPrettifyLibraryWithPythonFiles(String[] sourceCodeAfterPrettyfying) {
        for (int i = 0; i < sourceCodeAfterPrettyfying.length; i++) {
            String currLine = sourceCodeAfterPrettyfying[i];
            sourceCodeAfterPrettyfying[i] = currLine.substring(0, currLine.length() );
        }
    }
}
