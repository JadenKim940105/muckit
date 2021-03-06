package me.summerbell.muckit.kakaosearchapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class RestaurantDto {
    private String address_name;

    private String kakao_id;

    private String phone;

    private String place_name;

    private String place_url;

    private String road_address_name;

    private String longitude;

    private String latitude;
}
