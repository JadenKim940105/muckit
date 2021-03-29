package me.summerbell.muckit.likeit;

import lombok.RequiredArgsConstructor;
import me.summerbell.muckit.accounts.auth.CurrentUser;
import me.summerbell.muckit.domain.Account;
import me.summerbell.muckit.domain.LikeIt;
import me.summerbell.muckit.kakaosearchapi.dto.RestaurantDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class LikeItController {

    private final LikeItService likeItService;


    @PostMapping("/api/likeIt")
    public ResponseEntity enrollmentLikeIt(@CurrentUser Account account, @RequestBody RestaurantDto restaurant){

        LikeIt likeIt = likeItService.addLikeIt(account, restaurant);
        URI createdUri = linkTo(methodOn(LikeItController.class).enrollmentLikeIt(account, restaurant)).slash(likeIt.getId()).toUri();

        return ResponseEntity.created(createdUri).build();
    }
}
