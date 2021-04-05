package com.github.th997.userserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@EnableScheduling
@Component
public class DynamicCronHandler implements SchedulingConfigurer {

    @Value("${taskCron:0/5 * * * * ?}")
    private String taskCron;

    private String getTaskCron() {
        return taskCron;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
            System.out.println("执行任务：" + new Date());
            if (new Random().nextInt(10) == 1) {
                throw new RuntimeException("定时任务异常测试！！！");
            }
        }, triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(this.getTaskCron());
            Date nextExecDate = cronTrigger.nextExecutionTime(triggerContext);
            return nextExecDate;
        });

    }
}