package me.summerbell.muckit.reviews;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.summerbell.muckit.accounts.AccountRepository;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.Restaurant;
import me.summerbell.muckit.domain.Review;
import me.summerbell.muckit.kakaosearchapi.dto.RestaurantDto;
import me.summerbell.muckit.restaurants.RestaurantRepository;
import me.summerbell.muckit.utils.vo.RestaurantReviewVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebApplicationContext ctx;



    @BeforeEach
    void beforeEach(TestInfo testInfo){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        Account account = Account.builder()
                .accountId("jaden")
                .email("jaden@email.com")
                .password("123123123")
                .nickName("jaden")
                .build();

        Account savedAccount = accountRepository.save(account);


        Restaurant restaurant = new Restaurant();
        restaurant.setAddressName("서울 강남구 역삼동 824-29");
        restaurant.setKakaoId("1863534623");
        restaurant.setPhone("02-6014-1245");
        restaurant.setPlaceName("보슬보슬");
        restaurant.setPlaceUrl("http://place.map.kakao.com/1863534623");
        restaurant.setRoadAddressName("서울 강남구 테헤란로8길 22");
        restaurant.setLongitude("127.0316074527639");
        restaurant.setLatitude("37.49739976085793");

        if(testInfo.getDisplayName().equals("최초리뷰 생성하기")){
            return;
        }

        Review review1 = new Review();
        review1.setAccount(savedAccount);
        review1.setReviewContent("이집 맛있네요~");
        review1.setRestaurant(restaurant);


        restaurant.addReview(review1);

        restaurantRepository.save(restaurant);
    }
/*
    @DisplayName("입력받은 레스토랑의 리뷰가 존재하는 경우")
    @Test
    @WithMockUser
    void getRestaurantReview() throws Exception {

        RestaurantDto restaurantDto = RestaurantDto.builder()
                .address_name("서울 강남구 역삼동 824-29")
                .kakao_id("1863534623")
                .phone("02-6014-1245")
                .place_url("보슬보슬")
                .place_url("http://place.map.kakao.com/1863534623")
                .road_address_name("서울 강남구 테헤란로8길 22")
                .longitude("127.0316074527639")
                .latitude("37.49739976085793")
                .build();



        mockMvc.perform(get("/api/restaurant-review")
                .contentType(MediaType.APPLICATION_JSON+";charset=UTF-8")
                .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("입력받은 레스토랑의 리뷰가 없는 경우")
    @Test
    @WithMockUser
    void getRestaurantReview_WRONG_INPUT() throws Exception {

        RestaurantDto restaurantDto = RestaurantDto.builder()
                .address_name("서울 강남구 역삼동 824-29")
                .kakao_id("000")
                .phone("02-6014-1245")
                .place_url("보슬보슬")
                .place_url("http://place.map.kakao.com/1863534623")
                .road_address_name("서울 강남구 테헤란로8길 22")
                .longitude("127.0316074527639")
                .latitude("37.49739976085793")
                .build();



        mockMvc.perform(get("/api/restaurant-review")
                .contentType(MediaType.APPLICATION_JSON+";charset=UTF-8")
                .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("최초리뷰 생성하기")
    @Test
    void create_first_review() throws Exception{
        RestaurantReviewVo restaurantReviewVo = RestaurantReviewVo.builder()
                .address_name("서울 강남구 역삼동 824-29")
                .kakao_id("000")
                .phone("02-6014-1245")
                .place_url("보슬보슬")
                .place_url("http://place.map.kakao.com/1863534623")
                .road_address_name("서울 강남구 테헤란로8길 22")
                .longitude("127.0316074527639")
                .latitude("37.49739976085793")
                .reviewContent("첫 리뷰!!!")
                .build();
        mockMvc.perform(post("/api/restaurant-review")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(restaurantReviewVo)))
                .andExpect(status().isOk());
    }
*/







}