package edu.uwm.capstone.service;

import edu.uwm.capstone.db.DegreeProgramDao;
import edu.uwm.capstone.model.DegreeProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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



}
