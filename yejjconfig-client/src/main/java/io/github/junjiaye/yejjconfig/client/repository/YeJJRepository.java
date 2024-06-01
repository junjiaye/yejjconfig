package io.github.junjiaye.yejjconfig.client.repository;

import io.github.junjiaye.yejjconfig.client.config.ConfigMeta;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @program: yejjconfig
 * @ClassName: YeJJRepository
 * @description: \
 * @author: yejj
 * @create: 2024-05-25 10:38
 */
public interface YeJJRepository {

    static YeJJRepository getDefault( ConfigMeta configMeta) {
        return new YeJJRepositoryImpl(configMeta);
    }
    void addListener(YeJJRepositoryChangeListner listener);

    Map<String,String> getConfig();
}
