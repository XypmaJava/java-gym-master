package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek,Map<TimeOfDay,List <TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        if(!timetable.containsKey(dayOfWeek)) {
            timetable.put(dayOfWeek, new TreeMap<>());
        }

        Map<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (!sessionsForDay.containsKey(timeOfDay)) {
            sessionsForDay.put(timeOfDay, new ArrayList<>());
        }

        sessionsForDay.get(timeOfDay).add(trainingSession);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, Collections.emptyMap());
    }

    public List<TrainingSession> getTrainingSessionsForTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> sessionsForDay = getTrainingSessionsForDay(dayOfWeek);
        return sessionsForDay.getOrDefault(timeOfDay, Collections.emptyList());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (Map<TimeOfDay, List<TrainingSession>> sessionsForDay : timetable.values()) {
            for (List<TrainingSession> sessionList : sessionsForDay.values()) {
                for (TrainingSession session : sessionList) {
                    Coach coach = session.getCoach();
                    coachCounts.put(coach, coachCounts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> counterList = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            counterList.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        counterList.sort(Comparator.comparingInt(CounterOfTrainings::getCount).reversed());

        return counterList;
    }
}