package com.app.userrestapi.controller;

import com.app.userrestapi.exception.ApiException;
import com.app.userrestapi.model.dto.UserDto;
import com.app.userrestapi.model.dto.UserWriteDto;
import com.app.userrestapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/")
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    public UserDto findById(@PathVariable Long id) throws ApiException {
        return userService.findById(id);
    }

    @GetMapping
    public List<UserDto> findAll() throws ApiException {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody UserWriteDto userWriteDto) throws ApiException {
        return userService.add(userWriteDto);
    }

    @PutMapping("{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody UserWriteDto userWriteDto) throws ApiException {
        return userService.update(id, userWriteDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Long id) throws ApiException {
        userService.deleteById(id);
    }

}
