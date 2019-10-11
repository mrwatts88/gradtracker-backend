package edu.uwm.capstone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefinition extends BaseEntity {

    @ApiModelProperty(hidden = true)
    private Long formDefId;
    private String label;
    private String dataType;

}
