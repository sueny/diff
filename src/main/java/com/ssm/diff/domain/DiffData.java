package com.ssm.diff.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssm.diff.alignment.Mismatch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "diff_data")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiffData {
    @Id
    private Long id;
    @Column
    private Boolean equal;
    @Column
    private Integer leftDataSize;
    @Column
    private Integer rightDataSize;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchOffset> matches = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MismatchOffset> leftMismatches = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MismatchOffset> rightMismatches = new ArrayList<>();

    public DiffData() {
    }

    public DiffData(Long id, Boolean equal) {
        this.id = id;
        this.equal = equal;
    }

    public DiffData(Long id, Integer leftDataSize, Integer rightDataSize) {
        this.id = id;
        this.leftDataSize = leftDataSize;
        this.rightDataSize = rightDataSize;
    }

    public DiffData(Long id, List<MatchOffset> matches, List<MismatchOffset> leftMismatches, List<MismatchOffset> rightMismatches) {
        this.id = id;
        this.matches = matches;
        this.leftMismatches = leftMismatches;
        this.rightMismatches = rightMismatches;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEqual() {
        return equal;
    }

    public void setEqual(Boolean equal) {
        this.equal = equal;
    }

    public Integer getLeftDataSize() {
        return leftDataSize;
    }

    public void setLeftDataSize(Integer leftDataSize) {
        this.leftDataSize = leftDataSize;
    }

    public Integer getRightDataSize() {
        return rightDataSize;
    }

    public void setRightDataSize(Integer rightDataSize) {
        this.rightDataSize = rightDataSize;
    }

    public List<MatchOffset> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchOffset> matches) {
        this.matches = matches;
    }

    public List<MismatchOffset> getLeftMismatches() {
        return leftMismatches;
    }

    public void setLeftMismatches(List<MismatchOffset> leftMismatches) {
        this.leftMismatches = leftMismatches;
    }

    public List<MismatchOffset> getRightMismatches() {
        return rightMismatches;
    }

    public void setRightMismatches(List<MismatchOffset> rightMismatches) {
        this.rightMismatches = rightMismatches;
    }
}
