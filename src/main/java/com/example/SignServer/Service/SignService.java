package com.example.SignServer.Service;

import com.example.SignServer.Dto.TokenDto;
import com.example.SignServer.Dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SignService {
    UserDto SignUp(UserDto userDto);

    TokenDto SignIn(UserDto userDto);

    List<UserDto> ReadAllUser();

    UserDto ReadUserById(Long id);

    UserDto PatchUser(Long id, UserDto userDto);

    UserDto DeleteUser(Long id);
}
