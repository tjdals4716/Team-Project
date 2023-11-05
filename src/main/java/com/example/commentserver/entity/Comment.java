package com.example.commentserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity //JPA가 이 객체를 인식하여 테이블에 쓸 준비를 하는 것이다.
@Table(name="comment") //테이블옵션을 이렇게 주면 테이블 이름을 옆과 같이 구성가능하다.

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //숫자를 부여하는 규칙은 보통 하나씩 증가하는 형태로 구성됨. ex)1. 2. 3.
    private Long id; //댓글 아이디

    @Column(name = "user_id")
    private Long userId; //투표 번호(유저 테이블 외래키 연결)

    @Column(name = "vote_id")
    private Long voteId; //투표 번호(투표 테이블 외래키 연결)

    @Column(name = "content")
    private String content; //댓글 내용

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment; //부모댓글(null일 경우 최상위 댓글)

    @JsonIgnore
    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> childrenComment = new ArrayList<>(); //자식댓글들(대댓글 기능)

    @Column(name = "media_data")
    private byte[] mediaData; //이미지, 동영상 데이터

    @Column(name = "media_name")
    private String mediaName; //이미지, 동영상 파일 이름

    @Column(name = "likes")
    private int likes = 0; //댓글 좋아요 기능, 0은 좋아요 수를 의미

    @Column(name = "time")
    private String time; //댓글 달때 달린 현재 시간 기능

    @Column(name = "report")
    private boolean report = false; //댓글이 신고되었는지 여부, 기존 댓글은 신고 상태가 아니니 false값 부여, 값이 1일 경우 신고 완료

    @Column(name = "report_reason")
    private String reportReason; //댓글 신고 이유

    @PrePersist //자동으로 날짜 및 시간을 설정하는 어노테이션
    protected void Createtime() {
        time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")); //현재시간을 저장
    }

    //키 빼고 생성자 모두 생성
    public Comment(Long voteId, String content, String mediaName) {
        this.voteId = voteId;
        this.content = content;
        this.mediaData = mediaData;
        this.mediaName = mediaName;
        this.time = time;
    }

    //빈 생성자도 생성
    public Comment() {

    }

    //그 다음 게터 세터 모두 생성
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

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getChildrenComment() {
        return childrenComment;
    }

    public void setChildrenComment(List<Comment> childrenComment) {
        this.childrenComment = childrenComment;
    }

    public byte[] getMediaData() {
        return mediaData;
    }

    public void setMediaData(byte[] mediaData) {
        this.mediaData = mediaData;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }
}
