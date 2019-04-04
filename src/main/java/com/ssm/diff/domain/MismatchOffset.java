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
    private Integer charOffset;
    @Column
    private Integer length;

    public MismatchOffset() {
    }

    public MismatchOffset(Integer charOffset, Integer length) {
        this.charOffset = charOffset;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCharOffset() {
        return charOffset;
    }

    public void setCharOffset(Integer charOffset) {
        this.charOffset = charOffset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
