
## Manager API Testing Commands

### 1. Create Managers

```bash
# Create Manager 1 - Admin Manager
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR001",
    "name": "Rahman Karim",
    "email": "rahman.karim@example.com",
    "phoneNumber": "01710000001",
    "address": "House 12, Road 5, Banani, Dhaka",
    "department": "ADMIN",
    "designation": "Senior Manager",
    "password": "password123"
  }'
```

```bash
# Create Manager 2 - Operations Manager
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR002",
    "name": "Fatema Begum",
    "email": "fatema.begum@example.com",
    "phoneNumber": "01710000002",
    "address": "House 5, Road 12, Gulshan, Dhaka",
    "department": "OPERATIONS",
    "designation": "Operations Manager",
    "password": "password123"
  }'
```

```bash
# Create Manager 3 - Route Manager
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR003",
    "name": "Hasan Mahmud",
    "email": "hasan.mahmud@example.com",
    "phoneNumber": "01710000003",
    "address": "House 8, Road 3, Dhanmondi, Dhaka",
    "department": "ROUTES",
    "designation": "Route Manager",
    "password": "password123"
  }'
```

```bash
# Create Manager 4 - Fleet Manager
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR004",
    "name": "Shahidul Islam",
    "email": "shahidul.islam@example.com",
    "phoneNumber": "01710000004",
    "address": "House 15, Road 7, Mirpur, Dhaka",
    "department": "FLEET",
    "designation": "Fleet Manager",
    "password": "password123"
  }'
```

```bash
# Create Manager 5 - Customer Service Manager
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR005",
    "name": "Nasrin Akter",
    "email": "nasrin.akter@example.com",
    "phoneNumber": "01710000005",
    "address": "House 20, Road 10, Uttara, Dhaka",
    "department": "CUSTOMER_SERVICE",
    "designation": "CS Manager",
    "password": "password123"
  }'
```

### 2. Get All Managers
```bash
curl -X GET http://localhost:8080/api/v1/managers
```

### 3. Get Manager by ID
```bash
curl -X GET http://localhost:8080/api/v1/managers/1
curl -X GET http://localhost:8080/api/v1/managers/2
curl -X GET http://localhost:8080/api/v1/managers/3
```

### 4. Get Manager by Email
```bash
curl -X GET http://localhost:8080/api/v1/managers/email/rahman.karim@example.com
curl -X GET http://localhost:8080/api/v1/managers/email/fatema.begum@example.com
```

### 5. Get Manager by Manager ID
```bash
curl -X GET http://localhost:8080/api/v1/managers/manager-id/MGR001
curl -X GET http://localhost:8080/api/v1/managers/manager-id/MGR002
curl -X GET http://localhost:8080/api/v1/managers/manager-id/MGR003
```

### 6. Get Manager by Phone
```bash
curl -X GET http://localhost:8080/api/v1/managers/phone/01710000001
curl -X GET http://localhost:8080/api/v1/managers/phone/01710000002
```

### 7. Get Managers by Department
```bash
# Get ADMIN department managers
curl -X GET http://localhost:8080/api/v1/managers/department/ADMIN

# Get OPERATIONS department managers
curl -X GET http://localhost:8080/api/v1/managers/department/OPERATIONS

# Get ROUTES department managers
curl -X GET http://localhost:8080/api/v1/managers/department/ROUTES
```

### 8. Update Manager (Full Update - PUT)

```bash
# Update Manager 1
curl -X PUT http://localhost:8080/api/v1/managers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rahman Karim Updated",
    "email": "rahman.karim.updated@example.com",
    "phoneNumber": "01720000001",
    "address": "House 12, Road 5, Banani, Dhaka - Updated",
    "department": "ADMIN",
    "designation": "General Manager"
  }'
```

### 9. Partial Update (PATCH)

