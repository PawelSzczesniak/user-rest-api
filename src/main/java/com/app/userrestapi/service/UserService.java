package com.app.userrestapi.service;

import com.app.userrestapi.exception.ApiException;
import com.app.userrestapi.model.dto.UserDto;
import com.app.userrestapi.model.dto.UserWriteDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll() throws ApiException;
    UserDto findById(Long id) throws ApiException;
    UserDto add(UserWriteDto userWriteDto) throws ApiException;
    UserDto update(Long id, UserWriteDto userWriteDto) throws ApiException;
    void deleteById(Long id) throws ApiException;
}
