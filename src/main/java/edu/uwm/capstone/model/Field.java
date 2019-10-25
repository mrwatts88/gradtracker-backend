package edu.uwm.capstone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Field extends BaseEntity{
    @ApiModelProperty(hidden = true)
    private Long formId;
    private Long fieldDefId;
    private String data;

    @ApiModelProperty(hidden = true)
    @EqualsAndHashCode.Exclude
    private String label; // only needed for returning a field to front end

    @ApiModelProperty(hidden = true)
    @EqualsAndHashCode.Exclude
    private int fieldIndex; // only needed for returning a field to front end
}
