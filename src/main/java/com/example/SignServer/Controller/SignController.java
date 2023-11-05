package com.example.SignServer.Controller;

import com.example.SignServer.Dto.TokenDto;
import com.example.SignServer.Dto.UserDto;
import com.example.SignServer.Service.SignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class SignController {
    private final SignService signService;

    public SignController(@Autowired SignService signService){ //의존성 주입
        this.signService = signService;
    }

    @PostMapping("/signup") //회원가입
    public ResponseEntity<UserDto> SignUp(@RequestBody UserDto userDto){
        UserDto createUser = signService.SignUp(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(createUser);
    }

    @PostMapping("/signin") //로그인
    public ResponseEntity<TokenDto> SignIn(@RequestBody UserDto userDto){
        TokenDto login = signService.SignIn(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(login);
        
    }

    @GetMapping() //모든 User 정보 조회
    public ResponseEntity<List<UserDto>> ReadAllUser(){
        List<UserDto> userList = signService.ReadAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/{id}") //회원번호로 User 정보 조회
    public ResponseEntity<UserDto> ReadUserById(@PathVariable("id") Long id){
        UserDto readUser = signService.ReadUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(readUser);
    }

    @PatchMapping("/patch/{id}") //회원정보 수정
    public ResponseEntity<UserDto> PatchUser(@PathVariable("id") Long id,
                                             @RequestBody UserDto userDto){
        UserDto patchUser = signService.PatchUser(id,userDto);
        return ResponseEntity.status(HttpStatus.OK).body(patchUser);
    }

    @DeleteMapping("/delete/{id}") //회원번호로 User 삭제
    public ResponseEntity<UserDto> DeleteUser(@PathVariable("id") Long id){
        UserDto deleteUser = signService.DeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleteUser);
    }
}
