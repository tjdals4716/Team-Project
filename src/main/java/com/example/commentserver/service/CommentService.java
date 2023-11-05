package com.example.commentserver.service;

import com.example.commentserver.entity.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CommentService {
    public Comment save(Comment comment); //저장하게 되면 댓글이 저장 결과
    public Optional<Comment> findById(Long id); //Optional : Null값을 주려할때 알맞게 대응해주는 객체
    public Comment update(Long id, Comment comment); //고유한 아이디값으로 객체값을 찾고 새로 입력한 댓글값으로 덮게되는 것
    public void delete(Long id); //고유한 아이디값으로 객체를 찾고 그것을 삭제해버리는 것. 그러므로 특별한 반환값을 설정하지 않아도 됨.
}
