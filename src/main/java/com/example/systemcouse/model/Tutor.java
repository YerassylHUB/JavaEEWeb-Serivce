package com.example.systemcouse.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Transactional
@Cacheable
public class Tutor {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tutor_id", nullable = false)
    private Long tutorId;

    @NotNull
    @Column(name = "tutor_name", nullable = false)
    private String tutorName;

    @NotNull
    @Column(name = "specialization", nullable = false)
    private String specialization;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    public Tutor(){}

    public Tutor(Long tutorId, String tutorName, String specialization, String description) {
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.specialization = specialization;
        this.description = description;
    }

    private static List<Tutor> tutors = new ArrayList<Tutor>();

    public static void addTutor(Tutor tutor) {
        tutors.add(tutor);
    }

    public static List<Tutor> returnTutors() {
        return tutors;
    }
}
