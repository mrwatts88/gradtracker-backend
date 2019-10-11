package edu.uwm.capstone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form extends BaseEntity {

    private Long user_id;
    private List<Field> fieldsList;

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
