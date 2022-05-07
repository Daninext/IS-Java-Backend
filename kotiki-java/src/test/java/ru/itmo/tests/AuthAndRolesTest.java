package ru.itmo.tests;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.itmo.data.dao.*;
import ru.itmo.data.entity.*;
import ru.itmo.services.serv.*;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebAuxTestConfig.class
)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class AuthAndRolesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("art3m")
    void getCatsByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cats")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("110")));
    }

    @Test
    @WithUserDetails("admin")
    void getCatsByAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cats")
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("110")));
    }

    @Test
    @WithUserDetails("art3m")
    void getOwnersByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/owners")
                        .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }
}