package edu.uwm.capstone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormDefinition {

    @ApiModelProperty(hidden = true)
    protected Long id;
    private List<FieldDefinition> fieldDefinitions;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createdDate;
    @ApiModelProperty(hidden = true)
    private LocalDateTime updatedDate;

}
