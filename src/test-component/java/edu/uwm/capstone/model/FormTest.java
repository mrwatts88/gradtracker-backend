package edu.uwm.capstone.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class FormTest {

    @Test
    public void testFieldLength(){
        Form obj = new Form();
        obj.setFields(new ArrayList<Field>());
        Assert.assertEquals(0, obj.size());

        obj.setFields(new ArrayList<Field>(){{add(new Field());}});
        Assert.assertEquals(1, obj.size());
    }
}
