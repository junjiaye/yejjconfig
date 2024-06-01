package io.github.junjiaye.yejjconfig.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @program: yejjconfig
 * @ClassName: SpringValue
 * @description:
 * @author: yejj
 * @create: 2024-05-26 09:03
 */
@Data
@AllArgsConstructor
public class SpringValue {
    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field  field;
}
