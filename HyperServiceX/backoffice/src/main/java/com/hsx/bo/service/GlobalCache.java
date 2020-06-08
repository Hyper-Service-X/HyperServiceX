package com.hsx.bo.service;

import com.hsx.common.model.error.Status;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalCache {
    public static ConcurrentHashMap<String, CompletableFuture<Status>> boRequestMap = new ConcurrentHashMap<>();
}