```bash
# Update only name
curl -X PATCH http://localhost:8080/api/v1/managers/2 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fatema Begum Updated"
  }'

# Update only department and designation
curl -X PATCH http://localhost:8080/api/v1/managers/2 \
  -H "Content-Type: application/json" \
  -d '{
    "department": "ADMIN",
    "designation": "Senior Operations Manager"
  }'

# Update only email
curl -X PATCH http://localhost:8080/api/v1/managers/3 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "hasan.mahmud.new@example.com"
  }'

# Update only phone
curl -X PATCH http://localhost:8080/api/v1/managers/3 \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "01720000003"
  }'

# Update address only
curl -X PATCH http://localhost:8080/api/v1/managers/4 \
  -H "Content-Type: application/json" \
  -d '{
    "address": "New Address, House 100, Road 20, Mirpur"
  }'

# Update multiple fields
curl -X PATCH http://localhost:8080/api/v1/managers/5 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nasrin Akter Senior",
    "designation": "Senior CS Manager",
    "department": "CUSTOMER_SERVICE"
  }'
```

### 10. Update Manager Status (Block/Unblock)

```bash
# Block Manager 3
curl -X PATCH http://localhost:8080/api/v1/managers/3/status \
  -H "Content-Type: application/json" \
  -d '{
    "blocked": true,
    "reason": "Violation of company policy"
  }'

# Unblock Manager 3
curl -X PATCH http://localhost:8080/api/v1/managers/3/status \
  -H "Content-Type: application/json" \
  -d '{
    "blocked": false,
    "reason": "Issue resolved"
  }'

# Block Manager 4
curl -X PATCH http://localhost:8080/api/v1/managers/4/status \
  -H "Content-Type: application/json" \
  -d '{
    "blocked": true,
    "reason": "On leave"
  }'
```

### 11. Change Password

```bash
# Change password for Manager 1
curl -X POST http://localhost:8080/api/v1/managers/1/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "password123",
    "newPassword": "newpassword456",
    "confirmPassword": "newpassword456"
  }'
```

### 12. Filter Managers

```bash
# Filter by name
curl -X POST http://localhost:8080/api/v1/managers/filter \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rahman"
  }'

# Filter by department
curl -X POST http://localhost:8080/api/v1/managers/filter \
  -H "Content-Type: application/json" \
  -d '{
    "department": "ADMIN"
  }'

# Filter by blocked status
curl -X POST http://localhost:8080/api/v1/managers/filter \
  -H "Content-Type: application/json" \
  -d '{
    "isBlocked": true
  }'

# Filter by email
curl -X POST http://localhost:8080/api/v1/managers/filter \
  -H "Content-Type: application/json" \
  -d '{
    "email": "hasan"
  }'

# Filter by designation
curl -X POST http://localhost:8080/api/v1/managers/filter \
  -H "Content-Type: application/json" \
  -d '{
    "designation": "Manager"
  }'

# Complex filter (multiple criteria)
curl -X POST http://localhost:8080/api/v1/managers/filter \
  -H "Content-Type: application/json" \
  -d '{
    "department": "OPERATIONS",
    "isBlocked": false
  }'
```

### 13. Get Manager Statistics

```bash
# Overall manager statistics
curl -X GET http://localhost:8080/api/v1/managers/statistics
```

### 14. Delete a Manager

```bash
# Delete Manager 5
curl -X DELETE http://localhost:8080/api/v1/managers/5

# Verify deletion (should return 404)
curl -X GET http://localhost:8080/api/v1/managers/5
```

### 15. Test Error Cases

