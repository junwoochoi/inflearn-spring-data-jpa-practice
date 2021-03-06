package com.ingang.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;


    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String name, int age, Team team) {
        this(name, age);

        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String name, int age, Team team, Address address) {
        this(name, age, team);

        this.address = address;
    }

    public Member(String name) {
        this.username = name;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
