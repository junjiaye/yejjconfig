package io.github.junjiaye.yejjconfig.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: yejjconfig-demo
 * @ClassName: YeJJConfig
 * @description:
 * @author: yejj
 * @create: 2024-05-24 07:34
 */
@ConfigurationProperties(prefix = "yejj")
@Data
public class YeJJDemoConfig {
    String a;
    String b;
}
