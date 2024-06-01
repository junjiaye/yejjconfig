package io.github.junjiaye.yejjconfig.client.annotation;

import io.github.junjiaye.yejjconfig.client.config.YeJJConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * yejj config client entry point
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import(YeJJConfigRegistrar.class)
public @interface EnableYeJJConfig {
}
