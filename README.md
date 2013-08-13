RepeatedTaskFilter
==================

### Repeated asynchronous task filter for event driven projects.

This jar contains an utility class that manages repeated tasks identified
by a key submitted repeatedly in a event driven architecture.

The filter immediately executes the first request for a given key.

If, while the first task is running, others request should arrive,
the second task is enqueued and subsequent ones are dropped. 
When the current task finishes its work, the enqueued one gets dequeued
and so on...

There's no guarantee that exactly the enqueued task gets executed,
this is not the purpose of the library, because for a given key
the task must be the __same__, so which one gets executed isn't 
really relevant.

The library guarantees that:

* 2 actions with the same key are never executed concurrently
* 2 or more actions are collapsed into one and executed at the
  end of the current task being processed.
