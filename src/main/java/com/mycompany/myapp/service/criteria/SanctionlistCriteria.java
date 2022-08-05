package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Sanctionlist} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SanctionlistResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sanctionlists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SanctionlistCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private LocalDateFilter dob;

    private StringFilter address;

    private StringFilter passport;

    private IntegerFilter score;

    private Boolean distinct;

    public SanctionlistCriteria() {}

    public SanctionlistCriteria(SanctionlistCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.passport = other.passport == null ? null : other.passport.copy();
        this.score = other.score == null ? null : other.score.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SanctionlistCriteria copy() {
        return new SanctionlistCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public LocalDateFilter getDob() {
        return dob;
    }

    public LocalDateFilter dob() {
        if (dob == null) {
            dob = new LocalDateFilter();
        }
        return dob;
    }

    public void setDob(LocalDateFilter dob) {
        this.dob = dob;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPassport() {
        return passport;
    }

    public StringFilter passport() {
        if (passport == null) {
            passport = new StringFilter();
        }
        return passport;
    }

    public void setPassport(StringFilter passport) {
        this.passport = passport;
    }

    public IntegerFilter getScore() {
        return score;
    }

    public IntegerFilter score() {
        if (score == null) {
            score = new IntegerFilter();
        }
        return score;
    }

    public void setScore(IntegerFilter score) {
        this.score = score;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SanctionlistCriteria that = (SanctionlistCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(address, that.address) &&
            Objects.equals(passport, that.passport) &&
            Objects.equals(score, that.score) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dob, address, passport, score, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionlistCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (dob != null ? "dob=" + dob + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (passport != null ? "passport=" + passport + ", " : "") +
            (score != null ? "score=" + score + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
