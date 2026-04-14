package ru.ac.uniyar.testingcourse;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class BreakagesBrokerTest {
    BreakagesBroker breakagesBroker = new BreakagesBroker();

    // тест добавления поломки
    // ожидается, что при инициализации пустая коллекция
    // при добавлении в коллекции будет находиться только один добавленный объект Breakage
    @Test
    void addBreakage() {
        assertThat(breakagesBroker.findBreakages(null)).isEqualTo(List.of()); // проверка, список поломок пуст
        String description = "description1";
        String place = "place1";
        String contact = "contact1";

        breakagesBroker.addBreakage(description, place, contact);

        List<BreakagesBroker.Breakage> breakageList = breakagesBroker.findBreakages(place);
        assertThat(breakageList.size()).isEqualTo(1);

        BreakagesBroker.Breakage breakage = breakageList.getFirst();
        assertThat(breakage.description()).isEqualTo(description);
        assertThat(breakage.place()).isEqualTo(place);
        assertThat(breakage.contact()).isEqualTo(contact);
    }

    // тест получения списка неисправленных поломок по месту (place - строка)
    // ожидается список поломок с определенном местом
    @Test
    void getListPlaceString() {
        String place = "place1";
        breakagesBroker.addBreakage("d1", place, "c1");
        breakagesBroker.addBreakage("d2", "lalala", "c2");
        breakagesBroker.addBreakage("d3", place, "c3");

        List<BreakagesBroker.Breakage> breakageList = breakagesBroker.findBreakages(place);

        assertThat(breakageList).isEqualTo(
                List.of(
                        new BreakagesBroker.Breakage("d1", place, "c1"),
                        new BreakagesBroker.Breakage("d3", place, "c3")
                )
        );
    }

    // тест получения списка неисправленных поломок по месту (place - null)
    // ожидается список поломок с определенном местом
    @Test
    void getListPlaceNull() {
        String place1 = "place1";
        String place2 = "place2";
        breakagesBroker.addBreakage("d1", place1, "c1");
        breakagesBroker.addBreakage("d2", place2, "c2");
        breakagesBroker.addBreakage("d3", place1, "c3");

        List<BreakagesBroker.Breakage> breakageList = breakagesBroker.findBreakages(null);

        assertThat(breakageList).isEqualTo(
                List.of(
                        new BreakagesBroker.Breakage("d1", place1, "c1"),
                        new BreakagesBroker.Breakage("d2", place2, "c2"),
                        new BreakagesBroker.Breakage("d3", place1, "c3")
                )
        );
    }

    // тестирование пометки поломки как решенной, где починили один раз и поломка есть в нерешенных
    // ожидается в словаре починенных будет поломка с id мастера (проверка будет через списки)
    @Test
    void markBreakageSolvedInBreakages() {
        Integer solverId = 1;
        breakagesBroker.addBreakage("d1", "p1", "c1");
        breakagesBroker.addBreakage("d2", "p1", "c2");
        breakagesBroker.addBreakage("d3", "p3", "c3");

        BreakagesBroker.Breakage breakage = breakagesBroker.findBreakages("p1").getFirst();
        breakagesBroker.markBreakageSolved(breakage, solverId);

        List<BreakagesBroker.Breakage> breakageList = breakagesBroker.findSolvedBy(solverId);
        assertThat(breakageList).isEqualTo(
                List.of(
                        breakage
                )
        );
    }

    // отметка поломки как решенная, где починили два раза одну и ту же поломку и поломка есть в нерешенных
    // ожидается в словаре починенных будет поломка с id мастера (проверка будет через списки)
    @Test
    void markBreakageSolvedTwoInBreakages() {
        Integer solverId = 1;
        breakagesBroker.addBreakage("d1", "p1", "c1");
        breakagesBroker.addBreakage("d2", "p1", "c2");
        breakagesBroker.addBreakage("d3", "p3", "c3");

        BreakagesBroker.Breakage breakage = breakagesBroker.findBreakages("p1").getFirst();
        breakagesBroker.markBreakageSolved(breakage, solverId);
        breakagesBroker.markBreakageSolved(breakage, solverId);

        List<BreakagesBroker.Breakage> breakageList = breakagesBroker.findSolvedBy(solverId);
        assertThat(breakageList).isEqualTo(
                List.of(
                        breakage
                )
        );
    }


    // отметка поломки как решенной, где починили один раз и поломки нет в нерешенных
    // ожидается пустой список, элемента нет в поломке
    @Test
    void markBreakageSolvedNotInBreakages() {
        Integer solverId = 1;
        breakagesBroker.addBreakage("d1", "p1", "c1");
        BreakagesBroker.Breakage breakage = new BreakagesBroker.Breakage("d2", "p2", "c2");
        breakagesBroker.markBreakageSolved(breakage, solverId);

        List<BreakagesBroker.Breakage> breakageList = breakagesBroker.findSolvedBy(solverId);
        assertThat(breakageList.size()).isEqualTo(0);
    }
}
