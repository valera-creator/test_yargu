package ru.ac.uniyar.testingcourse.whitebox;

public class MockTimeProvider implements TimeProvider {

    long currentTime = 0;

    @Override
    public long currentTimeMillis() {
        return currentTime;
    }

    void skipSeconds(double seconds) {
        currentTime += (long) (seconds * 1000);
    }
}
