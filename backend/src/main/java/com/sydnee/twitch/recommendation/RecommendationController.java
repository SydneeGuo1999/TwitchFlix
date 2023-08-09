package com.sydnee.twitch.recommendation;




import com.sydnee.twitch.db.entity.UserEntity;
import com.sydnee.twitch.model.TypeGroupedItemList;
import com.sydnee.twitch.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RecommendationController {


    private final RecommendationService recommendationService;
    private final UserService userService;


    public RecommendationController(RecommendationService recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }


    @GetMapping("/recommendation")
    TypeGroupedItemList getRecommendation(@AuthenticationPrincipal User user) {
        UserEntity userEntity;
        if (user == null) {
            userEntity = null;
        } else {
            userEntity = userService.findByUsername(user.getUsername());
        }
        return recommendationService.recommendItems(userEntity);
    }
}
