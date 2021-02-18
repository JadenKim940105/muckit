package me.summerbell.muckit.domain;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Restaurant {
    private String address_name;
    private String id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private String longitude; // longitude
    private String latitude; // latitude
}
