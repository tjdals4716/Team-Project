package com.example.commentserver.repository;
import com.example.commentserver.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVoteId(Long voteId);
}
