package com.app.userrestapi.controller;

import com.app.userrestapi.model.dto.UserWriteDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = UserControllerConfig.class)
class UserControllerTest {
    private final String USERS_URI = "/users/";
    private final String USERS_URI_WITH_ID = "/users/1";

    @Test
    void shouldReturnCreatedInAdd(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post(USERS_URI).
                content(validBody()).
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsNotValidInAdd(@Autowired MockMvc mockMvc) throws Exception {
        performPostRequestTestWithBadRequest(mockMvc, bodyWithNotValidEmail());
    }

    @Test
    void shouldReturnBadRequestWhenFirstNameAndLastNameIsNullInAdd(@Autowired MockMvc mockMvc) throws Exception {
        performPostRequestTestWithBadRequest(mockMvc, bodyWithNullLasName());
        performPostRequestTestWithBadRequest(mockMvc, bodyWithNullFirstName());
        performPostRequestTestWithBadRequest(mockMvc, bodyWithNullFirstNameAndLastName());
    }

    @Test
    void shouldReturnCreatedInUpdate(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(put(USERS_URI_WITH_ID).
                content(validBody()).
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotSupportedWhenIdIsNullInUpdate(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(put(USERS_URI).
                content(validBody()).
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsNotValidInUpdate(@Autowired MockMvc mockMvc) throws Exception {
        performPutRequestTestWithBadRequest(mockMvc, bodyWithNotValidEmail());
    }

    @Test
    void shouldReturnBadRequestWhenFirstNameAndLastNameIsNullInUpdate(@Autowired MockMvc mockMvc) throws Exception {
        performPutRequestTestWithBadRequest(mockMvc, bodyWithNullLasName());
        performPutRequestTestWithBadRequest(mockMvc, bodyWithNullFirstName());
        performPutRequestTestWithBadRequest(mockMvc, bodyWithNullFirstNameAndLastName());
    }

    @Test
    void shouldReturnNoContentInDelete(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(delete(USERS_URI_WITH_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotSupportedWhenIdIsNullInDelete(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(delete(USERS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturnOkInFindAll(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get(USERS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOkInFindById(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(get(USERS_URI_WITH_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void performPostRequestTestWithBadRequest(MockMvc mockMvc, String userData) throws Exception {
        mockMvc.perform(post(USERS_URI)
                .content(userData)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void performPutRequestTestWithBadRequest(MockMvc mockMvc, String userData) throws Exception {
        mockMvc.perform(put(USERS_URI_WITH_ID)
                .content(userData)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String validBody() {
        return new Gson().toJson(new UserWriteDto("firstName", "lastName", "email@gmail.com"));
    }

    private String bodyWithNotValidEmail() {
        return new Gson().toJson(new UserWriteDto("firstName", "lastName", "notValidEmail"));
    }

    private String bodyWithNullLasName() {
        return new Gson().toJson(new UserWriteDto("firstName", null, "notValidEmail"));
    }

    private String bodyWithNullFirstName() {
        return new Gson().toJson(new UserWriteDto(null, "lastName", "notValidEmail"));
    }

    private String bodyWithNullFirstNameAndLastName() {
        return new Gson().toJson(new UserWriteDto(null, null, "notValidEmail"));
    }
}
