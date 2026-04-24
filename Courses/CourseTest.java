package ru.ac.uniyar.testingcourse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseTest {

    Course makeFullCourse() {
        Course course = new Course(3);
        for (int i = 1; i <= 3; i++) {
            course.enroll(i);
        }
        return course;
    }

    Course makeWaitCourse() {
        Course course = makeFullCourse();
        course.enroll(4);
        return course;
    }

    // состояние неполного курса
    static void assertNotFull(Course course) {
        assertThat(course.isFullyEnrolled()).isFalse();
        assertThat(course.hasWaitingList()).isFalse();
    }

    // состояние полного курса
    static void assertFull(Course course) {
        assertThat(course.isFullyEnrolled()).isTrue();
        assertThat(course.hasWaitingList()).isFalse();
    }

    // состояние очереди
    static void assertWait(Course course) {
        assertThat(course.isFullyEnrolled()).isTrue();
        assertThat(course.hasWaitingList()).isTrue();
    }

    @Nested
    @DisplayName("Состояние: Неполный курс")
    class CourseNotFullTests {
        Course course = new Course(2);

        // проверка, что в незаполненный курс добавляется студент, при этом после добавления курс не заполнится до конца
        // ожидается: добавление студента в список курса
        @Test
        @DisplayName("Добавление недобавленного студента")
        void addUnenroll() {
            course.enroll(1);
            assertThat(course.getEnrollmentList()).containsOnly(1);
            assertThat(course.hasWaitingList()).isFalse();

            assertNotFull(course);
        }

        // тест добавление добавленного студента
        // ожидается: ничего не произойдет
        @Test
        @DisplayName("Добавление добавленного студента")
        void addEnroll() {
            course.enroll(1);
            course.enroll(1);
            assertThat(course.getEnrollmentList()).containsOnly(1);
            assertThat(course.hasWaitingList()).isFalse();

            assertNotFull(course);
        }

        // тест удаление несуществующего студента
        // ожидается: ничего не произойдет
        @Test
        @DisplayName("Удаление несуществующего студента")
        void unEnrollEmpty() {
            course.unenroll(1);
            assertThat(course.getEnrollmentList()).isEmpty();
            assertThat(course.getWaitingList()).isEmpty();

            assertNotFull(course);
        }

        // тест удаление существующего студента
        // ожидается: студент удалится из списка курса
        @Test
        @DisplayName("Удаление существующего студента")
        void unEnrollNotExist() {
            course.enroll(1);
            course.unenroll(1);
            assertThat(course.getEnrollmentList()).isEmpty();
            assertThat(course.getWaitingList()).isEmpty();

            assertNotFull(course);
        }

        // тест добавления студентов до максимума
        // ожидается: список курса станет полным
        @Test
        @DisplayName("Добавление студентов до максимума")
        void makeFull() {
            course.enroll(1);
            course.enroll(2);

            assertFull(course);
        }
    }

    @Nested
    @DisplayName("Состояние: Полный курс")
    class CourseFullTest {
        Course course = makeFullCourse();

        // тест удаления из полного курса
        // ожидается: удаление студента и курс становится не полным
        @Test
        @DisplayName("Удаление существующего")
        void unEnroll() {
            course.unenroll(1);
            assertNotFull(course);
        }

        // тест добавление добавленного студента
        // ожидается: ничего не произойдет
        @Test
        @DisplayName("Добавление добавленного студента")
        void addEnroll() {
            course.enroll(1);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.hasWaitingList()).isFalse();

            assertFull(course);
        }

        // тест удаление несуществующего студента
        // ожидается: ничего не произойдет
        @Test
        @DisplayName("Удаление несуществующего студента")
        void unEnrollNotExist() {
            course.unenroll(-1);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.hasWaitingList()).isFalse();

            assertFull(course);
        }

        // тест попытки добавить студента в полный курс
        // ожидается добавление студента в очередь
        @Test
        @DisplayName("Добавление студента в полный курс")
        void enrollFull() {
            course.enroll(4);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.getWaitingList()).containsOnly(4);

            assertWait(course);
        }
    }

    @Nested
    @DisplayName("Состояние: Ожидание")
    class Waiting {
        Course course = makeWaitCourse();

        // удаление студента из списка курса добавление в очередь
        // ожидается удаление студента из списка курса и добавление из списка очереди студента в список курса, удаление из списка очереди
        @Test
        @DisplayName("Удаление студента из списка курса и добавление в очередь")
        void enrollAddWait() {
            course.unenroll(1);
            assertThat(course.getEnrollmentList()).containsOnly(2, 3, 4);
            assertThat(course.hasWaitingList()).isFalse();

            assertFull(course);
        }

        // удаление студента из списка очереди
        // ожидадается удаление студента из списка очереди
        @Test
        @DisplayName("Удаление студента из списка очереди")
        void enrollWait() {
            course.unenroll(4);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.hasWaitingList()).isFalse();

            assertFull(course);
        }

        // добавление существующего студента из списка курса
        // ожидается: ничего не произойдет
        @Test
        @DisplayName("добавление существующего студента из списка курса")
        void addStudentCourseExists() {
            course.enroll(1);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.hasWaitingList()).isTrue();
            assertThat(course.getWaitingList()).containsOnly(4);

            assertWait(course);
        }

        // добавление существующего студента из списка очереди
        // ожидается: ничего не произойдет
        @Test
        @DisplayName("добавление существующего студента из списка очереди")
        void addStudentWaitExists() {
            course.enroll(4);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.getWaitingList()).containsOnly(4);

            assertWait(course);
        }

        // добавление нового студента в очередь
        // ожидается: добавление нового студента в очередь
        @Test
        @DisplayName("Добавление нового студента в очередь")
        void enrollNewWait() {
            course.enroll(5);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.getWaitingList()).containsExactly(4, 5);

            assertWait(course);
        }

        // удаление несуществующего студента
        // ожидается: ничего
        @Test
        @DisplayName("Удаление несуществующего студента")
        void unenrollNotExists() {
            course.unenroll(10);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.getWaitingList()).containsOnly(4);

            assertWait(course);
        }

        // удаление ожидающего студента
        // ожидается: удаление ожидающегося студента из списка очереди
        @Test
        @DisplayName("Удаление ожидающегося студента")
        void unenrollWaiting() {
            course.enroll(5);
            course.unenroll(4);
            assertThat(course.getEnrollmentList()).containsOnly(1, 2, 3);
            assertThat(course.getWaitingList()).containsOnly(5);

            assertWait(course);
        }

        // удаление из списка курса студента и добавления первого из очереди в список ожидания
        // ожидается: удаление из списка курса студента и добавления первого из очереди в список ожидания
        @Test
        @DisplayName("Удаление из списка курса и перенос первого студента из списка очереди в курс")
        void unenrollAndAddStudentsFromWait() {
            course.enroll(5);
            course.enroll(6);
            course.unenroll(1);

            assertThat(course.getEnrollmentList()).containsOnly(4, 2, 3);
            assertThat(course.getWaitingList()).containsExactly(5, 6);

            assertWait(course);
        }
    }
}