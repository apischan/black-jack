```
#!html

1. To run the project:
    - unix: "./gradlew jettyRunWar"
    - windows "gradlew.bat jettyRunWar"

2. To run the tests use command:
    - unix: "./gradlew test"
    - windows "gradlew.bat test"

3. Tokens {userId} and {roundId} is ID of user and ID of round from database respectively. There is a user with userId=1 and round with roundID=1. Replace this tokens with numbers.

4. To cancel the bet please use the following address: http://localhost:8080/black-jack-spring/game/{userId}/bet/{roundId}/cancel

5. To place bet use address: http://localhost:8080/black-jack-spring/game/{userId}/bet

6. To perform the "deal" use address: http://localhost:8080/black-jack-spring/game/{userId}/{roundId}/deal

7. To perform the "hit" use address: http://localhost:8080/black-jack-spring/game/{userId}/{roundId}/hit

8. To perform the "stand" use the address: http://localhost:8080/black-jack-spring/game/{userId}/{roundId}/stand

9. To see the result of game use the address: http://localhost:8080/black-jack-spring/game/{userId}/{roundId}/result



```
