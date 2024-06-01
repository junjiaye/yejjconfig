package io.github.junjiaye.yejjconfig.client.config;

import io.github.junjiaye.yejjconfig.client.repository.YeJJRepositoryChangeEvent;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: yejjconfig-client
 * @ClassName: YeJJConfigServiceImpl
 * @description:
 * @author: yejj
 * @create: 2024-05-25 08:49
 */
public class YeJJConfigServiceImpl implements YeJJConfigService {

    Map<String,String> config;
    ApplicationContext applicationContext;

    public YeJJConfigServiceImpl(ApplicationContext applicationContext,Map<String,String> config){
        this.applicationContext = applicationContext;
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }


    @Override
    public void onChange(YeJJRepositoryChangeEvent event) {
        //取出变化的数据，然后再进行更新
        Set<String> keys = calcChangeKeys(this.config,event.getConfig());
        this.config = event.getConfig();
        //触发EnvironmentChangeEvent事件，刷新配置
        if (this.config != null){
            //springcloud中，刷新ConfigurationProperties的方法：SpringCloud中ConfigurationPropertiesRebinder订阅了EnvironmentChangeEvent事件，
            // 当有EnvironmentChangeEvent事件发生变化时，会rebind ConfigurationProperties的值
            applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
        }
    }

    private Set<String> calcChangeKeys(Map<String, String> oldConfig, Map<String, String> newConfig) {
        if (oldConfig.isEmpty()) return newConfig.keySet();
        if (newConfig.isEmpty()) return oldConfig.keySet();
        Set<String> collect = newConfig.keySet().stream().filter(key -> !newConfig.get(key).equals(oldConfig.get(key))).collect(Collectors.toSet());
        oldConfig.keySet().stream().filter(key -> !newConfig.containsKey(key)).forEach(collect::add);
        return collect;
    }

}
