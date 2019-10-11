package edu.uwm.capstone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Field extends BaseEntity {

    private Long formId;
    private String label;
    private String data;
    private int dataTypeFlag;

}
