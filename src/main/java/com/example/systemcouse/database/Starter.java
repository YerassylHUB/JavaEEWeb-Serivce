package com.example.systemcouse.database;

import com.example.systemcouse.model.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Starter {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseManagementSystem");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        for(int i=1; i<10; i++) {
            Task task = new Task();
            task.setTaskName("Task Name " + i);

            entityManager.persist(task);
        }

        List<Rank> ranks = new ArrayList<>();
        for(int i=1; i<10; i++) {
            Rank rank = new Rank();
            rank.setRankName("Score Name " + i);
            ranks.add(rank);
            entityManager.persist(rank);
        }

        List<User_> userList = new ArrayList<>();
        for(int i=0; i<ranks.size(); i++) {
            User_ user = new User_();
            user.setUserName("User Name " + (i+1));
            user.setRank(ranks.get(i));
            user.setLastSession(LocalDate.of(2021, 10, i+20));
            userList.add(user);
            entityManager.persist(user);
        }

        List<Tutor> tutors = new ArrayList<>();
        for(int i=0; i<userList.size(); i++) {
            Tutor tutor = new Tutor();
            tutor.setTutorName("Tutor Name " + i+1);
            tutor.setSpecialization("Specialization Name " + i+1);
            tutor.setDescription("Description " + i+1);
            tutors.add(tutor);
            entityManager.persist(tutor);
        }
        for(int i=0; i<userList.size(); i++) {
            Quality quality = new Quality();
            quality.setQualityName("Ability Name " + i +1);
            quality.setTutor(tutors.get(i));
            entityManager.persist(quality);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
