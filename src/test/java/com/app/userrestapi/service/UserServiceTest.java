package com.app.userrestapi.service;

import com.app.userrestapi.exception.ApiException;
import com.app.userrestapi.exception.ResourceNotFoundException;
import com.app.userrestapi.model.dto.UserDto;
import com.app.userrestapi.model.dto.UserWriteDto;
import com.app.userrestapi.model.entity.User;
import com.app.userrestapi.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class UserServiceTest {
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    UserService userService = new UserServiceImpl(userRepository);

    @Test
    void shouldFindAll() throws ApiException {
        when(userRepository.findAll()).thenReturn(List.of(new User(1L, "Pawel", "Szczesniak", "paw@wp.pl")));

        final List<UserDto> all = userService.findAll();
        Assertions.assertThat(all).isNotNull();
        Assertions.assertThat(all.isEmpty()).isFalse();
    }

    @Test
    void shouldRepositoryFindByIdCallOnceInFindAll() throws ApiException {
        when(userRepository.findAll()).thenReturn(List.of(new User(1L, "Pawel", "Szczesniak", "paw@wp.pl")));

        final List<UserDto> all = userService.findAll();

        Mockito.verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldFindById() throws ApiException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(1L, "Pawel", "Szczesniak", "paw@wp.pl")));

        final UserDto byId = userService.findById(1L);

        Assertions.assertThat(byId).isNotNull();
        Assertions.assertThat(byId.getFirstName()).isEqualTo("Pawel");
        Assertions.assertThat(byId.getLastName()).isEqualTo("Szczesniak");
        Assertions.assertThat(byId.getEmail()).isEqualTo("paw@wp.pl");
    }

    @Test
    void shouldThrowResourceNotFoundInFindById() {
        Long id = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> userService.findById(id))
                .withMessage(String.format("No user with id: %s", id));
    }

    @Test
    void shouldThrowResourceNotFoundInUpdate() {
        Long id = 1L;
        final UserWriteDto userWriteDto = new UserWriteDto("Marek", "NOwak", "marnowak@wp.pl");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> userService.update(id, userWriteDto))
                .withMessage(String.format("No user with id: %s", id));
    }

    @Test
    void shouldUpdate() throws ApiException {
        Long id = 1L;
        final UserWriteDto userWriteDto = new UserWriteDto("Marek", "NOwak", "marnowak@wp.pl");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(1L, "Pawel", "Szczesniak", "paw@wp.pl")));
        final UserDto updated = userService.update(id, userWriteDto);

        Assertions.assertThat(updated.getFirstName()).isEqualTo("Marek");
        Assertions.assertThat(updated.getLastName()).isEqualTo("NOwak");
        Assertions.assertThat(updated.getEmail()).isEqualTo("marnowak@wp.pl");
    }

    @Test
    void shouldAdd() throws ApiException {
        Long id = 1L;
        final UserWriteDto userWriteDto = new UserWriteDto("Marek", "NOwak", "marnowak@wp.pl");

        when(userRepository.save(any())).thenReturn(new User(id, "Marek", "NOwak", "marnowak@wp.pl"));
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        final UserDto added = userService.add(userWriteDto);

        Assertions.assertThat(added.getId()).isEqualTo(id);
        Assertions.assertThat(added.getFirstName()).isEqualTo("Marek");
        Assertions.assertThat(added.getLastName()).isEqualTo("NOwak");
        Assertions.assertThat(added.getEmail()).isEqualTo("marnowak@wp.pl");
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenUserExists() {
        Long id = 1L;
        final UserWriteDto userWriteDto = new UserWriteDto("Marek", "NOwak", "marnowak@wp.pl");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User(id, "Marek", "NOwak", "marnowak@wp.pl")));

        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.add(userWriteDto))
                .withMessage(String.format("User with email %s exists", userWriteDto.getEmail()));
    }
}
