package com.app.userrestapi.controller;

import com.app.userrestapi.exception.ApiException;
import com.app.userrestapi.model.dto.UserDto;
import com.app.userrestapi.model.dto.UserWriteDto;
import com.app.userrestapi.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class UserControllerConfig {

    @Bean
    UserService userService() {
        return new UserService() {
            @Override
            public List<UserDto> findAll() throws ApiException {
                return null;
            }

            @Override
            public UserDto findById(Long id) throws ApiException {
                return null;
            }

            @Override
            public UserDto add(UserWriteDto userWriteDto) throws ApiException {
                return null;
            }

            @Override
            public UserDto update(Long id, UserWriteDto userWriteDto) throws ApiException {
                return null;
            }

            @Override
            public void deleteById(Long id) throws ApiException {

            }
        };
    }

    @Bean
    UserController userController(UserService userService) {
        return new UserController(userService);
    }
}
