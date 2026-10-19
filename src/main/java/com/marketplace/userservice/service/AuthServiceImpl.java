package com.marketplace.userservice.service;

import com.marketplace.userservice.interfaces.AuthService;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.model.dto.LoginDto;
import com.marketplace.userservice.model.dto.RegisterDto;
import com.marketplace.userservice.model.dto.UserDto;
import com.marketplace.userservice.model.enums.Role;
import com.marketplace.userservice.repository.UserRepository;
import com.marketplace.userservice.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) throws Exception {

        // check username is already exists in database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new Exception("Username already exists!");
        }

        // check email is already exists in database
        if(userRepository.findByEmail(registerDto.getEmail()).isPresent()){
            throw new Exception("Email is already exists!.");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(Role.valueOf(registerDto.getRole()));
        userRepository.save(user);

        return "User Registered Successfully!.";
    }
}