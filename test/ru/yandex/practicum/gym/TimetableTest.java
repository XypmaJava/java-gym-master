package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());//Проверить, что за понедельник вернулось одно занятие

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());//Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        // Проверить, что за понедельник вернулось одно занятие

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        Assertions.assertTrue(thursdaySessions.get(0).getTimeOfDay().equals(new TimeOfDay(13, 0)));
        Assertions.assertTrue(thursdaySessions.get(1).getTimeOfDay().equals(new TimeOfDay(20, 0)));
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
        // Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt1300 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        Assertions.assertEquals(1, sessionsAt1300.size());
        //Проверить, что за понедельник в 13:00 вернулось одно занятие

        List<TrainingSession> sessionsAt1400 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        Assertions.assertTrue(sessionsAt1400.isEmpty(), "Ожидалось, что занятия в 14:00 отсутствуют");
        //Проверить, что за понедельник в 14:00 не вернулось занятий
    }

    @Test
    void testSortingSessionsByTimeWithinOneDay() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession session3 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        TrainingSession session2 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession session1 = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(17, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        // Проверяем, что занятия отсортированы по времени
        Assertions.assertEquals(session3, mondaySessions.get(0));
        Assertions.assertEquals(session2, mondaySessions.get(1));
        Assertions.assertEquals(session1, mondaySessions.get(2));
    }

    @Test
    void testGetTrainingSessionsForEmptyDay() {
        Timetable timetable = new Timetable();

        // Проверяем, что за воскресенье не вернулось занятий
        List<TrainingSession> sundaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);
        Assertions.assertTrue(sundaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForEdgeCases() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        // Добавляем занятия на крайние времена дня
        TrainingSession morningSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(6, 0));
        TrainingSession eveningSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(22, 0));

        timetable.addNewTrainingSession(morningSession);
        timetable.addNewTrainingSession(eveningSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        // Проверяем, что занятия отсортированы по времени и крайние значения присутствуют
        Assertions.assertEquals(morningSession, mondaySessions.get(0));
        Assertions.assertEquals(eveningSession, mondaySessions.get(1));
    }

    @Test
    void testGetCountByCoaches_SingleCoachMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession session1 = new TrainingSession(new Group("Акробатика для детей", Age.CHILD, 60), coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        TrainingSession session2 = new TrainingSession(new Group("Акробатика для детей", Age.CHILD, 60), coach, DayOfWeek.TUESDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertEquals(1, counts.size());
        Assertions.assertEquals(coach, counts.get(0).getCoach());
        Assertions.assertEquals(2, counts.get(0).getCount());
    }

    @Test
    void testGetCountByCoaches_MultipleCoaches() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Смирнов", "Алексей", "Павлович");

        TrainingSession session1 = new TrainingSession(new Group("Акробатика для детей", Age.CHILD, 60), coach1, DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        TrainingSession session2 = new TrainingSession(new Group("Плавание", Age.ADULT, 45), coach2, DayOfWeek.WEDNESDAY, new TimeOfDay(17, 0));
        TrainingSession session3 = new TrainingSession(new Group("Акробатика для детей", Age.CHILD, 60), coach1, DayOfWeek.THURSDAY, new TimeOfDay(9, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertEquals(2, counts.size());
        Assertions.assertTrue(counts.get(0).getCoach() == coach1 || counts.get(1).getCoach() == coach1);
        Assertions.assertTrue(counts.get(0).getCount() == 2 && counts.get(1).getCount() == 1);
    }

    @Test
    void testGetCountByCoaches_NoSessions() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counts = timetable.getCountByCoaches();
        Assertions.assertTrue(counts.isEmpty());
    }
}
