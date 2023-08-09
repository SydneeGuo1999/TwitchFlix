package com.sydnee.twitch.model;

import com.sydnee.twitch.db.entity.ItemEntity;

public record FavoriteRequestBody(
        ItemEntity favorite
) {
}
