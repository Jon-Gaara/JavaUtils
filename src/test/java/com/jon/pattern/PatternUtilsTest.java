package com.jon.pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PatternUtilsTest {

    @Test
    public void validIdCardPasses() {
        assertTrue(PatternUtils.idCardLastPattern("11010519491231002X"));
    }

    @Test
    public void tooLongIdCardReturnsFalseWithoutException() {
        // 修复前会抛 ArrayIndexOutOfBoundsException
        assertFalse(PatternUtils.idCardLastPattern("11010519491231002X9"));
    }

    @Test
    public void nonDigitIdCardReturnsFalseWithoutException() {
        // 修复前会抛 NumberFormatException
        assertFalse(PatternUtils.idCardLastPattern("abcdefghijklmnopqr"));
    }

    @Test
    public void wrongCheckDigitFails() {
        assertFalse(PatternUtils.idCardLastPattern("110105194912310021"));
    }
}
