package com.fastdfs.model;

public class CreditorFile {
    private Integer id;

    private String groupName;

    private String remoteFilename;

    private Long fileSize;

    private String fileType;

    private String oldFilename;

    private Integer ciId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemoteFilename() {
        return remoteFilename;
    }

    public void setRemoteFilename(String remoteFilename) {
        this.remoteFilename = remoteFilename;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getOldFilename() {
        return oldFilename;
    }

    public void setOldFilename(String oldFilename) {
        this.oldFilename = oldFilename;
    }

    public Integer getCiId() {
        return ciId;
    }

    public void setCiId(Integer ciId) {
        this.ciId = ciId;
    }

    @Override
    public String toString() {
        return "CreditorFile{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", remoteFilename='" + remoteFilename + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", oldFilename='" + oldFilename + '\'' +
                ", ciId=" + ciId +
                '}';
    }
}