package edu.uwm.capstone.db;

import edu.uwm.capstone.UnitTestConfig;
import edu.uwm.capstone.model.*;
import edu.uwm.capstone.util.TestDataUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class DegreeProgramDaoComponentTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DegreeProgramDao degreeProgramDaoDao;

    @Autowired
    DegreeProgramStateDao degreeProgramStateDao;

    private List<DegreeProgram> degreeProgramsToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        /*
        assertNotNull(degreeProgramDao);
        assertNotNull(degreeProgramDao.sql(""));
         */
    }

}
