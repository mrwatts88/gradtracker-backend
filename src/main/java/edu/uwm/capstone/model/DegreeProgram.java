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
public class DegreeProgram extends BaseEntity implements Iterable<DegreeProgramState>{
    private String name;
    private String description;
    private List<DegreeProgramState> degreeProgramStates;

    @Override
    public Iterator<DegreeProgramState> iterator() { return degreeProgramStates.iterator(); }

    public DegreeProgramState getDegreeProgramStateById(Long dpStateId) {
        for(DegreeProgramState dps : degreeProgramStates) {
            if(dps.getId() == null) continue;
            if(dps.getId().equals(dpStateId)) return dps;
        }
        return null;
    }
}