package ru.ac.uniyar.testingcourse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class LinearEquationSolverTest {

    // проверка, правильно ли решается линейное уравнение
    // дано: a = 4, b = 8
    // ожидается: 2.0
    @Test
    void oneRoot() {
        assertThat(LinearEquationSolver.solve(4, 8)).isEqualTo(2.0);
    }

    // проверка, если b = 0
    // дано: a = 4, b = 0
    // ожидается: 0.0
    @Test
    void bIsZero() {
        assertThat(LinearEquationSolver.solve(4, 0)).isEqualTo(0.0);
    }

    // проверка, если a = 0
    // дано: a = 0, b = 2
    // ожидается: null (нет решений)
    @Test
    void aIsZero() {
        assertThat(LinearEquationSolver.solve(0, 2)).isNull();
    }

    @Test
        // проверка, если есть отрицательное число
        // дано: a = 2, b = -4
        // ожидается: -2
    void negativeNum() {
        assertThat(LinearEquationSolver.solve(2, -4)).isEqualTo(-2.0);
    }

    // проверка, если a = 0 и b = 0
    // дано: a = 0, b = 0
    // ожидается: исключение, что x - любой
    @Test
    void abZero() {
        assertThatThrownBy(() -> LinearEquationSolver.solve(0, 0)).isInstanceOf(LinearEquationSolver.AnyNumberIsRootException.class);
    }

}
