package me.summerbell.muckit.utils.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class RestaurantReviewVo {
    private String address_name;

    private String kakao_id;

    private String phone;

    private String place_name;

    private String place_url;

    private String road_address_name;

    private String longitude;

    private String latitude;

    private String reviewContent;
}
