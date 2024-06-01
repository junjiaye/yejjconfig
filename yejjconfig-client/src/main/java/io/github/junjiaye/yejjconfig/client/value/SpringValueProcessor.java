package io.github.junjiaye.yejjconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import io.github.junjiaye.yejjconfig.client.urils.PlaceholderHelper;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * @program: yejjconfig
 * @ClassName: SpringValueProcessor
 * @description: 处理spring @value注解的配置更新
 *               1.扫描所有的spring @value注解，并保存下来
 *               2，在配置更新时，更新spring @value注解的值
 * @author: yejj
 * @create: 2024-05-26 08:28
 */
@Setter
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {
    static final Logger log = LoggerFactory.getLogger(SpringValueProcessor.class);
    //占位符处理器
    static final PlaceholderHelper helper = PlaceholderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();

    BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        //获取
        List<Field> fields = FieldUtils.findAnnotatedField(bean.getClass(), Value.class);
        fields.forEach(field -> {
            log.info("[YEJJCONFIG] find spring value:{}",field);
            //获取注解的值
            Value value = field.getAnnotation(Value.class);
            Set<String> places = helper.extractPlaceholderKeys(value.value());
            places.forEach(key ->{
                SpringValue springValue = new SpringValue(bean,beanName,key,value.value(),field);
                VALUE_HOLDER.add(key,springValue);
            });

        });
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

//    @EventListener  等价于 实现ApplicationListener<EnvironmentChangeEvent> 接口
//    private void onChange(EnvironmentChangeEvent event){
//
//    }
    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().forEach(key ->{
            log.info("[YEJJCONFIG] update spring value:{}",key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            //卫语句
            if (springValues == null || springValues.isEmpty()){
                return;
            }
            springValues.forEach(springValue ->{
                try {
                    Object value = helper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory,springValue.getBeanName(),springValue.getPlaceholder());
                    springValue.getField().setAccessible(true);
                    springValue.getField().set(springValue.getBean(),value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}
