package ru.ac.uniyar.testingcourse;

/**
 * Класс предназначен для получения остаточного времени прогрева лазера
 * относительно текущего момента времени. Время прогрева определяется
 * по следующим правилам:
 * <ul>
 * <li>Время прогрева лазера из холодного состояния равно 2 мин.
 * <li>После выключения лазер полностью охлаждается за 1 мин.
 * <li>Если лазер охладился не полностью, то прогрев его не нужен при условии,
 * что предыдущий прогрев не был прерван.
 * <li>Если лазер охладился не полностью и предыдущий его прогрев был прерван,
 * то его следует прогревать в течение времени, оставшегося на момент прерывания
 * предыдущего прогрева.
 * </ul>
 */
public class WarmupController {
    //! Длительность прогрева из холодного состояния
    private final double warmupDurationFromCold = 120.0;
    //! Длительность промежутка времени, после которого прогрев осуществляется заново
    private final double thresholdDuration = 60.0;
    //! Время начала прогрева
    double warmupStartTime;
    //! Время остановки лазера
    double laserStopTime;
    //! Длительность текущего прогрева
    double currentWarmupDuration;
    //! Состояние лазера
    boolean on;

    /** Обозначить текущий момент в качестве момента включения лазера. */
    /**
     * Обозначить текущий момент в качестве момента включения лазера.
     */
    public void markLaserOn() {
        on = true; // Лазер включается
        double laserActiveDuration = laserStopTime - warmupStartTime; // Вычисляем, сколько времени лазер был активен в предыдущем цикле
        warmupStartTime = System.currentTimeMillis() / 1000.0; //  Обновляем время начала прогрева на текущее
        double laserInactiveDuration = warmupStartTime - laserStopTime; // сколько времени лазер был выключен

        if (laserInactiveDuration >= thresholdDuration) { // лазер остыл полностью (простой ≥ 60 сек)
            currentWarmupDuration = warmupDurationFromCold; // требуется полный прогрев с нуля (120 сек)
        } else if (laserActiveDuration < currentWarmupDuration) { // лазер не остыл полностью, И предыдущий прогрев был прерван
            currentWarmupDuration -= laserActiveDuration; // продолжаем прогрев с того места, где остановились
        } else { // лазер не остыл полностью, И предыдущий прогрев был завершён
            currentWarmupDuration = 0; // дополнительный прогрев не нужен
        }
    }

    /**
     * Обозначить текущий момент в качестве момента выключения лазера.
     */
    public void markLaserOff() {
        on = false;
        laserStopTime = System.currentTimeMillis() / 1000.0;
    }

    /**
     * Получить остаточное время прогрева лазера относительно текущего момента.
     *
     * @return Остаточное время прогрева в секундах.
     * @throws IllegalStateException если лазер выключен.
     */
    public double getRemainingTime() {
        if (!on) throw new IllegalStateException(
                "Getting remaining time when the laser is off is not allowed");
        return Math.max(0, currentWarmupDuration -
                (System.currentTimeMillis() / 1000.0 - warmupStartTime));
    }

    public WarmupController() {
        warmupStartTime = laserStopTime = -thresholdDuration * 2;
        currentWarmupDuration = 0;
        on = false;
    }
}
