package io.github.junjiaye.yejjconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: yejjconfig
 * @ClassName: ConfigMeta
 * @description:
 * @author: yejj
 * @create: 2024-05-25 10:52
 */
@Data
@AllArgsConstructor
public class ConfigMeta {
    private String app;
    private String env;
    private String ns;
    private String configServer;

    public String genKey(){
        return this.app + "_" + this.env + "_" + this.ns;
    }

    public String listPath(){
        return this.getConfigServer() + "/list?app=" + this.getApp() + "&env=" + this.getEnv() + "&ns=" + this.getNs();
    }

    public String versionPath(){
        return this.getConfigServer() + "/version?app=" + this.getApp() + "&env=" + this.getEnv() + "&ns=" + this.getNs();
    }


}
