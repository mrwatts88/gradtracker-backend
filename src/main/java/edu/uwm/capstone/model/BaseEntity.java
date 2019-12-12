package edu.uwm.capstone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity implements Serializable {

    @ApiModelProperty(hidden = true)
    protected Long id;
    @ApiModelProperty(hidden = true)
    private LocalDateTime createdDate;
    @ApiModelProperty(hidden = true)
    private LocalDateTime updatedDate;

}
