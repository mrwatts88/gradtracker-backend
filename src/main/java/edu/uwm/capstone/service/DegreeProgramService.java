package edu.uwm.capstone.service;

import edu.uwm.capstone.db.DegreeProgramDao;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service("degreeProgramService")
public class DegreeProgramService {

    private static final Logger LOG = LoggerFactory.getLogger(DegreeProgramService.class);

    private final DegreeProgramDao degreeProgramDao;

    @Autowired
    public DegreeProgramService(DegreeProgramDao degreeProgramDao) {
        this.degreeProgramDao = degreeProgramDao;
    }

    public DegreeProgram create(DegreeProgram dp) {
        LOG.trace("Creating degree program {}", dp);
        // Check the Degree Program validity
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

        // check valid dp
        DegreeProgram dpInDb = degreeProgramDao.read(dp.getId());

        if(dpInDb == null) {
            throw new EntityNotFoundException("Could not update degree program " + dp.getId() + " - record not found.");
        }

        dp.setCreatedDate(dpInDb.getCreatedDate());
        return degreeProgramDao.update(dp);
    }

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
        Assert.notNull(dp.getName(), "Degree program name cannot be null.");
        Assert.notNull(dp.getDescription(), "Degree program description cannot be null.");

        //TODO: check that degree program's connected degree program states aren't null
    }
}
