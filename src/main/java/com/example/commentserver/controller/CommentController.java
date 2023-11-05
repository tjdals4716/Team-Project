package com.example.commentserver.controller;

import com.example.commentserver.entity.Comment;
import com.example.commentserver.service.CommentServicempl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@CrossOrigin("*") //요청의 주제가 특정 도메인을 제안하는 부분, 전체를 허용한다는 뜻, 보안때문에 설정
@RestController //사용자 입력을 http방식에 어떠한 경로에 맞춰서 호출하면 거기에 맞춰서 각각의 서비스를 호출하도록 구성이된 어노테이션
@RequestMapping("/api") //어떤 경로로 사용자의 요청을 받을지, 메인 주소 옆에다 /api이걸 붙혀줘야함

//실제로 서비스의 명령을 내리기 위해서는 사용자의 요청을 직접적으로 받는 부분이 필요한데 그 부분이 바로 여기 컨트롤러
//컨트롤러는 서비스를 찾고 서비스는 레포지토리를 찾고 이런 방식임
//컨트롤러는 사용자의 입력을 받아서 서비스한테 시키는 역할을 하는 애

public class CommentController {

    @Autowired
    private CommentServicempl commentService;

    //미디어 추가 기능 테스트 할 때 {미디어 이름, 컨텐츠 != 스페이스 바} 확인하기
    //이미지, 동영상 추가 기능
    @PostMapping("/comments/media")
    public ResponseEntity<String> uploadMedia(
            @RequestPart("mediaFile") MultipartFile mediaFile,
            @RequestParam("mediaName") String mediaName,
            @RequestParam("content") String content
    ) {
        try {
            commentService.uploadMedia(mediaFile, mediaName, content);
            return ResponseEntity.ok("이미지 및 동영상이 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 및 동영상 업로드에 실패하였습니다.");
        }
    }

    //미디어 추가 기능 테스트 할 때 {미디어 이름, 컨텐츠 != 스페이스 바} 확인하기
    //대댓글에 이미지, 동영상 추가 기능
    @PostMapping("/comments/media/{id}")
    public ResponseEntity<String> MediaComment(
            @PathVariable("id") long id,
            @RequestPart("mediaFile") MultipartFile mediaFile,
            @RequestParam("mediaName") String mediaName,
            @RequestParam("content") String content
    ) {
        try {
            commentService.MediaComment(id, mediaFile, mediaName, content);
            return ResponseEntity.ok("이미지 및 동영상이 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 및 동영상 업로드에 실패했습니다.");
        }
    }

    //대댓글 기능
    @PostMapping("/comments/{id}")
    public ResponseEntity<Comment> addComment(
            @PathVariable("id") long id,
            @RequestBody Comment reply
    ) {
        try {
            Comment addedReply = commentService.addComment(id, reply);
            if (addedReply != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(addedReply);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //좋아요 추가 기능
    @PostMapping("/comments/like/{id}")
    public ResponseEntity<Comment> likeComment(@PathVariable("id") long id) {
        try {
            Comment likedComment = commentService.likeComment(id);
            if (likedComment != null) {
                return ResponseEntity.ok(likedComment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //좋아요 취소 기능
    @PostMapping("/comments/unlike/{id}")
    public ResponseEntity<Comment> unlikeComment(@PathVariable("id") long id){
        try {
            Comment unlikedComment = commentService.unlikeComment(id);
            if (unlikedComment != null) {
                return ResponseEntity.ok(unlikedComment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //댓글 신고 기능
    @PostMapping("/comments/report/{id}")
    public ResponseEntity<Comment> reportComment(
            @PathVariable("id") long id,
            @RequestBody String reportReason
    ) {
        try {
            Comment reportComment = commentService.reportComment(id, reportReason);
            if (reportComment != null) {
                return ResponseEntity.ok(reportComment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //추가
    @PostMapping("/comments") //주소 규칙이 들어있음. 메인주소에다가 api -> comment 순서대로 경로를 입력하고 Post방식으로 http로 호출을 하게 되면 실제로 comment를 생성하는 create액션을 실행을 시킬 수 있다는 뜻
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) { //@RequestBody는 실제로 comment를 만들기 위한 데이터를 전달하는 부분이라는 것을 의미.
        try{
            ResponseEntity
                    .status(HttpStatus.CREATED) //정상적으로 처리가 됐는지 에러가 발생했는지 http의 통신에 대한 결과물을 보여주는 부분, 생성같은 경우는 CREATED로 뒤를 처리하는 방식
                    .body(commentService.save(comment)); //사용자에게 입력받을 값을 서비스에게 넘겨주는 부분
        } catch(Exception e){
            e.printStackTrace(); //여긴 서비스랑 똑같음
        }
        return null;
    }

    //조회
    @GetMapping("/comments/{id}") //어떤 데이터를 조회할때 http에 Get이라는 방식으로 주소를 호출하게 된다는 뜻(조회)
    public ResponseEntity<Optional<Comment>> getCommentById(@PathVariable("id") long id) {
        try{
            return ResponseEntity.ok(commentService.findById(id)); //응답받았을때의 객체는 여기서 comment. http 상태가 ok라는 상태를 말한다. 정상적으로 http 메소드가 호출이 됐다는 뜻
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //수정
    @PutMapping("/comments/{id}") //두가지의 인자 중 하나는 주소에서 가져옴
    public ResponseEntity<Comment> updateComment(
            @PathVariable("id") long id, //@PathVariable은 주소 옆에 붙는 id값을 어떤 것으로 집어넣겠냐고 묻는 것임. 우리는 long 타입으로 지정한 id라고하는 맴버 변수를 사용한다는 뜻
            @RequestBody Comment comment //두가지의 인자 중 하나는 @RequestBody에서 가져옴
    ) {
        //컨트롤러는 사용자 요청을 받아서 서비스에게 처리하라고 던지는 역할만 하기 때문에 변경에 있어서는 코드가 그렇게 길지 않음
        try{
            ResponseEntity
                    .status(HttpStatus.CREATED) //http통신의 어떤 상태를 가리키는 것
                    .body(commentService.update(id, comment)); //commentService에서 정의한 updata란 함수가 있었음. 여기에 id와 comment 각각의 인자를 넘겨주면 됨.
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") long id) {
        try{
            commentService.delete(id); //commentService에게 시킨다는 구문. 삭제를 시킬때 넘겨줄 값은 위에 @PathVariable("id") long id로 부터 넘겨받는 id 값
            ResponseEntity.noContent(); //content가 더이상 없다는 http 상태값
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
