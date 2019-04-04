package com.ssm.diff.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "mismatch_offset")
public class MismatchOffset {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @Column
    private Integer index;
    @Column
    private Integer length;

    public MismatchOffset() {
    }

    public MismatchOffset(Integer index, Integer length) {
        this.index = index;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
