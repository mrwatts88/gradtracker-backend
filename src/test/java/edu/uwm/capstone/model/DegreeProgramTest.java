package edu.uwm.capstone.model;

import edu.uwm.capstone.util.TestDataUtility;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

public class DegreeProgramTest {

    @Test
    public void testGetDegreeProgramStateById() {
        DegreeProgram dp = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(5, 15));
        long i = 0;
        for (DegreeProgramState dps : dp) {
            dps.setId(i);
            Assert.assertEquals(dps, dp.getDegreeProgramStateById(i++));
        }
    }

    @Test
    public void testGetInitialState() {
        DegreeProgram dp = TestDataUtility.degreeProgramWithTestValues(TestDataUtility.randomInt(5, 15));
        Set<DegreeProgramState> initialStateSet = dp.getDegreeProgramStates().stream().filter(DegreeProgramState::isInitial).collect(Collectors.toSet());
        Assert.assertEquals(1, initialStateSet.size());
        Assert.assertTrue(initialStateSet.contains(dp.initialState()));
    }
}
