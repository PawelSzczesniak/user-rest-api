package com.app.userrestapi.service;

import com.app.userrestapi.exception.ApiException;
import com.app.userrestapi.exception.ResourceNotFoundException;
import com.app.userrestapi.model.dto.UserDto;
import com.app.userrestapi.model.dto.UserWriteDto;
import com.app.userrestapi.model.entity.User;
import com.app.userrestapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll() throws ApiException {
        try {
            return userRepository.findAll().stream().map(UserMapper::map).collect(Collectors.toList());
        } catch (Exception e) {
            String message = "Cannot find all users";
            log.error(message + "-" + e.getMessage());
            throw new ApiException(message, e);
        }
    }

    @Override
    public UserDto findById(Long id) throws ApiException {
        try {
            return userRepository.findById(id).map(UserMapper::map).orElseThrow(() -> new ResourceNotFoundException("No user with id: " + id));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            String message = "Cannot find user with id: " + id;
            log.error(message + "-" + e.getMessage());
            throw new ApiException(message, e);
        }
    }

    @Override
    @Transactional
    public UserDto add(UserWriteDto userWriteDto) throws ApiException {
        try {
            userRepository.findByEmail(userWriteDto.getEmail()).ifPresent(user -> {
                throw new IllegalStateException(String.format("User with email %s exists", user.getEmail()));
            });
            return UserMapper.map(
                    userRepository.save(UserMapper.map(userWriteDto))
            );
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            String message = "Cannot add new user with data: " + userWriteDto.toString();
            log.error(message + "-" + e.getMessage());
            throw new ApiException(message, e);
        }
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserWriteDto userWriteDto) throws ApiException {
        try {
            final User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user with id: " + id));
            user.setFirstName(userWriteDto.getFirstName());
            user.setLastName(userWriteDto.getLastName());
            user.setEmail(userWriteDto.getEmail());

            return UserMapper.map(user);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            String message = String.format("Cannot update user with id: %s and data: %s", id, userWriteDto.toString());
            log.error(message + "-" + e.getMessage());
            throw new ApiException(message, e);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws ApiException {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            String message = "Cannot delete user with id: " + id;
            log.error(message + "-" + e.getMessage());
            throw new ApiException(message, e);
        }
    }
}
