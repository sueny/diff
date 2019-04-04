package com.ssm.diff.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "match_offset")
public class MatchOffset {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @Column
    private Integer leftOffset;
    @Column
    private Integer rightOffset;
    @Column
    private Integer length;

    public MatchOffset() {
    }

    public MatchOffset(Integer leftOffset, Integer rightOffset, Integer length) {
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLeftOffset() {
        return leftOffset;
    }

    public void setLeftOffset(Integer leftOffset) {
        this.leftOffset = leftOffset;
    }

    public Integer getRightOffset() {
        return rightOffset;
    }

    public void setRightOffset(Integer rightOffset) {
        this.rightOffset = rightOffset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
