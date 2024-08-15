package ru.vtb.jpro.limits;


import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
import org.springframework.scheduling.support.ScheduledMethodRunnable;


@SpringBootTest
class TestCheckScheduler {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("Проверка времени и метода запуска Scheduler-а")
    void testScheduler() throws NoSuchFieldException, IllegalAccessException {
        ScheduledTask scheduledTask = context.getBean(ScheduledTaskHolder.class)
                                          .getScheduledTasks()
                                          .iterator()
                                          .next();
        //Проверяем время следующего запуска
        Field fieldFuture = ScheduledTask.class.getDeclaredField("future");
        fieldFuture.setAccessible(true);
        ScheduledFuture scheduledFuture = (ScheduledFuture) fieldFuture.get(scheduledTask);
        //Время следующего запуск
        long hNextRun = scheduledFuture.getDelay(TimeUnit.HOURS);
        int hCurTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"))
                           .get(Calendar.HOUR_OF_DAY);
        long hInterval = 24 - hNextRun - hCurTime;
        Assertions.assertTrue(hInterval >= 0 && hInterval <= 1);

        //Проверяем запускаемый метод
        DelegatingErrorHandlingRunnable delegatingRunnable = (DelegatingErrorHandlingRunnable) scheduledFuture;
        Field fieldDelegate = DelegatingErrorHandlingRunnable.class.getDeclaredField("delegate");
        fieldDelegate.setAccessible(true);
        //Запускаемый метод
        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) fieldDelegate.get(delegatingRunnable);
        Assertions.assertEquals("restoreDailyLimitsForAll", runnable.getMethod()
                                                                .getName());
    }
}
