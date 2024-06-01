package io.github.junjiaye.yejjconfig.client.config;

import io.github.junjiaye.yejjconfig.client.repository.YeJJRepositoryChangeListner;
import org.springframework.context.ApplicationContext;
import io.github.junjiaye.yejjconfig.client.repository.YeJJRepository;


/**
 * @program: yejjconfig-client
 * @ClassName: YeJJConfigService
 * @description:
 * @author: yejj
 * @create: 2024-05-25 08:45
 */
public interface YeJJConfigService extends YeJJRepositoryChangeListner {

    static YeJJConfigService getDefault(ApplicationContext applicationContext, ConfigMeta configMeta){
        YeJJRepository repo = YeJJRepository.getDefault(configMeta);
        YeJJConfigService configService = new YeJJConfigServiceImpl(applicationContext, repo.getConfig());
        repo.addListener(configService);
        return configService;
    }
    String[] getPropertyNames() ;

    String getProperty(String name);
}
