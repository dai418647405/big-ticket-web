package org.daijing.big.ticket.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by daijing03 on 17/5/2.
 */
public interface AvatarDataApiService {

    String JOKE_APP_TOKEN = "ad8cbd49c83b4d9183761cd4d5d025ec";

    String JOKE_HOST = "http://api.avatardata.cn";

    int CONNECT_TIMEOUT = 60 * 1000;

    int READ_TIMEOUT = 60 * 1000;

    JSONObject getLatestJokeImg(Integer page, Integer rows);
}
