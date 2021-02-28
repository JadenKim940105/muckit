package me.summerbell.muckit.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class ReviewDto {

    private String nickName;

    private String reviewContent;

}
