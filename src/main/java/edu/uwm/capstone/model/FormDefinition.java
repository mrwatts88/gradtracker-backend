package edu.uwm.capstone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormDefinition extends BaseEntity implements Iterable<FieldDefinition> {

    private List<FieldDefinition> fieldDefinitions;

    @Override
    public Iterator<FieldDefinition> iterator() {
        return fieldDefinitions.iterator();
    }

    @Override
    public void forEach(Consumer action) {
        fieldDefinitions.forEach(action);
    }

    @Override
    public Spliterator<FieldDefinition> spliterator() {
        return fieldDefinitions.spliterator();
    }

}
