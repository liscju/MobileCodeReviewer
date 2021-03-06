package pl.edu.agh.mobilecodereviewer.view.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.io.Files;
import com.google.inject.Inject;

import java.util.List;

import pl.edu.agh.mobilecodereviewer.R;
import pl.edu.agh.mobilecodereviewer.controllers.api.SourceExplorerController;
import pl.edu.agh.mobilecodereviewer.model.Line;
import pl.edu.agh.mobilecodereviewer.model.SourceCode;
import pl.edu.agh.mobilecodereviewer.model.SourceCodeDiff;
import pl.edu.agh.mobilecodereviewer.view.activities.base.BaseActivity;
import pl.edu.agh.mobilecodereviewer.view.activities.resources.ExtraMessages;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.comments.InsertCommentDialogBuilder;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.SingleLineCommentViewListAdapter;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.SourceCodeDiffViewListAdapter;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.SourceCodeListAdapter;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.SourceCodeViewListAdapter;
import pl.edu.agh.mobilecodereviewer.view.api.SourceExplorerView;
import roboguice.inject.InjectView;

/**
 * Activity is primary point for exploring changes of the file for a given change.
 * Source Explorer show changes of the file,comments of the lines, give
 * tools to add comment etc
 *
 * @author AGH
 * @version 0.1
 * @since 0.1
 */
public class SourceExplorer extends BaseActivity implements SourceExplorerView {
    public static final String COMMENT_ADDING_NOT_AVALAIBLE = "Adding comment is not avalaible for Anonymous or when change status is merged or abandoned";
    private int visibleFileIndex;

    private Menu menu;
    private MenuItem prevChangeNavigation;
    private MenuItem nextChangeNavigation;

    private final Context context = this;

    private AlertDialog commentListDialog;
    /**
     * Associated controller which make actions to activity events
     */
    @Inject
    private SourceExplorerController controller;

    /**
     * List View which shows source code and comments
     */
    @InjectView(R.id.sourceLinesListView)
    private ListView sourceLinesListView;

