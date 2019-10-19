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
public class Field extends BaseEntity{
    @ApiModelProperty(hidden = true)
    private Long formId;
    private Long fieldDefId;
    private String data;
    private int fieldIndex;
}
