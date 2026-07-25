package com.dineflow.service;

import com.dineflow.dto.AuthResponseDTO;
import com.dineflow.dto.LoginRequestDTO;
import com.dineflow.dto.RegisterRequestDTO;

public interface AuthService {

    String register(RegisterRequestDTO request);


    AuthResponseDTO login(LoginRequestDTO request);
}
