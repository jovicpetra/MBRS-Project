# MBRS-Project
 MBRS University Project

 # Team Members
 1. Petra Jović
 2. Jovana Arsović
 3. Anđela Đurić

 # Running the Generator
1. Run Intellij as administrator
2. Build `PluginDevelopment` inside `MBRS-Project`
3. Run `PluginDevelopment`
4. MagicDraw should open, afterwards open `OurBeutySalon.mdzip` project and `Generate Code`
5. `OurBeautySalon` folder should be created with all backend, frontend and test files inside it

# Running the generated app
1. The database should be running on port `5432`
2. Inside `OurBeautySalon` run the app using `spring-boot:run`
3. The frontend should run on `localhost:8080`

# Running the tests
1. Run `mvn clean test` to run the backend tests
2. Run `npm i` and `npm test` inside `OurBeautySalon/frontend`
