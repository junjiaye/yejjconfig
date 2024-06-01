package io.github.junjiaye.yejjconfig.client.repository;

import io.github.junjiaye.yejjconfig.client.config.ConfigMeta;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @program: yejjconfig
 * @ClassName: YeJJRepositoryChangeEvent
 * @description:
 * @author: yejj
 * @create: 2024-05-25 19:24
 */
@Data
@AllArgsConstructor
public class YeJJRepositoryChangeEvent {
    ConfigMeta configMeta ;
    Map<String,String> config ;
}
