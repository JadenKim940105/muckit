package me.summerbell.muckit.kakaosearchapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class KakaoSearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("로그인한 사용자가 주변 음식점 정보 가져오기")
    void findBestRestaurant() throws Exception{
        mockMvc.perform(get("/find-best-restaurant")
        .accept(MediaTypes.HAL_JSON_VALUE+";charset=UTF-8")
        .param("longitude", "127.027610")
        .param("latitude", "37.498095"))
                .andDo(print())
                .andExpect(jsonPath("$[0].address_name").exists())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].phone").exists())
                .andExpect(jsonPath("$[0].place_name").exists())
                .andExpect(jsonPath("$[0].place_url").exists())
                .andExpect(jsonPath("$[0].road_address_name").exists())
                .andExpect(jsonPath("$[0].longitude").exists())
                .andExpect(jsonPath("$[0].latitude").exists());
    }

}