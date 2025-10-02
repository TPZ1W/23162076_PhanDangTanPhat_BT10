@echo off
echo Starting Product GraphQL API...
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if errorlevel 1 (
    echo Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

echo Maven is available
echo.

REM Clean and compile
echo Cleaning and compiling project...
mvn clean compile

if errorlevel 1 (
    echo Failed to compile project
    pause
    exit /b 1
)

echo.
echo Starting Spring Boot application...
echo Application will be available at:
echo - GraphQL Endpoint: http://localhost:7002/graphql  
echo - GraphiQL UI: http://localhost:7002/graphiql
echo - REST API: http://localhost:7002/api/product
echo.

mvn spring-boot:run

pause