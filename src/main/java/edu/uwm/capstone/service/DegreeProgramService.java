package edu.uwm.capstone.service;

import edu.uwm.capstone.db.DegreeProgramDao;
import edu.uwm.capstone.db.DegreeProgramStateDao;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.model.DegreeProgramState;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.swing.text.html.parser.Entity;
import javax.validation.constraints.AssertFalse;
import java.util.List;

@Service("degreeProgramService")
public class DegreeProgramService {

    private static final Logger LOG = LoggerFactory.getLogger(DegreeProgramService.class);

    private final DegreeProgramDao degreeProgramDao;
    private final DegreeProgramStateDao degreeProgramStateDao;

    @Autowired
    public DegreeProgramService(DegreeProgramDao degreeProgramDao, DegreeProgramStateDao degreeProgramStateDao) {
        this.degreeProgramDao = degreeProgramDao;
        this.degreeProgramStateDao = degreeProgramStateDao;
    }

    public DegreeProgram create(DegreeProgram dp) {
        LOG.trace("Creating degree program {}", dp);
        checkValidDegreeProgram(dp, true);
        return degreeProgramDao.create(dp);
    }

    public DegreeProgram read(Long degreeProgramId) {
        LOG.trace("Reading degree program {}", degreeProgramId);

        DegreeProgram dp = degreeProgramDao.read(degreeProgramId);

        if(dp == null) {
            throw new EntityNotFoundException("Degree program with ID: " + degreeProgramId + " not found.");
        }
        return dp;
    }

    public List<DegreeProgram> readAll() {
        return degreeProgramDao.readAll();
    }

    public DegreeProgram update(DegreeProgram dp) {
        LOG.trace("Updating degree program {}", dp);

        checkValidDegreeProgram(dp, false);
        DegreeProgram dpInDb = degreeProgramDao.read(dp.getId());

        if(dpInDb == null) {
            throw new EntityNotFoundException("Could not update degree program " + dp.getId() + " - record not found.");
        }

        dp.setCreatedDate(dpInDb.getCreatedDate());
        return degreeProgramDao.update(dp);
    }

    // TODO: add logic to delete the degree program states with the degree program.
    public void delete(Long degreeProgramId) {
        LOG.trace("Deleting degree program {}", degreeProgramId);
        if (degreeProgramDao.read(degreeProgramId) == null) {
            throw new EntityNotFoundException("Could not delete degree program " + degreeProgramId + " - record not found.");
        }
        degreeProgramDao.delete(degreeProgramId);
    }

    public void checkValidDegreeProgram(DegreeProgram dp, boolean checkNullId) {
        Assert.notNull(dp, "Degree program cannot be null.");
        if(checkNullId) {
            Assert.isNull(dp.getId(), "Degree program ID should be null.");
        }
        else {
            // Assert that there is only one initial Degree Program State in the degree program
            List<DegreeProgramState> programStates = degreeProgramStateDao.readAllStatesByDegreeProgramId(dp.getId());
            Assert.notEmpty(programStates, "Degree program must have at least one degree program state.");
            int numInitial = 0;
            for(DegreeProgramState programState : programStates) {
                if(programState.isInitial()) {
                    numInitial++;
                }
            }
            Assert.isTrue(numInitial == 1, "Degree program must have only one initial state.");
        }
        Assert.notNull(dp.getName(), "Degree program name cannot be null.");
        Assert.notNull(dp.getDescription(), "Degree program description cannot be null.");

        // Assert that the Degree Program name is unique
        List<DegreeProgram> degreePrograms = degreeProgramDao.readAll();
        for (DegreeProgram program : degreePrograms) {
            if(program.getId() != dp.getId()) {
                Assert.isTrue(!(dp.getName().equals(program.getName())), "Degree program names must be unique.");
            }
        }
    }
}
