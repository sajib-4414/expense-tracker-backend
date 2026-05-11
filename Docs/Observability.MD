The level hierarchy from lowest to highest:
TRACE → DEBUG → INFO → WARN → ERROR
When you set a level, you see that level and everything above it.
So if you set level="INFO" you see: INFO, WARN, ERROR
If you set level="DEBUG" you see: DEBUG, INFO, WARN, ERROR

In your code, when you write:
log.debug("INCOMING payload={}",  payload);  // only shows if level is DEBUG or lower
log.info("RESPONSE status={}",    status);   // shows if level is INFO or lower
log.warn("RESPONSE error ...",    ...);      // shows if level is WARN or lower
log.error("UNHANDLED_ERROR ...",  ...);      // always shows