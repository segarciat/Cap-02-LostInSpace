package com.lostinspace.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {
    @Test
    public void Model_createModel_shouldLoadAllObjects_withoutThrowingException() {
        try {
            new Model();
        } catch(RuntimeException e) {
            fail("Was unable to load all of the assets for the game");
        }
    }
}