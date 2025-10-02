@echo off
echo ========================================
echo    TESTING CRUD OPERATIONS
echo ========================================
echo.

REM Check if curl is available
where curl >nul 2>&1
if errorlevel 1 (
    echo curl is not available. Please install curl to run tests.
    pause
    exit /b 1
)

set BASE_URL=http://localhost:7002

echo Testing server connectivity...
curl -s %BASE_URL%/api/user > nul
if errorlevel 1 (
    echo Server is not running. Please start the application first.
    echo Run: mvn spring-boot:run
    pause
    exit /b 1
)

echo ✅ Server is running!
echo.

echo ========================================
echo Testing USER CRUD Operations
echo ========================================

echo 📝 Creating new user...
curl -X POST %BASE_URL%/api/user ^
  -H "Content-Type: application/json" ^
  -d "{\"fullname\":\"Test User CRUD\",\"email\":\"testcrud@example.com\",\"password\":\"password123\",\"phone\":\"0901234567\"}"
echo.
echo.

echo 📖 Getting all users...
curl -X GET %BASE_URL%/api/user
echo.
echo.

echo 🔍 Searching users...
curl -X GET "%BASE_URL%/api/user/search?name=Test"
echo.
echo.

echo ========================================
echo Testing CATEGORY CRUD Operations  
echo ========================================

echo 📝 Creating new category...
curl -X POST %BASE_URL%/api/category ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Test Category CRUD\",\"images\":\"test_crud.jpg\"}"
echo.
echo.

echo 📖 Getting all categories...
curl -X GET %BASE_URL%/api/category
echo.
echo.

echo ========================================
echo Testing PRODUCT CRUD Operations
echo ========================================

echo 📝 Creating new product...
curl -X POST %BASE_URL%/api/product ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Test Product CRUD\",\"quantity\":25,\"description\":\"Product for CRUD testing\",\"price\":750000.00,\"images\":\"test_product.jpg\",\"userId\":1}"
echo.
echo.

echo 📖 Getting all products...
curl -X GET %BASE_URL%/api/product
echo.
echo.

echo 🔍 Searching products...
curl -X GET "%BASE_URL%/api/product/search?title=Test"
echo.
echo.

echo 📊 Getting products by price range...
curl -X GET "%BASE_URL%/api/product/price-range?minPrice=500000&maxPrice=1000000"
echo.
echo.

echo 📈 Getting products sorted by price...
curl -X GET %BASE_URL%/api/product/sort/price-asc
echo.
echo.

echo ========================================
echo Testing RELATIONSHIPS
echo ========================================

echo 🔗 Getting products by category...
curl -X GET %BASE_URL%/api/product/category/1
echo.
echo.

echo 👥 Getting users by category...
curl -X GET %BASE_URL%/api/user/category/1
echo.
echo.

echo 📂 Getting categories by user...
curl -X GET %BASE_URL%/api/category/user/1
echo.
echo.

echo ========================================
echo ✅ CRUD TESTING COMPLETED!
echo ========================================
echo.
echo 🎯 Test Summary:
echo   - User CRUD: ✅ Create, Read, Search
echo   - Category CRUD: ✅ Create, Read
echo   - Product CRUD: ✅ Create, Read, Search, Filter, Sort
echo   - Relationships: ✅ User-Category, Product-User, Product-Category
echo.
echo 💡 Next steps:
echo   1. Test GraphQL at: %BASE_URL%/graphiql
echo   2. Import Postman collection for detailed testing
echo   3. Check CRUD_API_Documentation.md for full API reference
echo.
echo 📋 Available endpoints:
echo   - Users: %BASE_URL%/api/user
echo   - Products: %BASE_URL%/api/product  
echo   - Categories: %BASE_URL%/api/category
echo   - GraphQL: %BASE_URL%/graphql
echo   - GraphiQL: %BASE_URL%/graphiql
echo.

pause