package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {
    private static Timetable timetable;

    @BeforeEach
    public void beforeEach() {
        timetable = new Timetable();

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coachChild = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coachChild,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coachChild,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coachChild,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        TrainingSession wednesdayChildTrainingSession = new TrainingSession(groupChild, coachChild,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        timetable.addNewTrainingSession(wednesdayChildTrainingSession);

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach coachAdult = new Coach("Иванов", "Иван", "Иванович");
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coachAdult,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        TrainingSession wednesdayAdultTrainingSession = new TrainingSession(groupAdult, coachAdult,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(wednesdayAdultTrainingSession);
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Map<TimeOfDay, List<TrainingSession>> sessionsOnMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, sessionsOnMonday.size()); //Проверить, что за понедельник вернулось одно занятие

        Map<TimeOfDay, List<TrainingSession>> sessionsOnTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertEquals(0, sessionsOnTuesday.size()); //Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Map<TimeOfDay, List<TrainingSession>> sessionsOnMonday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, sessionsOnMonday.size());
        // Проверить, что за понедельник вернулось одно занятие

        Map<TimeOfDay, List<TrainingSession>> sessionsOnThursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, sessionsOnThursday.size());

        List<TimeOfDay> times = new ArrayList<>(sessionsOnThursday.keySet());

        TimeOfDay previousTime = null;
        for (TimeOfDay currentTime : times) {
            if (previousTime != null) {
                assertTrue(previousTime.compareTo(currentTime) < 0);
            }
            previousTime = currentTime;
        }
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00

        Map<TimeOfDay, List<TrainingSession>> sessionsOnTuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(sessionsOnTuesday.isEmpty());
        // Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        List<TrainingSession> sessionsOnMondayAt13 = timetable.getTrainingSessionsForTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        assertEquals(1, sessionsOnMondayAt13.size());
        //Проверить, что за понедельник в 13:00 вернулось одно занятие

        List<TrainingSession> sessionsOnMondayAt14 = timetable.getTrainingSessionsForTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        assertEquals(0, sessionsOnMondayAt14.size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
    }

    @Test
    void testGroupForChildAndAdultDifference() {
        List<TrainingSession> thursdayChildTrainingSession = timetable.getTrainingSessionsForTime
                (DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        Age groupChild = Age.CHILD;

        for (TrainingSession session : thursdayChildTrainingSession) {
            assertEquals(groupChild, session.getGroup().getAge());
        }

        List<TrainingSession> thursdayAdultTrainingSession = timetable.getTrainingSessionsForTime
                (DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        Age groupAdult = Age.ADULT;

        for (TrainingSession session : thursdayAdultTrainingSession) {
            assertEquals(groupAdult, session.getGroup().getAge());
        }
        // Проверить, что группа тренировки соответствует ожидаемой группе
    }

    @Test
    void testDifferentTrainingSessionsAtSameTime() {
        List<TrainingSession> wednesdayTrainingSession = timetable.getTrainingSessionsForTime
                (DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));
        assertEquals(2, wednesdayTrainingSession.size());
        // Проверить, что тренировки в один день и в одно время - разные тренировки

        assertFalse(wednesdayTrainingSession.get(0).equals(wednesdayTrainingSession.get(1)));
        // Проверить, что тренировки действительно разные
    }

    @Test
    void testGetAllTrainingSessions() {
        int totalSessions = 0;
        for (DayOfWeek day : DayOfWeek.values()) {
            Map<TimeOfDay, List<TrainingSession>> sessionsForWeek = timetable.getTrainingSessionsForDay(day);
            for (List<TrainingSession> sessionList : sessionsForWeek.values()) {
                totalSessions += sessionList.size();
            }
        }
        assertEquals(6, totalSessions);
        // Проверить общее количество тренировок
    }

    @Test
    void testCountTrainingsByCoaches() {
        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();

        assertEquals(4, coachCounts.get(0).getCount());
        assertEquals(2, coachCounts.get(1).getCount());
        // Проверить количество тренировок для каждого тренера по отдельности
    }

    @Test
    void testSortingOfTrainingsCount() {
        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();

        for (int i = 0; i < coachCounts.size() - 1; i++) {
            int currentCount = coachCounts.get(i).getCount();
            int nextCount = coachCounts.get(i + 1).getCount();

            assertTrue(currentCount >= nextCount, "Список должен быть отсортирован по убыванию " +
                    "количества тренировок");
            // Проверить, что список отсортирован по убыванию количества тренировок
        }
    }

    @Test
    void testNoTrainingsForCoaches() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> coachCounts = timetable.getCountByCoaches();

        assertTrue(coachCounts.isEmpty(), "Список должен быть пустым, если тренеры не провели тренировок");
        //Проверить, что список количества проведенных тренировок пуст
    }

}
