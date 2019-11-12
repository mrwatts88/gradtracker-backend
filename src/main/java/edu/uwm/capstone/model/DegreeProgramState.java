package edu.uwm.capstone.model;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DegreeProgramState extends BaseEntity {
    private long degreeProgramId;
    private String name;
    private String description;
}
