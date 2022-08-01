package com.twl.miniredis.repository;

import com.twl.miniredis.db.Database;
import com.twl.miniredis.exception.BusinessException;
import com.twl.miniredis.exception.NonNumericValueException;
import com.twl.miniredis.model.dto.ScoreMember;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Method documentations from <a href="https://redis.io/commands/">Redis commands page</a>.
 *
 * @author Tiago Wolker
 */
@Repository
@Log4j2
public class DatabaseRepository {

    /**
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     * Any previous time to live associated with the key is discarded on successful SET operation.
     * @param key
     * @param value
     */
    public void setStringValue(String key, Object value) {
        Database.getValues().put(key, value);
    }

    /**
     * Get the value of key. If the key does not exist the special value nil is returned. An error is returned if the
     * value stored at key is not a string, because GET only handles string values.
     * @param key
     * @return String value stored in given key.
     */
    public String getStringValue(String key) throws BusinessException {
        Object value = Database.getValues().get(key);
        if (value != null) {
            if (String.class.equals(value.getClass())) {
                return value.toString();
            } else {
                try {
                    Double num = Double.valueOf(value.toString());
                    return num.toString();
                } catch (NumberFormatException e) {
                    String message = "Value can`t be resolved as a string. If you`re trying to get a zset, try using ZRANGE instead.";
                    log.error(message);
                    throw new BusinessException(message);
                }
            }
        }
        return null; // TODO - verificar possivel retorno de exception
    }

    // TODO - SET key value EX seconds

    /**
     * Removes the specified keys. A key is ignored if it does not exist.
     * @param keys
     */
    public int del(String... keys) {
        int deletions = 0;
        for (String key : keys) {
            Object value = Database.getValues().get(key);
            if (value != null) {
                Database.getValues().remove(key);
                deletions++; //TODO caso seja um `zset` incrementar quantos registros tinham neste
            }
        }
        return deletions;
    }

    /**
     * @return Return the number of keys in the currently-selected database.
     */
    public Integer dbsize() {
        return Database.getValues().size();
    }

    /**
     * Increments the number stored at key by one. If the key does not exist, it is set to 0 before performing the
     * operation. An error is returned if the key contains a value of the wrong type or contains a string that can not
     * be represented as integer. This operation is limited to 64 bit signed integers.
     * <br/><br/>
     * <b>Note:</b> this is a string operation because Redis does not have a dedicated integer type. The string stored at the
     * key is interpreted as a base-10 <b>64 bit signed integer</b> to execute the operation.
     * <br/><br/>
     * Redis stores integers in their integer representation, so for string values that actually hold an integer, there
     * is no overhead for storing the string representation of the integer.
     * @param key
     */
    public String incr(String key) throws NonNumericValueException, BusinessException {
        Object value = Database.getValues().get(key);
        try {
            int numericValue = Integer.parseInt(value.toString());
            numericValue++;
            Database.getValues().put(key, String.valueOf(numericValue));
            return this.getStringValue(key);
        } catch (NumberFormatException e) {
            log.error(e);
            throw new NonNumericValueException(e);
        }
    }

    /**
     * Adds all the specified members with the specified scores to the sorted set stored at key. It is possible to
     * specify multiple score / member pairs. If a specified member is already a member of the sorted set, the score is
     * updated and the element reinserted at the right position to ensure the correct ordering.
     * <br/><br/>
     * If key does not exist, a new sorted set with the specified members as sole members is created, like if the
     * sorted set was empty. If the key exists but does not hold a sorted set, an error is returned.
     * <br/><br/>
     * The score values should be the string representation of a double precision floating point number. +inf and -inf
     * values are valid values as well.
     *
     * @param key
     * @param scoreMember A {@link TreeMap} where its key represents the member and value represents the score.
     */
    public void zadd(String key, TreeMap<String, Double> scoreMember) {
        Database.getValues().put(key, scoreMember);
//        Collections.sort(new ArrayList<>(Database.getZset().get(key).entrySet()), new TreeMapValueKeyComparator<>());
    }

    public Object getObject(String key) {
        return Database.getValues().get(key);
    }

    /**
     * Returns the sorted set cardinality (number of elements) of the sorted set stored at key.
     *
     * @param key
     * @return the cardinality (number of elements) of the sorted set, or 0 if key does not exist.
     */
    public Integer zcard(String key) throws BusinessException {
        Object value = Database.getValues().get(key);
        if (value != null) {
            if (LinkedHashMap.class.equals(value.getClass())) {

                return ((LinkedHashMap) value).size();
            } else {

                String message = "Value stored in given key does not represent a zset.";
                log.error(message);
                throw new BusinessException(message);
            }
        } else {
            return 0;
        }
    }

    /**
     * @param key
     * @param member {@link ScoreMember#getMember()}
     * @return Returns the rank of member in the sorted set stored at key, with the scores ordered from low to high.
     * The rank (or index) is 0-based, which means that the member with the lowest score has rank 0.
     */
    public Integer zrank(String key, String member) {
//        List<ScoreMember> scoreMemberList = Database.getZset().get(key);
//        return IntStream.range(0, scoreMemberList.size())
//                .filter(scoreMember -> member.equals(scoreMemberList.get(scoreMember).getMember()))
//                .findFirst()
//                .orElse(-1);// TODO refinar retorno
        return null;//TODO
    }

    /**
     * Returns the specified range of elements in the sorted set stored at key. <br/><br/>
     * By default, the command performs an index range query. The <b>start</b> and <b>stop</b> arguments represent zero-based
     * indexes, where 0 is the first element, 1 is the next element, and so on. These arguments specify an inclusive
     * range, so for example, ZRANGE myzset 0 1 will return both the first and the second element of the sorted set.
     * <br/><br/>
     * The indexes can also be negative numbers indicating offsets from the end of the sorted set, with -1 being the
     * last element of the sorted set, -2 the penultimate element, and so on.
     * <br/><br/>
     * Out of range indexes do not produce an error.
     * <br/><br/>
     * If <b>start</b> is greater than either the end index of the sorted set or <b>stop</b>, an empty list is returned.
     * <br/><br/>
     * If <b>stop</b> is greater than the end index of the sorted set, Redis will use the last element of the sorted set.
     * @param key
     * @param start Starting index range
     * @param stop Ending index range
     * @return Returns the specified range of elements in the sorted set stored at <b>key</b>.
     */
    public List<ScoreMember> zrange(String key, int start, int stop) {
//        List<ScoreMember> zset = Database.getZset().get(key);
//        if (zset == null || zset.isEmpty()) {
//            return new ArrayList<>();
//        }
//        int size = zset.size();
//        if (start > size && start > stop) {
//            return new ArrayList<>();
//        }
//        int lastElement = stop;
//        if (lastElement > size) {
//            lastElement = size - 1;
//        } else if (lastElement < 0) {
//            lastElement = size - 1 + stop;
//            if (lastElement < 0 || lastElement < start) {
//                return new ArrayList<>();
//            }
//        }
//        return IntStream.range(start, lastElement)
//                .mapToObj(zset::get)
//                .collect(Collectors.toList());
        return null;//TODO
    }

}
