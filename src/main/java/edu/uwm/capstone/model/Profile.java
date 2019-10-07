package edu.uwm.capstone.model;

import java.time.LocalDateTime;

import lombok.*;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

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
