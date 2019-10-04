package edu.uwm.capstone.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
