package com.example.systemcouse.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Transactional
@Cacheable
public class Rank {

    public Rank() {}

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rank_id", nullable = false)
    private Long rankId;

    @NotNull
    @Column(name = "rank_name", nullable = false)
    private String rankName;

    public Rank(Long rankId, String rankName) {
        this.rankId = rankId;
        this.rankName = rankName;
    }
}
