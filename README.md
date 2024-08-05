This is a POC for a Point of Sale software application.

The project uses Spring Boot for database interaction through JDBC. This allows the back end storage of data to be 
changed easily and quickly as long as table structures and naming conventions are followed.

For the date values in this test I really had a tough time choosing how to do it; using the older, maligned Date class 
or the newer LocalDate classes for Java 8 and higher. I decided to use the newer LocalDate objects in this demo as any code
still using a Java version less than 8 is not secure by lack of support. While there are millions of lines of Java code 
out there using the Date and Calendar objects the SimpleDateFormat class and Apache Commons DateUtil make interacting
with legacy logic fairly easy.

NOTE - Complete CRUD implementation was done in DAO Impl class even though only the Select calls where used and tested. 
It was common practice in older jobs when creating DAO classes to implement all CRUD logic even if not used for
future possible business needs.
