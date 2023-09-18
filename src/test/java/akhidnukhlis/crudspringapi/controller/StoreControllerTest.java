package akhidnukhlis.crudspringapi.controller;

import akhidnukhlis.crudspringapi.entity.Store;
import akhidnukhlis.crudspringapi.entity.User;
import akhidnukhlis.crudspringapi.model.*;
import akhidnukhlis.crudspringapi.repository.StoreRepository;
import akhidnukhlis.crudspringapi.repository.UserRepository;
import akhidnukhlis.crudspringapi.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        storeRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
        userRepository.save(user);
    }

    @Test
    void createStoreBadRequest() throws Exception {
        CreateStoreRequest request = new CreateStoreRequest();
        request.setStoreName("");

        mockMvc.perform(
                post("/api/stores")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void createStoreSuccess() throws Exception {
        CreateStoreRequest request = new CreateStoreRequest();
        request.setStoreName("Toko Kue");
        request.setPhone("0912345678");
        request.setEmail("toko@kue.com");
        request.setStreet("Jln A");
        request.setCity("Yogyakarta");
        request.setState("Indonesia");
        request.setZipCode("55555");

        mockMvc.perform(
                post("/api/stores")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<StoreResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(request.getStoreName(), response.getData().getStoreName());
            assertEquals(request.getPhone(), response.getData().getPhone());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getState(), response.getData().getState());
            assertEquals(request.getZipCode(), response.getData().getZipCode());

            assertTrue(storeRepository.existsById(response.getData().getId()));
        });
    }
    @Test
    void getStoreNotFound() throws Exception {
        mockMvc.perform(
                get("/api/stores/2134-4242")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getStoreSuccess() throws Exception {
        User user = userRepository.findById("test").orElse(null);

        Store store = new Store();
        store.setId(UUID.randomUUID().toString());
        store.setStoreName("toko-buah");
        store.setPhone("0912345678");
        store.setEmail("toko@kue.com");
        store.setStreet("Jln B");
        store.setCity("Yogyakarta");
        store.setState("Indonesia");
        store.setZipCode("55555");
        store.setUser(user);

        storeRepository.save(store);

        mockMvc.perform(
                get("/api/stores/" + store.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<StoreResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());

            assertEquals(store.getId(), response.getData().getId());
            assertEquals(store.getStoreName(), response.getData().getStoreName());
            assertEquals(store.getPhone(), response.getData().getPhone());
            assertEquals(store.getEmail(), response.getData().getEmail());
            assertEquals(store.getStreet(), response.getData().getStreet());
            assertEquals(store.getCity(), response.getData().getCity());
            assertEquals(store.getState(), response.getData().getState());
            assertEquals(store.getZipCode(), response.getData().getZipCode());
        });
    }
}
