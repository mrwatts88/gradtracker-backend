package edu.uwm.capstone.model;

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
public class FormDefinition extends BaseEntity implements Iterable<FieldDefinition> {
    private String name;
    private List<FieldDefinition> fieldDefs;

    @Override
    public Iterator<FieldDefinition> iterator() {
        return fieldDefs.iterator();
    }

    public int size() {
        return fieldDefs.size();
    }

    public void setId(Long id) {
        this.id = id;
        for(FieldDefinition fd : this) {
            fd.setFormDefId(id);
        }
    }
}
