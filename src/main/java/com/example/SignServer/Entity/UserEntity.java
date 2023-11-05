package com.example.SignServer.Entity;

import com.example.SignServer.Dto.UserDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UserEntity implements UserDetails {
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

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
