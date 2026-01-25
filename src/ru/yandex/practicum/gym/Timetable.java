package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>()); // Используем TreeMap для сортировки по времени
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        if (!timetable.get(day).containsKey(time)) {
            timetable.get(day).put(time, new ArrayList<>());
        }
        timetable.get(day).get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> sessions = new ArrayList<>();
        Map<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        if (daySessions != null) {
            for (List<TrainingSession> sessionList : daySessions.values()) {
                sessions.addAll(sessionList);
            }
        }
        return sessions;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        if (daySessions == null) {
            return Collections.emptyList();
        }
        return daySessions.getOrDefault(timeOfDay, Collections.emptyList());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachTrainingsCount = new HashMap<>();

        for (Map<TimeOfDay, List<TrainingSession>> daySessions : timetable.values()) {
            for (List<TrainingSession> sessionList : daySessions.values()) {
                for (TrainingSession session : sessionList) {
                    Coach coach = session.getCoach();
                    coachTrainingsCount.put(coach, coachTrainingsCount.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> counters = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachTrainingsCount.entrySet()) {
            counters.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        counters.sort(Comparator.comparingInt(CounterOfTrainings::getCount).reversed());

        return counters;
    }
}

