package pl.edu.agh.mobilecodereviewer.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Class represent data returned by gerrit instance
 * about information of the change
 */
public class ChangeInfoDTO {

    /**
     * Identifier
     */
    private String id;

    /**
     * Change Identifier
     */
    @SerializedName("change_id")
    private String changeId;

    /**
     * Subject of the change
     */
    private String subject;


    private String status;

    private AccountInfoDTO owner;

    private String project;

    private String branch;

    private String updated;

    private String created;

    private Integer insertions;

    private Integer deletions;

    private List<ChangeMessageInfoDTO> messages;

    private Map<String, LabelInfoDTO> labels;

    @SerializedName("permitted_labels")
    private Map<String, List<Integer>> permittedLabels;

    @SerializedName("_number")
    private Integer number;

    /**
     * Current revision identifier
     */
    @SerializedName("current_revision")
    private String currentRevision;

    private Boolean starred = false;

    private Boolean has_draft_comments = false;

    private Boolean mergeable = false;

    private Boolean reviewed = false;

    /**
     * Map between revision id and {@link pl.edu.agh.mobilecodereviewer.dto.RevisionInfoDTO}
     */
    private Map<String, RevisionInfoDTO> revisions;

    public Map<String, List<Integer>> getPermittedLabels() {
        return permittedLabels;
    }

    public void setPermittedLabels(Map<String, List<Integer>> permittedLabels) {
        this.permittedLabels = permittedLabels;
    }

    /**
     * Return current revision of the given change
     * @return Name of the revision
     */
    public String getCurrentRevision() {
        return currentRevision;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Set current revision
     * @param currentRevision Identifier of the revision
     */
    public void setCurrentRevision(String currentRevision) {
        this.currentRevision = currentRevision;
    }

    /**
     * Get association between revision id and {@link pl.edu.agh.mobilecodereviewer.dto.RevisionInfoDTO}
     * @return Map between revision id and {@link pl.edu.agh.mobilecodereviewer.dto.RevisionInfoDTO}
     */
    public Map<String, RevisionInfoDTO> getRevisions() {
        return revisions;
    }

    /**
     * Set Association between name and revision to give
     * @param revisions Given association
     */
    public void setRevisions(Map<String, RevisionInfoDTO> revisions) {
        this.revisions = revisions;
    }

    /**
     * Get subject of the change
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set subject to the give
     * @param subject New subject of the change
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get identifier of the change
     * @return Identifier of the change
     */
    public String getChangeId() {
        return changeId;
    }

    /**
     * Sets identifier of the change to given
     * @param changeId new identifier of the change
     */
    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    /**
     * Get identifier
     * @return identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Set identifier
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AccountInfoDTO getOwner() {
        return owner;
    }

    public void setOwner(AccountInfoDTO owner) {
        this.owner = owner;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getInsertions() {
        return insertions;
    }

    public void setInsertions(Integer insertions) {
        this.insertions = insertions;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<ChangeMessageInfoDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<ChangeMessageInfoDTO> messages) {
        this.messages = messages;
    }

    public Map<String, LabelInfoDTO> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, LabelInfoDTO> labels) {
        this.labels = labels;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Boolean getHas_draft_comments() {
        return has_draft_comments;
    }

    public void setHas_draft_comments(Boolean has_draft_comments) {
        this.has_draft_comments = has_draft_comments;
    }

    public Boolean getMergeable() {
        return mergeable;
    }

    public void setMergeable(Boolean mergeable) {
        this.mergeable = mergeable;
    }

    public Boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(Boolean reviewed) {
        this.reviewed = reviewed;
    }
}
