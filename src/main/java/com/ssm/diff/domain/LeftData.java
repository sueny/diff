package com.ssm.diff.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "left_data")
public class LeftData extends EncodedData {

    public LeftData() {
    }

    public LeftData(Long id, String data) {
        super(id, data);
    }
}
