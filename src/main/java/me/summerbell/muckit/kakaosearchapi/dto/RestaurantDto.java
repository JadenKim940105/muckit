package me.summerbell.muckit.kakaosearchapi.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class RestaurantDto {
    private String address_name;
    private String id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private String longitude; // longitude
    private String latitude; // latitude
}
