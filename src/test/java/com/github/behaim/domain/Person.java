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
    private ColorEnum preferredColor;
    private EmptyEnum alwaysNullValue; // can only be null since it's an empty enum.
    private SingleValueEnum singleValue; // can only be null or have the single enum value.

    public Person() {
        super();
    }

    public Person(String name, BigDecimal annualSalary, Date birthday, Person manager, Collection<Person> team, ColorEnum preferredColor) {
        super();
        this.name = name;
        this.annualSalary = annualSalary;
        this.birthday = birthday;
        this.manager = manager;
        this.team = team;
        this.preferredColor = preferredColor;
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

    public ColorEnum getPreferredColor() {
        return preferredColor;
    }

    public EmptyEnum getAlwaysNullValue() {
        return alwaysNullValue;
    }

    public SingleValueEnum getSingleValue() {
        return singleValue;
    }
}
