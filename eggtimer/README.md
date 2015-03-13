# EggTimer

Simple microservices to provide a caller with a synchronous and asynchronous delay functions. HTTP RESTful service to do a synchronous. JMS service to do asynchronous. An example of an async client is in systemtests/org/cucina/eggtimer/ScheduleClient. 

Also provides a function to test whether a date is before the system date. Useful for a clustered application where nodes could be out of sync, or even in a different timezones. An example of a client is in systemtests/org/cucina/eggtimer/CheckDateClient. 
