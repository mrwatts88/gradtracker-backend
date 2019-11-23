package edu.uwm.capstone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DegreeProgramState extends BaseEntity {
    private Long degreeProgramId;
    private String name;
    private String description;
    private boolean initial;
}
