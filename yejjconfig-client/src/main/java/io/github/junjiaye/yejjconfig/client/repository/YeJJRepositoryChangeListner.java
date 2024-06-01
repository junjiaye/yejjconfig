package io.github.junjiaye.yejjconfig.client.repository;

import io.github.junjiaye.yejjconfig.client.config.ConfigMeta;

import java.util.Map;

/**
 * @program: yejjconfig
 * @ClassName: YeJJRepositoryChangeListner
 * @description:
 * @author: yejj
 * @create: 2024-05-25 18:26
 */
public interface YeJJRepositoryChangeListner {
    void onChange(YeJJRepositoryChangeEvent event);

}
