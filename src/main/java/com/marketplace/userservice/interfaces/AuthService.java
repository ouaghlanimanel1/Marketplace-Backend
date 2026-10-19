package com.marketplace.userservice.interfaces;

import com.marketplace.userservice.model.dto.LoginDto;
import com.marketplace.userservice.model.dto.RegisterDto;
import com.marketplace.userservice.model.dto.UserDto;

public interface AuthService {
    UserDto login(LoginDto loginDto);
    String register(RegisterDto registerDto) throws Exception;
}
