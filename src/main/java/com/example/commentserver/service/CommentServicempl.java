package com.example.commentserver.service;

import com.example.commentserver.entity.Comment;
import com.example.commentserver.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
public class CommentServicempl {

    @Autowired //서비스도 일체의 객체이기 때문에 인스턴스로 생성되어야 사용될 수 있음
    private CommentRepository commentRepository; //서비스가 레포지토리에게 명령하기 위해서는 오토와이어라는 어노테이션을 사용해야함

    private final String imageUploadDirectory = "MediaDirectory"; // 이미지, 동영상 업로드 디렉토리(지금은 필요없음. 나중에 미디어 파일 잘 들어갔나 확인할 때 사용할 것)

    //미디어 추가 기능 테스트 할 때 {미디어 이름, 컨텐츠 != 스페이스 바} 확인하기
    //이미지, 동영상 추가 기능
    public void uploadMedia(MultipartFile mediaFile, String mediaName, String content) {
        try {
            byte[] mediaBytes = mediaFile.getBytes();

            Comment comment = new Comment();
            comment.setMediaData(mediaBytes);
            comment.setMediaName(mediaName);
            comment.setContent(content);

            commentRepository.save(comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //미디어 추가 기능 테스트 할 때 {미디어 이름, 컨텐츠 != 스페이스 바} 확인하기
    //대댓글에 이미지, 동영상 추가 기능
    public Comment MediaComment(long id, MultipartFile mediaFile, String mediaName, String content) {
        try {
            Comment parentComment = commentRepository.findById(id).orElse(null);
            if (parentComment != null) {
                byte[] mediaBytes = mediaFile.getBytes();

                Comment mediaComment = new Comment();
                mediaComment.setMediaData(mediaBytes);
                mediaComment.setMediaName(mediaName);
                mediaComment.setContent(content);
                mediaComment.setParentComment(parentComment);

                parentComment.getChildrenComment().add(mediaComment);

                return commentRepository.save(mediaComment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //대댓글 추가 기능
    public Comment addComment(long id, Comment reply) {
        try {
            Comment parentComment = commentRepository.findById(id).orElse(null);
            if (parentComment != null) {
                reply.setParentComment(parentComment); // 대댓글의 부모 댓글 설정
                parentComment.getChildrenComment().add(reply); // 부모 댓글의 대댓글 목록에 추가
                return commentRepository.save(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //좋아요 추가 기능
    public Comment likeComment(long id) {
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null) {
                comment.setLikes(comment.getLikes() + 1);
                return commentRepository.save(comment);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //좋아요 취소 기능
    public Comment unlikeComment(long id) {
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null && comment.getLikes() > 0) {
                comment.setLikes(comment.getLikes() - 1);
                return commentRepository.save(comment);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //댓글 신고 기능
    public Comment reportComment(long id, String reportReason) {
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null) {
                comment.setReport(true); //댓글을 신고 상태로 표시
                comment.setReportReason(reportReason); //댓글 신고 이유 저장
                return commentRepository.save(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //추가
    //@Override
    public Comment save(Comment comment) {
        try{ //얘기치 못한 상황에 있어서 이것들에 대한 예외를 적절하게 처리해주는게 안정적인 프로그램을 구성할 수 있기 때문에 try catch문 사용
            return commentRepository.save( //commentRepository에게 저장하라고 시키는 명령
                    new Comment( //댓글 객체를 새로 만들어서 이걸 데이터베이스에 저장하라고 시키는 명령
                            comment.getVoteId(),
                            comment.getContent(),
                            comment.getTime() //이렇게 총 세개의 인자를 넣어서 리턴해주면 레포지토리가 받아서 데이터베이스로 가져가는 형식
                    )
            );
        } catch (Exception e){
            e.printStackTrace(); //어떤 예외가 발생했는지 보는 코드
        }
        return null; //try catch에서도 마지막에는 따로 반환할게 없다면 최종적으론 null을 반환.
    }

    //조회
    //@Override
    public Optional<Comment> findById(Long id) { //서비스를 할땐 항상 try catch를 항상 열고 시작하는게 좋다
        try{
            Optional<Comment> commentData = commentRepository.findById(id); //commentRepository에서 findById를 사용한다는 뜻. 그리고 실제로 넘겨주는 id값을 사용.
            if(commentData.isPresent()){ //commentData가 실제로 존재하는 지를 묻는 것. isPresent : 문제여부를 확인할 수 있는 것
                return commentData; //내가 조회한 데이터를 리턴
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null; //아무것도 없다면 null을 리턴.
    }

    //수정
    //@Override
    public Comment update(Long id, Comment comment) { //조회에 사용하기 위한 아이디를 첫번째 파라미터로 사용
        try{
            Optional<Comment> commentData = commentRepository.findById(id); //업데이트를 하려면 조회해오는 과정이 반드시 필요하다. 특정 개체를 사용하기 위해서는 Optional이라는 특수한 개체를 사용
            if(commentData.isPresent()){ //이 데이터가 실제로 존재한다면
                Comment _comment = commentData.get(); //구데이터(commentData)를 신데이터(_comment)에다 할당해줘서 넣어줘야한다. 위에 comment얘는 구데이터에 덮어 쓸 사용자로부터 입력받을 값을 말함.
                _comment.setVoteId(comment.getVoteId()); //새로운 데이터를 Setter로 덮어 쓴다. Getter영역은 사용자로부터 입력값을 가져오는 곳
                _comment.setContent(comment.getContent()); //사용자로부터 입력받은 값을 새로운 데이터 객체에다가 넣어 줄 수 있다는 뜻.
                commentRepository.save(_comment); //객체를 가지고 Setter함수로 정의를 했다면 commentRepository를 통해서 실제로 데이터베이스에 반영될 수 있도록 신데이터 객체를 저장해줘야 한다.
                return _comment;
            } else { //이 데이터가 실제로 존재하지 않는다면
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return null; //최종적으로 try문 안에서 리턴값을 받지 못했다면 null을 반환
    }

    //삭제
    //@Override
    public void delete(Long id) { //삭제는 특별히 반환값이 없기 때문에 void로 처리하면 됨.
        try {
            commentRepository.deleteById(id); //일단 레포지토리를 불러와서 deleteById를 작성하고 id를 넘겨주기만 하면 그 데이터는 삭제가 됨.
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
