package com.example.SignServer.Service;

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

import java.util.List;
import java.util.stream.Collectors;
@Service
@Slf4j
public class SignServiceImpl implements SignService{

    private static final Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired PasswordEncoder passwordEncoder){ //의존성 주입
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public boolean CheckEmailDuplicate(String email){
//        return UserRepository.existsByEmail(email);
//    }
    @Override
    public UserDto SignUp(UserDto userDto) {
        UserEntity userEntity = userDto.dtoToEntity();
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);
        log.info("회원가입 완료! " + userEntity);
        return UserDto.entityToDto(userEntity);
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
