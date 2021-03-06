package pl.edu.agh.mobilecodereviewer.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.List;

import pl.edu.agh.mobilecodereviewer.R;
import pl.edu.agh.mobilecodereviewer.controllers.api.ModifiedFilesTabController;
import pl.edu.agh.mobilecodereviewer.controllers.api.SourceExplorerController;
import pl.edu.agh.mobilecodereviewer.model.ChangeStatus;
import pl.edu.agh.mobilecodereviewer.model.FileInfo;
import pl.edu.agh.mobilecodereviewer.view.activities.resources.ExtraMessages;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.ModifiedFilesViewListAdapter;
import pl.edu.agh.mobilecodereviewer.view.activities.utilities.refresh.RefreshableTabBaseActivity;
import pl.edu.agh.mobilecodereviewer.view.api.ModifiedFilesTabView;
import roboguice.inject.InjectView;

/**
 * Activity show list of modified files and enables navigating through them
 * as well as displaying detailed view of selected file.
 *
 * @author AGH
 * @version 0.1
 * @since 0.1
 */
public class ModifiedFilesTab extends RefreshableTabBaseActivity implements ModifiedFilesTabView {

    private ModifiedFilesViewListAdapter filesViewListAdapter;

    /**
     * Associated controller which make actions to activity events
     */
    @Inject
    private ModifiedFilesTabController controller;

    @Inject
    private SourceExplorerController sourceExplorerController;

    /**
     * List of modified files view
     */
    @InjectView(R.id.modifiedFilesTabListView)
    private ListView modifiedFilesTabListView;

    /**
     * No arg constructor,main for use by di and android framework
     */
    public ModifiedFilesTab() {
    }

    /**
     * Construct activity with given controller,for test only
     *
     * @param controller Controller to be used
     */
    public ModifiedFilesTab(ModifiedFilesTabController controller) {
        this.controller = controller;
    }

    /**
     * Invoked on start of the acivity, initialize view,
     * and show list of modified files
     *
     * @param savedInstanceState Last state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,controller);
        setContentView(R.layout.activity_modified_files_tab);

        Intent intent = getIntent();

        registerController(controller);
        controller.initializeData(this,intent.getStringExtra(ExtraMessages.CHANGE_EXPLORER_SELECTED_CHANGE_ID) );
        refresh();
    }

    @Override
    protected void onResume(){
        super.onResume();
        controller.checkIfFilesPendingCommentsChanged();
    }

    /**
     * According to given list of modified files , displays it
     * on the list of modified files on frame
     *
     * @param filesList List of modified files to display
     * @param status
     */
    @Override
    public void showFiles(final List<FileInfo> filesList, final ChangeStatus status, List<Boolean> hasPendingComments) {
        modifiedFilesTabListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showFileDetails(i, filesList, status);
            }
        });

        filesViewListAdapter = new ModifiedFilesViewListAdapter(this, filesList, hasPendingComments);
        modifiedFilesTabListView.setAdapter(filesViewListAdapter);
    }

    @Override
    public void refreshPendingComments(List<Boolean> hasPendingComments) {
        if(filesViewListAdapter != null){
            filesViewListAdapter.updateHasPendingComments(hasPendingComments);
            filesViewListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Show content of the file {@link pl.edu.agh.mobilecodereviewer.view.activities.SourceExplorer}
     */
    private void showFileDetails(int selectedFileIndex, List<FileInfo> filesInfos, ChangeStatus status) {
        sourceExplorerController.preInitialize(filesInfos, status);
        Intent intent = new Intent(getApplicationContext(), SourceExplorer.class);
        intent.putExtra(ExtraMessages.MODIFIED_FILES_SELECTED_FILE_INDEX, selectedFileIndex);
        startActivity(intent);
    }
}
