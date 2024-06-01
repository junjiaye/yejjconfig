package io.github.junjiaye.yejjconfig.client.config;

import io.github.junjiaye.yejjconfig.client.value.SpringValueProcessor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

/**
 * @program: yejjconfig-client
 * @ClassName: YeJJConfigRegiser
 * @description:
 * @author: yejj
 * @create: 2024-05-25 09:08
 */
public class YeJJConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        System.out.println("register Processor");
        //检查是否已注册
        //Class<?> aClass = PropertySourcesProcessor.class;
        registerClass(registry, PropertySourcesProcessor.class);
        registerClass(registry, SpringValueProcessor.class);

    }

    private static void registerClass(BeanDefinitionRegistry registry, Class<?> aClass) {
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(x -> aClass.getName().equals(x)).findFirst();
        if (first.isPresent()){
            return;
        }
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(aClass).getBeanDefinition();
        registry.registerBeanDefinition(aClass.getName(),beanDefinition);
    }
}
