package io.github.junjiaye.yejjconfig.client.config;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: yejjconfig-client
 * @ClassName: PropertySourcesProcessor
 * @description:
 * @author: yejj
 * @create: 2024-05-25 08:53
 */
@Data
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware, ApplicationContextAware, PriorityOrdered {

    private final static String YEJJ_PROPERTY_SOURCES = "YeJJPropertySources";
    private final static String YEJJ_PROPERTY_SOURCE = "YeJJPropertySource";

    ApplicationContext applicationContext;

    Environment environment;

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
        if (configurableEnvironment.getPropertySources().contains(YEJJ_PROPERTY_SOURCES)) {
            return;
        }
        //通过http请求，获取配置中心的配置
        String app = configurableEnvironment.getProperty("yejjconfig.app","app1");
        String env = configurableEnvironment.getProperty("yejjconfig.env","dev");
        String ns = configurableEnvironment.getProperty("yejjconfig.ns","public");
        String configServer = configurableEnvironment.getProperty("configServer","http://localhost:9129");
        ConfigMeta config = new ConfigMeta(app,env,ns,configServer);
        YeJJConfigService configService = YeJJConfigService.getDefault(applicationContext,config);
        YeJJPropertySource propertySource = new YeJJPropertySource(YEJJ_PROPERTY_SOURCE,configService);
        //把配置的信息，塞入到spring配置中
        CompositePropertySource composite = new CompositePropertySource(YEJJ_PROPERTY_SOURCES);
        composite.addPropertySource(propertySource);
        configurableEnvironment.getPropertySources().addFirst(composite);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
