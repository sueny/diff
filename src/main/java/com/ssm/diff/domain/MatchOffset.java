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
    private Integer leftIndex;
    @Column
    private Integer rightIndex;
    @Column
    private Integer length;

    public MatchOffset() {
    }

    public MatchOffset(Integer leftIndex, Integer rightIndex, Integer length) {
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(Integer leftIndex) {
        this.leftIndex = leftIndex;
    }

    public Integer getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(Integer rightIndex) {
        this.rightIndex = rightIndex;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
