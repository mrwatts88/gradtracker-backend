package edu.uwm.capstone.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Iterator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Form extends BaseEntity implements Iterable<Field>  {
    private Long formDefId;
    private Long userId;
    private Boolean approved;
    private List<Field> fields;

    @ApiModelProperty(hidden = true)
    @EqualsAndHashCode.Exclude
    private String name; // only needed for returning a form to front end

    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public int size() {
        return fields.size();
    }
}
