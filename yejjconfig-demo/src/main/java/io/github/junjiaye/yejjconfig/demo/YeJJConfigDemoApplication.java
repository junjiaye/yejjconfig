package io.github.junjiaye.yejjconfig.demo;

import io.github.junjiaye.yejjconfig.client.annotation.EnableYeJJConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author yejj
 */
@SpringBootApplication
@EnableConfigurationProperties(YeJJDemoConfig.class)
@EnableYeJJConfig
@RestController
public class YeJJConfigDemoApplication {

    @Value("${yejj.a}")
    private String a;

    @Value("${yejj.b}")
    private String b;

    @Autowired
    Environment environment;

    @Autowired
    private YeJJDemoConfig yeJJDemoConfig;
    public static void main(String[] args) {
        SpringApplication.run(YeJJConfigDemoApplication.class, args);
    }

    @GetMapping("/demo")
    public String demo() {
        return a + "====" +b + "====" + yeJJDemoConfig.getA() + "====" + yeJJDemoConfig.getB();
    }

    @Bean
    ApplicationRunner applicationRunner(){
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        return args -> {
            System.out.println(a);
            System.out.println(yeJJDemoConfig.getA());
        };
    }
}