    /**
     * Initialize view and request update of source code list
     *
     * @param savedInstanceState Saved instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_explorer);
        int toShowIndex = getIntent().getIntExtra(ExtraMessages.MODIFIED_FILES_SELECTED_FILE_INDEX, 0);
        initialize(toShowIndex);
    }

    private void initialize(int fileIndex){
        if(controller.fileExists(fileIndex)){
            visibleFileIndex = fileIndex;
            controller.initializeData(this, fileIndex);
            initializeView();
        }
    }

    private void initializeView() {
        final SourceExplorerView sourceView = this;

        if (controller.isAddingCommentAvalaible()) {
            sourceLinesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    return showCommentAddDialog(i, SourceExplorer.this, controller, (String) adapterView.getItemAtPosition(i));
                }
            });
        } else  {
            sourceLinesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    showMessage(COMMENT_ADDING_NOT_AVALAIBLE);
                    return true;
                }
            });
   
        }


        sourceLinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                controller.setCurrentLinePosition(position);
                controller.showComments(position);
            }
        });

        controller.initializeView();
    }

    @Override
    public void showCommentListDialog(final Line line) {
        View lineCommentsView = this.getLayoutInflater().inflate(R.layout.layout_line_comments, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(lineCommentsView);

        ListView listView = (ListView) lineCommentsView.findViewById(R.id.lineCommentsList);
        listView.setAdapter(new SingleLineCommentViewListAdapter(context, line, controller) );

        Button replyButton = (Button) lineCommentsView.findViewById(R.id.replyCommentButton);

        final Context sourceexplorer_context = this;
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertCommentDialogBuilder insertCommentDialogBuilder = new InsertCommentDialogBuilder(sourceexplorer_context, controller,
                        line.getLineNumber(), line.getContent());
                insertCommentDialogBuilder.showInsertCommentDialog();
            }
        });

        commentListDialog = alertDialogBuilder.create();

        commentListDialog.show();

        commentListDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    @Override
    public void dismissCommentListDialog(){
        if(commentListDialog != null){
            commentListDialog.dismiss();
        }
    }

    private boolean showCommentAddDialog(final int selected_line_number, SourceExplorer parent_acitivity_context, final SourceExplorerController parent_controller, String selected_line_content) {
        return new InsertCommentDialogBuilder(parent_acitivity_context, parent_controller, selected_line_number, selected_line_content).showInsertCommentDialog();
    }

    @Override
    public void clearSourceCode() {
        sourceLinesListView.setAdapter(null);
    }

    /**
     * Preparing activity's options menu.
     *
     * @inheritDoc
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.source_explorer, menu);
        this.prevChangeNavigation = menu.findItem(R.id.gotoPrevChange);
        this.nextChangeNavigation = menu.findItem(R.id.gotoNextChange);
        controller.setVisibilityOnSourceCodeNavigation();
        return true;
    }

    /**
     * Preparing activity's options menu onclick actions.
     *
     * @inheritDoc
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sourceDiffToggleButton) {
            controller.toggleDiffView();
        } else if (id == R.id.gotoNextChange) {
            controller.navigateToNextChange();
        } else if (id == R.id.gotoPrevChange) {
            controller.navigateToPrevChange();
        } else if (id == R.id.showHideLinesNumber) {
            SourceCodeListAdapter sourceCodeListAdapter = (SourceCodeListAdapter) sourceLinesListView.getAdapter();
            controller.toogleVisibilityOfLineNumbers(sourceCodeListAdapter);
        } else if (id == R.id.gotoNextFile) {
            initialize(visibleFileIndex + 1);
        } else if (id == R.id.goToPrevFile) {
            initialize(visibleFileIndex -1);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show content from given source code
     *
     * @param file_path
     * @param sourceCode {@link pl.edu.agh.mobilecodereviewer.model.SourceCode}
     */
    @Override
    public void showSourceCode(String file_path, final SourceCode sourceCode) {
        String extension = Files.getFileExtension(file_path);
        final SourceCodeViewListAdapter sourceCodeViewListAdapter =
                new SourceCodeViewListAdapter(this, extension,sourceCode);

        sourceLinesListView.setAdapter(sourceCodeViewListAdapter);
    }

    @Override
    public void showSourceCodeDiff(String file_path,SourceCodeDiff sourceCodeDiff,List<Boolean> hasComments, List<Boolean> hasPendingComments) {
        String extension = Files.getFileExtension(file_path);
        final SourceCodeDiffViewListAdapter sourceCodeDiffViewListAdapter =
                new SourceCodeDiffViewListAdapter(this, extension,sourceCodeDiff,hasComments, hasPendingComments);

        sourceLinesListView.setAdapter(sourceCodeDiffViewListAdapter);
    }

    @Override
    public void setInterfaceForCode() {
        if (menu != null) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.source_code_diff_icon));
        }
        hideNavigationButtons();
    }

    @Override
    public void hideNavigationButtons() {
        if (prevChangeNavigation == null || nextChangeNavigation == null)
            return;
        prevChangeNavigation.setVisible(false);
        nextChangeNavigation.setVisible(false);
        prevChangeNavigation.setEnabled(false);
        nextChangeNavigation.setEnabled(false);
    }

    @Override
    public void setInterfaceForDiff() {
        if (menu != null)
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.source_code_code_icon));
        showNavigationButtons();
    }

    @Override
    public void showNavigationButtons() {
        if (prevChangeNavigation == null || nextChangeNavigation == null)
            return;
        prevChangeNavigation.setVisible(true);
        nextChangeNavigation.setVisible(true);
        prevChangeNavigation.setEnabled(true);
        nextChangeNavigation.setEnabled(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void gotoLine(int line) {
        sourceLinesListView.smoothScrollToPosition(line);
        sourceLinesListView.setSelection(line);
        sourceLinesListView.setSelected(true);
    }

    @Override
    public void setTitle(String fileName) {
        super.setTitle(fileName);
    }

}

















