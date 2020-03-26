package com.li.travel.dao;

import com.li.travel.domain.Favorite;

public interface FavoriteDao {
    public  int findCountByRid(int rid);

    public Favorite isFlag(int uid, int rid);

    public void addFavorite(int rid,int uid);
}
