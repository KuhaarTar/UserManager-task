package com.kuhar.usermanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuhar.usermanager.models.User;
import com.kuhar.usermanager.models.UserUpdateRequest;
import com.kuhar.usermanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = new User("user1@gmail.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main Street", "555-123-4567");
        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        userService.saveUser(new User("user1@gmail.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main Street", "555-123-4567"));
        Long userId = 1L;
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete("/users/{userId}", userId));
        result.andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    public void testUpdateFirstAndLastName() throws Exception {
        Long userId = 1L;
        UserUpdateRequest updateRequest = new UserUpdateRequest("NewJohn", "NewDoe");
        User updatedUser = new User("user1@gmail.com", "NewJohn", "NewDoe",
                LocalDate.of(1990, 5, 15), "123 Main Street", "555-123-4567");
        when(userService.updateFirstAndLastName(userId, updateRequest)).thenReturn(updatedUser);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .patch("/users/{userId}/update-name", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("NewJohn"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("NewDoe"));

        verify(userService, times(1)).updateFirstAndLastName(userId, updateRequest);
    }

    @Test
    public void testUpdateUser() throws Exception {
        Long userId = 1L;
        User user = new User("user1@gmail.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main Street", "555-123-4567");
        when(userService.updateUser(userId, user)).thenReturn(user);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);
        User user1 = new User("user1@gmail.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main Street", "555-123-4567");
        User user2 = new User("user2@example.com", "Jane", "Smith",
                LocalDate.of(1993, 8, 25), "456 Elm Avenue", "555-987-6543");
        List<User> usersInRange = Arrays.asList(user1, user2);

        when(userService.searchUsersByBirthDateRange(startDate, endDate)).thenReturn(usersInRange);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/users/search")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("user1@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("user2@example.com"));

        verify(userService, times(1)).searchUsersByBirthDateRange(startDate, endDate);
    }
}
