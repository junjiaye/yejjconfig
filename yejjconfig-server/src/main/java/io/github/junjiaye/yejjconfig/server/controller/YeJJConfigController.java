package io.github.junjiaye.yejjconfig.server.controller;

import io.github.junjiaye.yejjconfig.server.DistributedLocks;
import io.github.junjiaye.yejjconfig.server.dal.ConfigsMapper;
import io.github.junjiaye.yejjconfig.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: yejjconfig-server
 * @ClassName: YeJJConfigController
 * @description:
 * @author: yejj
 * @create: 2024-05-10 07:33
 */
@RestController
public class YeJJConfigController {

    @Autowired
    ConfigsMapper configsMapper;

    @Autowired
    DistributedLocks distributedLocks;

    Map<String,Long> VERSIONS = new HashMap<>();
    @GetMapping("/list")
    public List<Configs> list(String app, String env, String ns){
        return configsMapper.list(app,env,ns);
    }

    @PostMapping("/update")
    public List<Configs> update(@RequestParam("app") String app,
                                @RequestParam("env") String env,
                                @RequestParam("ns") String ns,
                                @RequestBody Map<String,String> params){
        params.forEach((k,v)->{
            insertOrUpdate(new Configs(app,env,ns,k,v));
        });
        VERSIONS.put(app + "-" + env + "-" + ns,System.currentTimeMillis());
        return configsMapper.list(app,env,ns);
    }

    private void insertOrUpdate(Configs configs) {
        Configs conf = configsMapper.select(configs.getApp(),configs.getEnv(),configs.getNs(),configs.getPkey());
        if (conf == null){
            configsMapper.insert(configs);
        }else {
            configsMapper.update(configs);
        }
    }

    @GetMapping("/version")
    public long version(String app, String env, String ns){
        return VERSIONS.getOrDefault(app + "-" + env + "-" + ns,0L);
    }

    @GetMapping("/status")
    public boolean status(String app, String env, String ns){
        return distributedLocks.getLocked().get();
    }

}
