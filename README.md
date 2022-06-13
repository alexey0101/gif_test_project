# gif_test_project
1. Set your API keys for https://openexchangerates.org/ and https://giphy.com/ in application.properties
2. Build: ./gradlew build
3. Run: java -jar build/libs/test-0.0.1-SNAPSHOT.jar

You can also run application in Docker container:
1. Create Docker image: docker build -t gifapp .
2. Run container: docker run -p 8080:8080 gifapp
