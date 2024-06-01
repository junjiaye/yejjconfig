package io.github.junjiaye.yejjconfig.client.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.EnumerablePropertySource;

/**
 * @program: yejjconfig-client
 * @ClassName: YeJJPropertySource
 * @description:
 * @author: yejj
 * @create: 2024-05-25 08:44
 */
public class YeJJPropertySource extends EnumerablePropertySource<YeJJConfigService> {

    public YeJJPropertySource(String name, YeJJConfigService source) {
        super(name, source);
    }

    @NotNull
    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }
    @Override
    public Object getProperty(@NotNull String name) {
        return source.getProperty(name);
    }
}
