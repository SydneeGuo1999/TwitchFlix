package com.sydnee.twitch.item;

import com.sydnee.twitch.external.TwitchService;
import com.sydnee.twitch.external.model.Clip;
import com.sydnee.twitch.external.model.Stream;
import com.sydnee.twitch.external.model.Video;
import com.sydnee.twitch.model.TypeGroupedItemList;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private static final int SEARCH_RESULT_SIZE = 20;
    private final TwitchService twitchService;

    public ItemService(TwitchService twitchService) {
        this.twitchService = twitchService;
    }

    @Cacheable("items")
    public TypeGroupedItemList getItems(String gameId) {
        List<Video> videos = twitchService.getVideos(gameId, SEARCH_RESULT_SIZE);
        List<Clip> clips = twitchService.getClips(gameId, SEARCH_RESULT_SIZE);
        List<Stream> streams = twitchService.getStreams(List.of(gameId), SEARCH_RESULT_SIZE);
        return new TypeGroupedItemList(gameId, streams, videos, clips);
    }
}
