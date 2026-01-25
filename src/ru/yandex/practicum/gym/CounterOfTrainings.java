package ru.yandex.practicum.gym;

public class CounterOfTrainings implements Comparable<CounterOfTrainings> {
    private Coach coach;
    private int count;

    @Override
    public int compareTo(CounterOfTrainings other) {
        return Integer.compare(this.count, other.count);
    }

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }
}
