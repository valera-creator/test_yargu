package ru.ac.uniyar.testingcourse.whitebox;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class WarmupControllerTest {
    MockTimeProvider mockTimeProvider = new MockTimeProvider();
    WarmupController warmupControllerFakeTime = new WarmupController(mockTimeProvider);

    // тест конструктора без параметра
    @Test
    void constructorDefaultTest() {
        WarmupController warmupControllerDefaultTime = new WarmupController();
        assertThat(warmupControllerDefaultTime.on).isFalse();
    }

    // тест конструктора с параметром
    // ожидается экземпляр с начальными значениями
    @Test
    void constructorParameterTest() {
        assertThat(warmupControllerFakeTime.on).isFalse();
        assertThat(warmupControllerFakeTime.timeProvider).isEqualTo(mockTimeProvider);
        assertThat(warmupControllerFakeTime.warmupStartTime).isEqualTo(-120.0);
        assertThat(warmupControllerFakeTime.currentWarmupDuration).isEqualTo(0.0);
    }


    // тестирование включения лазера
    // если лазер был полностью заряженным, а потом выключеным 60 секунд
    // ожидается: полный прогрев
    @Test
    void markLaserOnTestOffMore60Sec() {
        warmupControllerFakeTime.currentWarmupDuration = 120.0;
        warmupControllerFakeTime.warmupStartTime = 40.0;
        warmupControllerFakeTime.laserStopTime = 160.0;

        mockTimeProvider.skipSeconds(160 + 60); // прошло времени больше, чем 60 секунд с момента выключения
        warmupControllerFakeTime.markLaserOn();
        assertThat(warmupControllerFakeTime.on).isTrue();

        assertThat(warmupControllerFakeTime.currentWarmupDuration).isEqualTo(120.0);
    }

    // тестирование включения лазера
    // если лазер был полностью заряженным, а потом выключеным менее 60 секунд
    // ожидается: полный неполный прогрев
    @Test
    void markLaserOnTestOffLess60Sec() {
        warmupControllerFakeTime.warmupStartTime = 70.0;
        warmupControllerFakeTime.laserStopTime = 100.0;
        warmupControllerFakeTime.currentWarmupDuration = 120.0;

        mockTimeProvider.skipSeconds(159);
        warmupControllerFakeTime.markLaserOn();
        assertThat(warmupControllerFakeTime.on).isTrue();

        // 120 - (100 - 70) = 90, при этом выключенным после зарядки менее 60 секунд (159 - 120 < 60)
        assertThat(warmupControllerFakeTime.currentWarmupDuration).isEqualTo(90.0);
    }

    // тестирование включения лазера
    // ожидается: не надо прогревать лазер
    @Test
    void markLaserOnTestElse() {
        warmupControllerFakeTime.warmupStartTime = 0.0;
        warmupControllerFakeTime.laserStopTime = 120.0;
        warmupControllerFakeTime.currentWarmupDuration = 120.0;

        mockTimeProvider.skipSeconds(120);

        warmupControllerFakeTime.markLaserOn();
        assertThat(warmupControllerFakeTime.on).isTrue();

        assertThat(warmupControllerFakeTime.currentWarmupDuration).isEqualTo(0.0);
    }

    // тестирование отключение лазера
    // ожидается, что состояние лазер выключен и время остановки равно текущему времени
    @Test
    void markLaserOffTest() {
        mockTimeProvider.skipSeconds(100); // вдруг всегда 0 записано там

        warmupControllerFakeTime.markLaserOff();
        assertThat(warmupControllerFakeTime.on).isFalse();
        assertThat(warmupControllerFakeTime.laserStopTime).isEqualTo(100.0);
    }

    // тестирование время прогрева, когда он выключен
    // ожидается исключение IllegalStateException
    @Test
    void RemainingTimeLaserOf() {
        assertThatThrownBy(() -> warmupControllerFakeTime.getRemainingTime()).isInstanceOf(IllegalStateException.class);
    }

    // тестирование время прогрева, когда он включен но не начал прогреваться
    // ожидается остаточное время прогрева в секундах равно полному времени прогрева.
    @Test
    void RemainingTimeLaserOnNotWarmedUp() {
        warmupControllerFakeTime.markLaserOn();
        assertThat(warmupControllerFakeTime.getRemainingTime()).isEqualTo(120.0);
    }

    // тестирование время прогрева, когда лазер прогрелся не до конца
    // ожидается остаточное время прогрева в секундах равно
    // разности полного времени прогрева - кол-во секунд, которые уже прогрелись.
    @Test
    void RemainingTimeLaserOnWarmedUpNotFull() {
        warmupControllerFakeTime.markLaserOn();
        int warnUpSeconds = 100;
        mockTimeProvider.skipSeconds(warnUpSeconds);
        assertThat(warmupControllerFakeTime.getRemainingTime()).isEqualTo(120.0 - warnUpSeconds);
    }

    // тестирование время прогрева, когда лазер прогрелся до конца
    // ожидается остаточное время прогрева в секундах равно 0
    @Test
    void RemainingTimeLaserOnWarmedUpFull() {
        warmupControllerFakeTime.markLaserOn();
        mockTimeProvider.skipSeconds(120);
        assertThat(warmupControllerFakeTime.getRemainingTime()).isEqualTo(0.0);
    }

}
