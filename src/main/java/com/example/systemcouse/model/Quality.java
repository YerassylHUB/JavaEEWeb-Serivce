package com.example.systemcouse.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Transactional
@Cacheable
public class Quality {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quality_id", nullable = false)
    private Long qualityId;

    @NotNull
    @Column(name = "quality_name", nullable = false)
    private String qualityName; // Добрый

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    public Quality(){}

    public Quality(Long qualityId, String qualityName, Tutor tutor) {
        this.qualityId = qualityId;
        this.qualityName = qualityName;
        this.tutor = tutor;
    }
}
