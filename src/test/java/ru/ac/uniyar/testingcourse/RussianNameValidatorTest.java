package ru.ac.uniyar.testingcourse;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RussianNameValidatorTest {
    RussianNameValidator russianNameValidator = new RussianNameValidator();

    // тест, если пустая строка
    // ожидается: false
    @Test
    void emptyWords() {
        assertThat(russianNameValidator.validate("")).isEqualTo(false);
    }

    // тест, когда обычные три слова с большой буквы, разделенные пробелами
    // ожидается: true
    @ParameterizedTest()
    @ValueSource(strings = {"Шевелев Александр Юрьевич", "Зайцев Максим Павлович", "Лебедев Александр Михайлович",
            "Урываев Михаил Алексеевич", "Алексеева Алиса Максимовна", "Хлапова Арина Александровна"})
    void correctThreeWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(true);
    }

    // тест, когда обычные два слова с большой буквы, разделенные пробелами (отсутствие отчества)
    // ожидается: true
    @ParameterizedTest()
    @ValueSource(strings = {"Ламири Лейла", "Ноунейм Ноунеймов"})
    void correctTwoWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(true);
    }

    // тест, когда есть двойное слово и три слова с большой буквы, разделенные пробелами, после дефиса заглавная буква
    // ожидается: true
    @ParameterizedTest()
    @ValueSource(strings = {"Шеве-Лев Александр Юрьевич", "З-Айцев Максим Павлович", "Лебедев Алекса-Ндр Михайлович",
            "Ур-Ываев Михаил Алексеевич", "Алексеева Али-Са Максимовна", "Хла-Пова Ари-На Александровна"})
    void correctDoubleThreeWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(true);
    }

    // тест, когда есть двойное слово и два слова с большой буквы, разделенные пробелами (отсутствие отчества), после дефиса заглавная буква
    // ожидается: true
    @ParameterizedTest()
    @ValueSource(strings = {"Лам-Ири Лейла", "Ноунейм Ноу-Неймов", "Н-Е Зн-Аю"})
    void correctDoubleTwoWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(true);
    }

    // тест, когда присутствует буква Ё/ё
    // ожидается: true
    @ParameterizedTest()
    @ValueSource(strings = {"Шевелёв Александр Юрьевич", "Ёлкин Павел", "Клёнова Алина"})
    void correctEncoding(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(true);
    }

    // тест, когда обычные слова и фамилия/имя/отчество с маленькой буквы
    // ожидается: false
    @ParameterizedTest()
    @ValueSource(strings = {"шевелёв александр юрьевич", "Хлапова Арина александровна", "алексеева Алиса Максимовна",
            "Снакина дарья Максимовна", "зайцев Максим Павлович", "Новосельский иван Андреевич",
            "Крупчатников Дмитрий романович", "Ламири лейла"})
    void letterStartUsuallyWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(false);
    }

    /**
     * Проверка в случае некорректного ФИО, присутствие специальных символов, цифр, не русских букв
     * Дано: {specialCharacter()}
     * Ожидаемый результат: false
     */
    // ! ? < > / \ | , . ' " : ; @ # $ % ^ & * ( ) { } [ ] ` ~ _
    @DisplayName("Ввод со спец. символами, цифрами и английскими буквами")
    @ParameterizedTest(name = "{0} = false")
    @MethodSource("specialCharacter")
    void russianNameValidatorSpecialCharacter(String fio) {
        boolean value = russianNameValidator.validate(fio);
        assertThat(value).isEqualTo(false);
    }

    private static Stream<Arguments> specialCharacter() {
        String str = "!?<>/\\|,.'\":;@#$%^&*(){}[]`~_abcd0123456789";
        Stream<Arguments> result = str.chars().
                mapToObj(c -> "Ив" + (char) c + "анов Иван") // Превращаем один объект в другой
                .map(s -> arguments(s)); // Делаем аргументы
        return result; // Преобразует каждый символ в строку
    }

    // тест, когда слова начинаются с большой буквы, а в середине заглавные буквы
    // ожидается: false
    @ParameterizedTest()
    @ValueSource(strings = {"ШевеЛев Александр Юрьевич", "Зайцев МаКсим Павлович", "Лебедев Александр МихаЙлович",
            "УрЫваев Михаил Алексеевич", "Алексеева АЛИСА Максимовна", "Хлапова АРина АлександровнА", "ЛАмири Лейла"})
    void upperInWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(false);
    }

    // тест, когда есть двойное слово и фамилия/имя/отчество с маленькой буквы
    // ожидается: false
    @ParameterizedTest()
    @ValueSource(strings = {"ше-Велёв александр юрьевич", "Хлап-Ова Арина александровна", "алексеева Али-Са Максимовна",
            "Снакина д-Арья Максимовна", "зайцев Макси-М Павлович", "Новосел-Ьский иван Андреевич",
            "Крупчатников Дм-Итрий романович"})
    void letterStartDoubleTwoWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(false);
    }

    // тест, когда есть двойное слово и три слова, слова начинаются с большой буквы, разделенные пробелами, после дефиса прописная буква
    // ожидается: false
    @ParameterizedTest()
    @ValueSource(strings = {"Шеве-лев Александр Юрьевич", "З-айцев Максим Павлович", "Лебедев Алекса-ндр Михайлович",
            "Ур-ываев Михаил Алексеевич", "Алексеева Али-са Максимовна", "Хла-пова Ари-на Александровна"})
    void uncorrectedDoubleThreeWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(false);
    }

    // тест, когда есть двойное слово и два слова, слова начинаются с большой буквы, разделенные пробелами (отсутствие отчества), после дефиса строчная буква
    // ожидается: false
    @ParameterizedTest()
    @ValueSource(strings = {"Лам-ири Лейла", "Ноунейм Ноу-неймов", "Н-е Зн-аю"})
    void uncorrectedDoubleTwoWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(false);
    }

    // тест, когда есть двойное слово и слова начинаются с большой буквы, разделенные пробелами, заглавная буква в середине
    // ожидается: false
    @ParameterizedTest()
    @ValueSource(strings = {"ШеВе-Лев Александр Юрьевич", "З-АйцеВ Максим Павлович", "Лебедев Алекса-Ндр МихайловИч",
            "Ур-ЫвАев Михаил Алексеевич", "АлексЕева Али-Са Максимовна", "Хла-Пова АРи-На Александровна"})
    void upperInMidDoubleWords(String fio) {
        assertThat(russianNameValidator.validate(fio)).isEqualTo(false);
    }


}
