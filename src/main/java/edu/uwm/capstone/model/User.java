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
public class User {

    @ApiModelProperty(hidden = true)
    protected Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String pantherId;
    private String email;

    @ApiModelProperty(hidden = true)
    protected LocalDateTime createdDate;
    @ApiModelProperty(hidden = true)
    protected LocalDateTime updatedDate;
}
