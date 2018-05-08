package site.binghai.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.binghai.Enums.ContractStatusEnum;
import site.binghai.service.ContractService;

/**
 * 自动确认机器人
 * Created by IceSea on 2018/5/8.
 * GitHub: https://github.com/IceSeaOnly
 */
@Component
@EnableScheduling
public class autoRobot {
    @Autowired
    private ContractService contractService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void start() {
        contractService.findByOfficeId(3L)
                .stream()
                .filter(v -> v.getState().equals(ContractStatusEnum.DETAILED.getCode()))
                .forEach(v -> {
                    v.setState(v.getState() + 1);
                    contractService.update(v);
                });
    }
}
