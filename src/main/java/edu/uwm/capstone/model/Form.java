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
public class Form {

    @ApiModelProperty(hidden = true)
    protected Long id;
    private Long user_id;
    private List<Field> fieldsList;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createdDate;
    @ApiModelProperty(hidden = true)
    private LocalDateTime updatedDate;

    public void addField(Field f) {
        if (this.fieldsList != null)
            this.fieldsList.add(f);
    }

    public boolean removeField(Field f) {
        if (this.fieldsList != null)
            return this.fieldsList.remove(f);
        return false;
    }

}
