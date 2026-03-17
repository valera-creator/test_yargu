package ru.ac.uniyar.testingcourse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WarmupControllerTest {
    WarmupController warmupController = new WarmupController();

    // проверка, что после создания контроллера время нагрева 120 секунд
    @Test
    void startCold() {
        warmupController.markLaserOn();
        assertThat(warmupController.getRemainingTime()).isEqualTo(120);
    }

    // проверка, что лазер включается
    @Test
    void startLazer() {
        warmupController.markLaserOn();
        assertThat(warmupController.on).isTrue();
    }

    // проверка, что лазер выключается
    @Test
    void offLazer() {
        warmupController.markLaserOn();
        warmupController.markLaserOff();
        assertThat(warmupController.on).isFalse();
    }

    // проверка, что лазер через 120 секунд нагретый
    @Test
    void warmupFullInTwoMinutes() {
        // Имитируем, что с момента начала прогрева прошло 120+ секунд
        warmupController.markLaserOn();
        warmupController.warmupStartTime = (System.currentTimeMillis() / 1000.0) - 120.0; // во сколько начался прогрев
        warmupController.currentWarmupDuration = 120.0; // прогрев должен идти 120 секунд

        assertThat(warmupController.getRemainingTime()).isEqualTo(0.0);

    }


}
