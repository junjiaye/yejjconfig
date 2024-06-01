package io.github.junjiaye.yejjconfig.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: yejjconfig-server
 * @ClassName: Configs
 * @description:
 * @author: yejj
 * @create: 2024-05-10 07:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configs {
    private String app;
    private String env;
    private String ns;
    private String pkey;
    private String pval;
}