```bash
# Try to create manager with duplicate manager ID (should fail)
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR001",
    "name": "Duplicate Manager",
    "email": "duplicate@example.com",
    "phoneNumber": "01799999999",
    "department": "ADMIN",
    "designation": "Manager",
    "password": "password123"
  }'

# Try to create manager with duplicate email (should fail)
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR006",
    "name": "Duplicate Email",
    "email": "rahman.karim@example.com",
    "phoneNumber": "01788888888",
    "department": "ADMIN",
    "designation": "Manager",
    "password": "password123"
  }'

# Try to create manager with duplicate phone (should fail)
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR007",
    "name": "Duplicate Phone",
    "email": "duplicate.phone@example.com",
    "phoneNumber": "01710000001",
    "department": "ADMIN",
    "designation": "Manager",
    "password": "password123"
  }'

# Try to create manager with invalid email (should fail)
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR008",
    "name": "Invalid Email",
    "email": "invalid-email",
    "phoneNumber": "01777777777",
    "department": "ADMIN",
    "designation": "Manager",
    "password": "password123"
  }'

# Try to create manager with short password (should fail)
curl -X POST http://localhost:8080/api/v1/managers \
  -H "Content-Type: application/json" \
  -d '{
    "managerId": "MGR009",
    "name": "Short Password",
    "email": "short.pass@example.com",
    "phoneNumber": "01766666666",
    "department": "ADMIN",
    "designation": "Manager",
    "password": "123"
  }'

# Try to get non-existent manager (should return 404)
curl -X GET http://localhost:8080/api/v1/managers/999

# Try to update non-existent manager (should return 404)
curl -X PUT http://localhost:8080/api/v1/managers/999 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Non Existent",
    "email": "nonexistent@example.com",
    "phoneNumber": "01700000000",
    "department": "ADMIN",
    "designation": "Manager"
  }'

# Try to change password with wrong current password (should fail)
curl -X POST http://localhost:8080/api/v1/managers/1/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "wrongpassword",
    "newPassword": "newpass123",
    "confirmPassword": "newpass123"
  }'

# Try to change password with mismatched confirmation (should fail)
curl -X POST http://localhost:8080/api/v1/managers/1/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "password123",
    "newPassword": "newpass123",
    "confirmPassword": "different123"
  }'

# Try to delete non-existent manager (should return 404)
curl -X DELETE http://localhost:8080/api/v1/managers/999
```

### 16. Complete Test Flow (Run in Order)

```bash
# 1. Create test managers
echo "Creating test managers..."
curl -X POST http://localhost:8080/api/v1/managers -H "Content-Type: application/json" -d '{"managerId":"TEST001","name":"Test Manager 1","email":"test1@example.com","phoneNumber":"01790000001","address":"Test Address 1","department":"TEST","designation":"Tester","password":"password123"}'
curl -X POST http://localhost:8080/api/v1/managers -H "Content-Type: application/json" -d '{"managerId":"TEST002","name":"Test Manager 2","email":"test2@example.com","phoneNumber":"01790000002","address":"Test Address 2","department":"TEST","designation":"Senior Tester","password":"password123"}'
curl -X POST http://localhost:8080/api/v1/managers -H "Content-Type: application/json" -d '{"managerId":"TEST003","name":"Test Manager 3","email":"test3@example.com","phoneNumber":"01790000003","address":"Test Address 3","department":"DEV","designation":"Developer","password":"password123"}'

# 2. Get all managers
echo -e "\n\nGetting all managers..."
curl -X GET http://localhost:8080/api/v1/managers

# 3. Get manager by ID
echo -e "\n\nGetting manager by ID 1..."
curl -X GET http://localhost:8080/api/v1/managers/1

# 4. Get managers by department
echo -e "\n\nGetting TEST department managers..."
curl -X GET http://localhost:8080/api/v1/managers/department/TEST

# 5. Update manager status
echo -e "\n\nBlocking manager 2..."
curl -X PATCH http://localhost:8080/api/v1/managers/2/status -H "Content-Type: application/json" -d '{"blocked":true,"reason":"Test block"}'

# 6. Get statistics
echo -e "\n\nGetting manager statistics..."
curl -X GET http://localhost:8080/api/v1/managers/statistics

# 7. Filter active managers
echo -e "\n\nFiltering active managers..."
curl -X POST http://localhost:8080/api/v1/managers/filter -H "Content-Type: application/json" -d '{"isBlocked":false}'

# 8. Partial update
echo -e "\n\nUpdating manager 3 designation..."
curl -X PATCH http://localhost:8080/api/v1/managers/3 -H "Content-Type: application/json" -d '{"designation":"Lead Developer"}'

# 9. Delete test manager
echo -e "\n\nDeleting manager 3..."
curl -X DELETE http://localhost:8080/api/v1/managers/3

# 10. Verify deletion
echo -e "\n\nVerifying deletion..."
curl -X GET http://localhost:8080/api/v1/managers
```

