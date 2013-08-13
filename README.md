RepeatedTaskFilter
==================

Repeated asynchronous task filter for event driven projects

This jar contains an utiliti class thata manage repeted task identified
by a key submitted repeatedly in a event driven architecture.

It execute immediatly the first request for a key.
If, while the first action is running, came others request, the second is enqueued
and the subsequents are dropped. When current action finishes its work, the enqueued one were dequeued
end so on...

It's no guarantee that exactly the enqueued action is executed, but is not in the purpose
of this librery, because for a key the action must be the SAME, so is not relevant which
of the one is executed.

The library guarantees that:

1) 2 action of the same key are never executed concurrently

2) 2 or more action during the execution were collapsed in one and executed
at the end of current task processed.
