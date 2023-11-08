package com.example.SignServer.Dto;

import com.example.SignServer.Entity.UserEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class UserDto {
    private Long id; //회원번호. 자동생성
    @NotBlank(message = "아이디를 입력하세요")
    private String uid; //아이디
    @NotBlank(message = "비밀번호를 입력하세요")
    private String password; //비밀번호
//  private String confirmPassword;
    @NotBlank(message = "닉네임을 입력하세요")
    @Size(min = 2,max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요")
    private String nickname; //닉네임
    @NotBlank(message = "성별을 선택해주세요")
    private String gender; //성별
    private String age; //연령대
    private String mbti; //mbti
    private Long popular_point; //대중성 포인트
    private String roles;

    public static UserDto entityToDto(UserEntity userEntity) { // public static = 정적 메소드로 객체 생성 없이 호출 가능한 메소드
        return new UserDto(
                userEntity.getId(),
                userEntity.getUid(),
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
        return new UserEntity(id,uid,password,nickname,gender,age,mbti,popular_point, Collections.singletonList(roles));
    }
}
