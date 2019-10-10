package edu.uwm.capstone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefinition {

    @ApiModelProperty(hidden = true)
    protected Long id;
    private Long formDefId;
    private String label;
    private String dataType;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createdDate;
    @ApiModelProperty(hidden = true)
    private LocalDateTime updatedDate;
}
