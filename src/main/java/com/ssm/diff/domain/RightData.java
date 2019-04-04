package com.ssm.diff.domain;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "right_data")
public class RightData extends EncodedData {

    public RightData() {
    }

    public RightData(Long id, String data) {
        super(id, data);
    }
}
