package ru.ac.uniyar.testingcourse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CounterTest {
    Counter counter = new Counter();

    // проверка, что при инициализии значение поля value 0
    @Test
    void valueZero() {
        assertThat(counter.getValue()).isEqualTo(0);
    }

    // увеличение один раз
    @Test
    void oneIncrease() {
        counter.increase();
        assertThat(counter.getValue()).isEqualTo(1);
    }

    // увеличение 100 раз
    @Test
    void oneHundredIncrease() {
        for (int i = 0; i < 100; i++)
            counter.increase();
        assertThat(counter.getValue()).isEqualTo(100);
    }

    // обнуление один раз (0 до 0)
    @Test
    void resetZeroValue() {
        counter.reset();
        assertThat(counter.getValue()).isEqualTo(0);
    }

    // обнуление с ненулевого значения до нуля
    @Test
    void resetValueNotZero() {
        for (int i = 0; i < 100; i++)
            counter.increase();
        counter.reset();
        assertThat(counter.getValue()).isEqualTo(0);
    }

    // тест, что поле счетчика не статическое
    // при вызове методе увеличение счетчика у одного counter`а у второго значение не изменится
    @Test
    void twoCounter() {
        Counter counterBear = new Counter();
        counter.increase();
        assertThat(counter.getValue()).isEqualTo(1); // первый изменился на 1
        assertThat(counterBear.getValue()).isEqualTo(0); // второй counter не изменился
    }

}
