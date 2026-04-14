package ru.ac.uniyar.testingcourse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Модуль передачи информации о поломках.
 * Используется для:
 * * Добавления информации о поломках
 * * Получения списков неустранённых поломок: общего и для конкретных мест
 * * Сохранение информации о том, что поломка была устранена
 * * Получения информации о том, какие мастера устраняли поломки
 */
public class BreakagesBroker {

    private final Map<Breakage, Integer> breakages = new HashMap<>();

    /**
     * Поломка, зарегистрированная в системе
     */
    public record Breakage(
            String description,
            String place,
            String contact
    ) {
    }

    /**
     * Зарегистрировать поломку в системе
     *
     * @param description описание поломки
     * @param place       место поломки
     * @param contact     контактный телефон оставившего информацию
     */
    public void addBreakage(String description, String place, String contact) {
        breakages.put(new Breakage(description, place, contact), null);
    }

    /**
     * Получить список неисправленных поломок
     *
     * @param forPlace место, для которого ищутся поломки. Если null, то поломки ищутся для всех мест.
     * @return список неустранённых поломок
     */
    public List<Breakage> findBreakages(String forPlace) {
        Stream<Map.Entry<Breakage, Integer>> breakagesStream = breakages.entrySet().stream();
        breakagesStream = breakagesStream.filter((e) -> e.getValue() == null);
        if (forPlace != null) {
            breakagesStream = breakagesStream
                    .filter((e) -> e.getKey().place.equals(forPlace));
        }
        return breakagesStream.map(Map.Entry::getKey).toList();
    }

    /**
     * Отметить поломку как решённую
     *
     * @param breakage поломка
     * @param solverId номер сотрудника, устранившего поломку
     */
    public void markBreakageSolved(Breakage breakage, Integer solverId) {
        breakages.put(breakage, solverId);
    }

    /**
     * Получить список поломок, устранённых сотрудником
     *
     * @param solverId номер сотрудника
     * @return список поломок, устранённых сотрудником
     */
    public List<Breakage> findSolvedBy(Integer solverId) {
        return breakages.entrySet()
                .stream()
                .filter((e) -> solverId.equals(e.getValue()))
                //.filter((e) -> e.getValue().equals(solverId)) - выдает ошибку в тесте markBreakageSolvedInBreakages
                .map(Map.Entry::getKey)
                .toList();
    }
}
