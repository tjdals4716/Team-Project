package com.example.SignServer.Service;

import com.example.SignServer.Config.JwtTokenProvider;
import com.example.SignServer.Dto.TokenDto;
import com.example.SignServer.Dto.UserDto;
import com.example.SignServer.Repository.UserRepository;
import com.example.SignServer.Entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Slf4j
public class SignServiceImpl implements SignService{

    private static final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired PasswordEncoder passwordEncoder,
                           @Autowired JwtTokenProvider jwtTokenProvider){ //의존성 주입
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    public boolean CheckEmailDuplicate(String email){
//        return UserRepository.existsByEmail(email);
//    }
    @Override
    public UserDto SignUp(UserDto userDto) {
        UserEntity userEntity = userDto.dtoToEntity();
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(Collections.singletonList("ROLE_USER"));
        userRepository.save(userEntity);
        log.info("회원가입 완료! " + userEntity);
        return UserDto.entityToDto(userEntity);
    }

    public TokenDto SignIn(UserDto userDto){

        UserEntity userEntity = userRepository.getByEmail(userDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 email 입니다."));
        TokenDto tokenDto = new TokenDto();
        tokenDto.setEmail(userDto.getEmail());
        tokenDto.setRole(userDto.getRoles());
        if(!passwordEncoder.matches(userDto.getPassword(),userEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
        tokenDto.setToken(jwtTokenProvider.createToken(userEntity.getUsername(),userEntity.getRoles()));
        return tokenDto;

    }

    @Override
    public List<UserDto> ReadAllUser() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> UserDto.entityToDto(userEntity))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto ReadUserById(Long id) {
        UserEntity target = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다"));
        return UserDto.entityToDto(target);
    }

    @Override
    @Transactional
    public UserDto PatchUser(Long id, UserDto userDto) {
        UserEntity target = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원정보 수정 실패! " +
                        " 대상 회원이 존재하지 않습니다. "));
        target.patch(userDto);
        UserEntity updated = userRepository.save(target);
        return UserDto.entityToDto(updated);
    }

    @Override
    @Transactional
    public UserDto DeleteUser(Long id) {
        UserEntity target = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 탈퇴 실패! " +
                        " 대상 회원이 존재하지 않습니다. "));
        userRepository.delete(target);
        return UserDto.entityToDto(target);
    }
}
