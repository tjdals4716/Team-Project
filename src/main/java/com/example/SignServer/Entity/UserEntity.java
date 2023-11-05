package com.example.SignServer.Entity;

import com.example.SignServer.Dto.UserDto;
import lombok.*;

import javax.persistence.*;

@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 회원번호 자동생성
    private Long id; //회원번호

    private String email; //이메일
    private String password; //비밀번호
    private String nickname; //닉네임
    private String gender; //성별
    private String age; //연령대
    private String mbti; //mbti
    private Long popular_point; //대중성 포인트

    public void patch(UserDto userDto) {
        if(!this.id.equals(userDto.getId()))
            throw new IllegalArgumentException("잘못된 회원번호가 입력되었습니다!");
        if(userDto.getPassword()!= null) //수정할 비밀번호가 입력되었다면
            this.password = userDto.getPassword();
        if(userDto.getNickname() != null) //수정할 닉네임이 입력되었다면
            this.nickname = userDto.getNickname();
        if(userDto.getMbti() != null) // 수정할 mbti가 입력되었다면
            this.mbti = userDto.getMbti();
    }
}
