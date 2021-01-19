package com.app.userrestapi.service;

import com.app.userrestapi.model.dto.UserDto;
import com.app.userrestapi.model.dto.UserWriteDto;
import com.app.userrestapi.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UserMapper {
    public static UserDto map(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public static User map(UserWriteDto user) {
        return User.builder().firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).build();
    }
}
