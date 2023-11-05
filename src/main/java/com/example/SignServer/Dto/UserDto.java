package com.example.SignServer.Dto;

import com.example.SignServer.Entity.UserEntity;
import lombok.*;

import java.util.Collections;

//import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class UserDto {
    private Long id; //회원번호
//    @NotEmpty(message = "Enter your email")
    private String email; //이메일
//    @NotEmpty(message = "Enter your password")
    private String password; //비밀번호
//    @NotEmpty(message = "Confirm your password")
//    private String confirmPassword;
//    @NotEmpty(message = "Enter your nickname")
    private String nickname; //닉네임

    private String gender; //성별
    private String age; //연령대
    private String mbti; //mbti
    private Long popular_point; //대중성 포인트
    private String role;

    public static UserDto entityToDto(UserEntity userEntity) { // public static = 정적 메소드로 객체 생성 없이 호출 가능한 메소드
        return new UserDto(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getNickname(),
                userEntity.getGender(),
                userEntity.getAge(),
                userEntity.getMbti(),
                userEntity.getPopular_point(),
                userEntity.getRoles().toString()
        );
    }

    public UserEntity dtoToEntity(){
        return new UserEntity(id,email,password,nickname,gender,age,mbti,popular_point, Collections.singletonList(role));
    }
}
