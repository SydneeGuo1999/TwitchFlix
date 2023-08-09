package com.sydnee.twitch.favorite;

import com.sydnee.twitch.db.FavoriteRecordRepository;
import com.sydnee.twitch.db.ItemRepository;
import com.sydnee.twitch.db.entity.FavoriteRecordEntity;
import com.sydnee.twitch.db.entity.ItemEntity;
import com.sydnee.twitch.db.entity.UserEntity;
import com.sydnee.twitch.model.TypeGroupedItemList;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class FavoriteService {
    private final ItemRepository itemRepository;
    private final FavoriteRecordRepository favoriteRecordRepository;

    public FavoriteService(ItemRepository itemRepository, FavoriteRecordRepository favoriteRecordRepository) {
        this.itemRepository = itemRepository;
        this.favoriteRecordRepository = favoriteRecordRepository;
    }

    @Transactional
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    public void setFavoriteItem(UserEntity user, ItemEntity item){
        ItemEntity persistedItem = itemRepository.findByTwitchId(item.twitchId());
        if (persistedItem == null) {
            persistedItem = itemRepository.save(item);
        }
        FavoriteRecordEntity favoriteRecord =
                new FavoriteRecordEntity(null, user.id(), persistedItem.id(), Instant.now());
        favoriteRecordRepository.save(favoriteRecord);
    }
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    public void unsetFavoriteItem(UserEntity user, String twitchId) {
        ItemEntity item = itemRepository.findByTwitchId(twitchId);
        if (item != null) {
            favoriteRecordRepository.delete(user.id(), item.id());
        }
    }

    public List<ItemEntity> getFavoriteItems(UserEntity user) {
        List<Long> favoriteItemIds = favoriteRecordRepository.findFavoriteItemIdsByUserId(user.id());
        return  itemRepository.findAllById(favoriteItemIds);
    }

    public TypeGroupedItemList getGroupedFavoriteItems(UserEntity user) {
        List<ItemEntity> items = getFavoriteItems(user);
        return new TypeGroupedItemList(items);
    }
}
