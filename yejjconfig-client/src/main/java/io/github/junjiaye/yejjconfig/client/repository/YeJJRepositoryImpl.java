package io.github.junjiaye.yejjconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.github.junjiaye.yejjconfig.client.config.ConfigMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @program: yejjconfig
 * @ClassName: YeJJRepositoryImpl
 * @description:
 * @author: yejj
 * @create: 2024-05-25 10:39
 */
public class YeJJRepositoryImpl implements YeJJRepository{

    ConfigMeta configMeta;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    Map<String,Map<String, String>> configsMap = new HashMap<>();
    Map<String,Long> versionMap = new HashMap<>();


    List<YeJJRepositoryChangeListner> listeners = new ArrayList<>();

    public YeJJRepositoryImpl(ConfigMeta configMeta) {
        this.configMeta = configMeta;
        executor.scheduleAtFixedRate(this::heartBeat, 1_000, 5_000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void addListener(YeJJRepositoryChangeListner listener){
        listeners.add(listener);
    }

    @Override
    public Map<String, String> getConfig() {
        //使用缓存
        String key = configMeta.genKey();
        if(configsMap.containsKey(key)){
            return configsMap.get(key);
        }
        return findAll();
    }

    private  Map<String, String> findAll() {
        String listPath = configMeta.listPath();
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>(){});
        Map<String,String> result = new HashMap<>();
        configs.forEach(config -> result.put(config.getPkey(),config.getPval()));
        return result;
    }

    private void heartBeat(){
        try {
            System.out.println("scheduleAtFixedRate ==============>");
            String versionPath = configMeta.versionPath();
            Long version = HttpUtils.httpGet(versionPath, new TypeReference<Long>(){});
            String key = configMeta.genKey();
            Long oldVersion = versionMap.getOrDefault(key,-1L);
            //版本有更新，需要更新配置
            if (version > oldVersion){
                System.out.println("[YeJJConfig] curent version :" + version + "oldversion:" + oldVersion);
                versionMap.put(key, version);
                Map<String, String> newConfigs = findAll();
                configsMap.put(key, newConfigs);
                listeners.forEach(listener -> listener.onChange(new YeJJRepositoryChangeEvent(configMeta,newConfigs)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
