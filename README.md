# Mini-Redis

API that implements a subset of Redis functions, storing data in memory.

## Frameworks, plugins, dependencies e etc.

- SpringBoot
- Spring Web
- Lombok* (*Requires annotation processing*)
- JUnit5

## The following Redis' commands are available:

**Note**: It is possible to import the Postman collection for testing from the json located in the folder `postman-collection`

###  SET key value

#### /SET

- (PUT)
- (RequestParam) key
- (RequestParam) value
- (RequestParam - optional) exSeconds

Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type. Any previous time to live associated with the key is discarded on successful SET operation.

#### /GET/{key}

- (GET)
- (PathVariable) key

Get the value of key. If the key does not exist the special value nil is returned. An error is returned if the value stored at key is not a string, because GET only handles string values.

#### /DEL/{key}

- (DEL)
- (PathVariable) key

Removes the specified keys. A key is ignored if it does not exist.

#### /DBSIZE

- (GET)

Return the number of keys in the currently-selected database.

#### /INCR/{key}

- (PUT)
- (PathVariable) key

Increments the number stored at key by one.

#### /ZADD/{key}

- (PUT)
- (PathVariable) key
- (RequestParam - score and members separeted by comma) 1,member1,2,member2 ...

Adds all the specified members with the specified scores to the sorted set stored at key. It is possible to specify multiple score / member pairs. If a specified member is already a member of the sorted set, the score is updated and the element reinserted at the right position to ensure the correct ordering.

#### /ZCARD/{key}

- (GET)
- (PathVariable) key

Returns the sorted set cardinality (number of elements) of the sorted set stored at key.

#### /ZRANK/{key}

- (GET)
- (PathVariable) key
- (RequestParam) member

Returns the rank of member in the sorted set stored at key, with the scores ordered from low to high. The rank (or index) is 0-based, which means that the member with the lowest score has rank 0.

#### /ZRANGE/{key}

- (GET)
- (PathVariable) key
- (RequestParam) start
- (RequestParam) stop

Returns the specified range of elements in the sorted set stored at key.