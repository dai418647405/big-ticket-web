package org.daijing.big.ticket.utils;

import com.dianping.squirrel.client.StoreKey;

public interface StoreCallBack<T> {
    T getResult(StoreKey missKey);
}