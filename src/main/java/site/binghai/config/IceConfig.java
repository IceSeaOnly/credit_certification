package site.binghai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by IceSea on 2018/5/8.
 * GitHub: https://github.com/IceSeaOnly
 */
@Component
@ConfigurationProperties(prefix = "ice")
@PropertySource("classpath:application.properties")
@Data
public class IceConfig {
    private String server;
    private String smartContractAddress;
}
