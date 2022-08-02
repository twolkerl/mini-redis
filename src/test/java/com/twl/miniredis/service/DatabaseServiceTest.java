package com.twl.miniredis.service;

import com.twl.miniredis.exception.NonNumericValueException;
import com.twl.miniredis.exception.NotFoundException;
import com.twl.miniredis.repository.DatabaseRepository;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseServiceTest {

    public static final String TEST_KEY = "test";
    public static final String TEST_KEY_SET = "test_zset";
    public static final String TEST_KEY_SET2 = "test_zset2";
    public static final String TEST_KEY_SET3 = "test_zset3";
    public static final String TEST_KEY_SET4 = "test_zset4";
    public static final String TEST_KEY_SET5 = "test_zset5";
    public static final String TEST_KEY_SET6 = "test_zset6";
    public static final String TEST_KEY_SET7 = "test_zset7";
    public static final String TEST_STRING_VALUE = "test_value";
    public static final Integer TEST_INTEGER_VALUE_ONE = 1;
    public static final Integer TEST_INTEGER_VALUE_TWO = 2;
    public static final String TEST_KEY_2 = "test2";
    public static final String STRING_VALUE_1 = "1";
    public static final String MEMBER_1 = "member1";
    public static final String STRING_VALUE_2 = "2";
    public static final String MEMBER_2 = "member2";
    public static final String KEY_DOES_NOT_EXIST_IN_DATABASE = "(nil) Key does not exist in database.";
    public static final String MEMBER_NOT_FOUND = "(nil) Member not found in zset.";
    public static final String COULD_NOT_FIND_ANY_RESULTS = "Could not find any results.";

    @InjectMocks
    private DatabaseService service;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private DatabaseRepository repository;

    @Test
    public void shouldSuccess_setKeyValue() {
        try {
            String actual = service.setKeyValue(TEST_KEY, TEST_STRING_VALUE, null);
            assertEquals(TEST_STRING_VALUE, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }


    @Test
    public void shouldFail_getKey_whenKeyDoesNotExist() {
        try {
            NotFoundException e = assertThrows(NotFoundException.class, () -> service.getStringValue(TEST_STRING_VALUE));
            assertEquals(KEY_DOES_NOT_EXIST_IN_DATABASE, e.getMessage());
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_del() {
        try {
            service.setKeyValue(TEST_KEY, TEST_STRING_VALUE, null);
            service.del(TEST_KEY, TEST_KEY_2);
            NotFoundException e = assertThrows(NotFoundException.class, () -> service.getStringValue(TEST_STRING_VALUE));
            assertEquals(KEY_DOES_NOT_EXIST_IN_DATABASE, e.getMessage());
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_dbsize_whenNotEmpty() {
        try {
            service.setKeyValue(TEST_KEY, TEST_STRING_VALUE, null);
            Integer actual = service.dbsize();
            assertTrue(actual > 0);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_incr_whenNumericValueStored() {
        try {
            service.setKeyValue(TEST_KEY, TEST_INTEGER_VALUE_ONE, null);
            String actual = service.incr(TEST_KEY);
            assertEquals(STRING_VALUE_2, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldFail_incr_whenNonNumericValueStored() {
        try {
            service.setKeyValue(TEST_KEY, TEST_STRING_VALUE, null);
            Exception e = assertThrows(NonNumericValueException.class, () -> service.incr(TEST_KEY));
            assertEquals(NonNumericValueException.class, e.getClass());
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_zadd_whenEvenArgs() {
        try {
            Integer actual = service.zadd(TEST_KEY_SET, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2, MEMBER_2);
            assertEquals(TEST_INTEGER_VALUE_TWO, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_zadd_whenOddArgs() {
        try {
            Integer actual = service.zadd(TEST_KEY_SET7, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2);
            assertEquals(TEST_INTEGER_VALUE_ONE, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_zcard() {
        try {
            service.zadd(TEST_KEY_SET6, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2, MEMBER_2);
            Integer actual = service.zcard(TEST_KEY_SET6);
            assertEquals(TEST_INTEGER_VALUE_TWO, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldFail_zrank_whenKeyDoesNotExist() {
        try {
            Exception e = assertThrows(NotFoundException.class, () -> service.zrank(TEST_KEY, MEMBER_1));
            assertEquals(NotFoundException.class, e.getClass());
            assertEquals(KEY_DOES_NOT_EXIST_IN_DATABASE, e.getMessage());
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_zrank_whenKeyAndMemberExists() {
        try {
            service.zadd(TEST_KEY_SET2, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2, MEMBER_2);
            Integer actual = service.zrank(TEST_KEY_SET2, MEMBER_2);
            assertEquals(TEST_INTEGER_VALUE_ONE, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldFail_zrank_whenMemberDoesNotExist() {
        try {
            service.zadd(TEST_KEY_SET3, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2);
            Exception e = assertThrows(NotFoundException.class, () -> service.zrank(TEST_KEY_SET3, MEMBER_2));
            assertEquals(NotFoundException.class, e.getClass());
            assertEquals(MEMBER_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_zrange_whenValidSet() {
        try {
            service.zadd(TEST_KEY_SET4, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2, MEMBER_2);

            LinkedHashMap<String, Double> actual = service.zrange(TEST_KEY_SET4, 0, -1);

            LinkedHashMap<String, Double> expected = new LinkedHashMap<>();
            expected.put(MEMBER_1, Double.valueOf(STRING_VALUE_1));
            expected.put(MEMBER_2, Double.valueOf(STRING_VALUE_2));

            assertEquals(expected, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldSuccess_zrange_whenStopValueIsGreaterThanLastIndex() {
        try {
            service.zadd(TEST_KEY, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2, MEMBER_2);

            LinkedHashMap<String, Double> actual = service.zrange(TEST_KEY, 0, 10);

            LinkedHashMap<String, Double> expected = new LinkedHashMap<>();
            expected.put(MEMBER_1, Double.valueOf(STRING_VALUE_1));
            expected.put(MEMBER_2, Double.valueOf(STRING_VALUE_2));

            assertEquals(expected, actual);
        } catch (Exception e) {
            TestCase.fail();
        }
    }

    @Test
    public void shouldFail_zrange_whenStartGreaterThanStop() {
        try {
            service.zadd(TEST_KEY_SET5, STRING_VALUE_1, MEMBER_1, STRING_VALUE_2, MEMBER_2);

            Exception e = assertThrows(NotFoundException.class, () -> service.zrange(TEST_KEY_SET5, 10, 1));

            assertEquals(NotFoundException.class, e.getClass());
            assertEquals(COULD_NOT_FIND_ANY_RESULTS, e.getMessage());
        } catch (Exception e) {
            TestCase.fail();
        }
    }
}