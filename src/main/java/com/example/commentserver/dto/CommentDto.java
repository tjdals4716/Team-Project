package com.example.commentserver.dto;

import java.util.List;

public class CommentDto {
    private Long id; // 댓글 아이디
    private Long userId; // 유저 번호
    private Long voteId; // 투표 번호
    private String content; // 댓글 내용
    private Long parentComment; // 부모 댓글 아이디
    private List<CommentDto> childrenComment; // 자식 댓글들
    private int likes; // 댓글 좋아요 수
    private String mediaName; // 이미지 파일 이름
    private String time; // 댓글 작성 시간
    private Boolean report; //댓글 신고
    private String reportReason; //댓글 신고 이유

    //DTO는 주로 데이터 전송을 위한 용도로 사용됨.
    //이미지 데이터 자체는 엔티티 클래스 내에서 처리됨.
    //즉 이미지를 Comment에 저장하기 위해 엔티티 클래스에 이미 byte[] image 필드가 정의되어 있는 것.

    //생성자
    public CommentDto(Long id,Long userId, Long voteId, String content, int likes, String mediaName, String time) {
        this.id = id;
        this.userId = userId;
        this.voteId = voteId;
        this.content = content;
        this.likes = likes;
        this.mediaName = mediaName;
        this.time = time;
    }

    //게터 및 세터

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentComment() {
        return parentComment;
    }

    public void setParentComment(Long parentComment) {
        this.parentComment = parentComment;
    }

    public List<CommentDto> getChildrenComment() {
        return childrenComment;
    }

    public void setChildrenComment(List<CommentDto> childrenComment) {
        this.childrenComment = childrenComment;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getReport() {
        return report;
    }

    public void setReport(Boolean report) {
        this.report = report;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }
}
