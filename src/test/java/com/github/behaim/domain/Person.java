package com.github.behaim.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class Person {
    private BigDecimal annualSalary;
    private Date birthday;
    private Person manager;
    private String name;
    private Collection<Person> team;

    public Person() {
        super();
    }

    public Person(String name, BigDecimal annualSalary, Date birthday, Person manager, Collection<Person> team) {
        super();
        this.name = name;
        this.annualSalary = annualSalary;
        this.birthday = birthday;
        this.manager = manager;
        this.team = team;
    }

    public BigDecimal getAnnualSalary() {
        return annualSalary;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Person getManager() {
        return manager;
    }

    public String getName() {
        return name;
    }

    public Collection<Person> getTeam() {
        return team;
    }
}
