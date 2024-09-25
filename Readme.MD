### Spring security related:
- All unhandled exceptions are 403: after adding spring security, no matter what exception 
happens, spring security is returning 403 for everything.
although i can see the errro in terminal. the only 
solution is using an exception handler to intercept 
and return a customized error. although i am using 
spring validation plugin, yet all 400,422 are wrapped with 
403 by spring security.