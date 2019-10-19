package edu.uwm.capstone.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form extends BaseEntity implements Iterable<Field>  {
    @ApiModelProperty(hidden = true)
    private Long formDefId;
    private Long userId;
    private Boolean approved;
    private String name;
    private List<Field> fields;

    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public int size() {
        return fields.size();
    }
}
