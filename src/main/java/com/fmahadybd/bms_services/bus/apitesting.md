

## Bus API Testing Commands

### 1. Create Buses

```bash
# Create Bus 1 - Green Line (AC Bus) assigned to Route 1
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green Line AC",
    "busNumber": "BUS-001",
    "status": "ACTIVE",
    "driverName": "Karim Khan",
    "helperName": "Rahim Ahmed",
    "driverPhone": "01712345678",
    "helperPhone": "01812345678",
    "routeId": 1
  }'
```

```bash
# Create Bus 2 - Hanif Express assigned to Route 1
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Hanif Express",
    "busNumber": "BUS-002",
    "status": "ACTIVE",
    "driverName": "Shahid Ullah",
    "helperName": "Jahid Hasan",
    "driverPhone": "01912345678",
    "helperPhone": "01612345678",
    "routeId": 1
  }'
```

```bash
# Create Bus 3 - Sohag Transport (On Trip) assigned to Route 2
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Sohag Transport",
    "busNumber": "BUS-003",
    "status": "ON_TRIP",
    "driverName": "Faruk Ahmed",
    "helperName": "Shofiq Islam",
    "driverPhone": "01512345678",
    "helperPhone": "01412345678",
    "routeId": 2
  }'
```

```bash
# Create Bus 4 - Shyamoli Paribahan (In Maintenance) - No Route
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Shyamoli Paribahan",
    "busNumber": "BUS-004",
    "status": "MAINTENANCE",
    "driverName": "Abul Kalam",
    "helperName": "Babul Miah",
    "driverPhone": "01312345678",
    "helperPhone": "01212345678"
  }'
```

```bash
# Create Bus 5 - Eagle Transport (Inactive)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Eagle Transport",
    "busNumber": "BUS-005",
    "status": "INACTIVE",
    "driverName": "Mostafa Kamal",
    "helperName": "Shahin Alam",
    "driverPhone": "01112345678",
    "helperPhone": "01012345678"
  }'
```

```bash
# Create Bus 6 - BRTC Double Decker (Out of Service)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "BRTC Double Decker",
    "busNumber": "BUS-006",
    "status": "OUT_OF_SERVICE",
    "driverName": "Harun Rashid",
    "helperName": "Mizanur Rahman",
    "driverPhone": "01987654321",
    "helperPhone": "01887654321"
  }'
```

### 2. Get All Buses
```bash
curl -X GET http://localhost:8080/api/v1/buses
```

### 3. Get Bus by ID
```bash
curl -X GET http://localhost:8080/api/v1/buses/1
curl -X GET http://localhost:8080/api/v1/buses/2
curl -X GET http://localhost:8080/api/v1/buses/3
```

### 4. Get Bus by Number
```bash
curl -X GET http://localhost:8080/api/v1/buses/number/BUS-001
curl -X GET http://localhost:8080/api/v1/buses/number/BUS-002
curl -X GET http://localhost:8080/api/v1/buses/number/BUS-003
```

### 5. Get Buses by Status
```bash
# Get ACTIVE buses
curl -X GET http://localhost:8080/api/v1/buses/status/ACTIVE

# Get INACTIVE buses
curl -X GET http://localhost:8080/api/v1/buses/status/INACTIVE

# Get MAINTENANCE buses
curl -X GET http://localhost:8080/api/v1/buses/status/MAINTENANCE

# Get ON_TRIP buses
curl -X GET http://localhost:8080/api/v1/buses/status/ON_TRIP

# Get OUT_OF_SERVICE buses
curl -X GET http://localhost:8080/api/v1/buses/status/OUT_OF_SERVICE
```

### 6. Get Buses by Route
```bash
# Get all buses for Route 1
curl -X GET http://localhost:8080/api/v1/buses/route/1

# Get all buses for Route 2
curl -X GET http://localhost:8080/api/v1/buses/route/2
```

### 7. Get Active Buses by Route
```bash
# Get active buses for Route 1
curl -X GET http://localhost:8080/api/v1/buses/route/1/active

# Get active buses for Route 2
curl -X GET http://localhost:8080/api/v1/buses/route/2/active
```

### 8. Get Available Buses
```bash
# Get all available buses (ACTIVE or ON_TRIP)
curl -X GET http://localhost:8080/api/v1/buses/available
```

### 9. Search Buses
```bash
# Search by driver name
curl -X GET "http://localhost:8080/api/v1/buses/search/driver?driverName=Karim"

# Search by bus name
curl -X GET "http://localhost:8080/api/v1/buses/search/name?busName=Green"
```

### 10. Update Bus (Full Update - PUT)
```bash
# Update Bus 1 with new information
curl -X PUT http://localhost:8080/api/v1/buses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green Line AC Premium",
    "busNumber": "BUS-001",
    "status": "ACTIVE",
    "driverName": "Karim Khan Updated",
    "helperName": "Rahim Ahmed Updated",
    "driverPhone": "01712345679",
    "helperPhone": "01812345679",
    "routeId": 1
  }'
```

### 11. Partial Update (PATCH)

```bash
# Update only bus name
curl -X PATCH http://localhost:8080/api/v1/buses/2 \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Hanif Express Premium"
  }'

# Update only driver information
curl -X PATCH http://localhost:8080/api/v1/buses/2 \
  -H "Content-Type: application/json" \
  -d '{
    "driverName": "New Driver Name",
    "driverPhone": "01711111111"
  }'

# Update only status
curl -X PATCH http://localhost:8080/api/v1/buses/3 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE"
  }'

# Update route assignment
curl -X PATCH http://localhost:8080/api/v1/buses/4 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 2
  }'

# Remove from route
curl -X PATCH http://localhost:8080/api/v1/buses/4 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": null
  }'

# Update multiple fields
curl -X PATCH http://localhost:8080/api/v1/buses/5 \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Eagle Transport Express",
    "status": "ACTIVE",
    "helperName": "New Helper"
  }'
```

### 12. Update Bus Status
```bash
# Set Bus 3 to MAINTENANCE
curl -X PATCH http://localhost:8080/api/v1/buses/3/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "MAINTENANCE",
    "reason": "Engine repair needed"
  }'

# Set Bus 4 to ACTIVE
curl -X PATCH http://localhost:8080/api/v1/buses/4/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE",
    "reason": "Maintenance completed"
  }'

# Set Bus 5 to ON_TRIP
curl -X PATCH http://localhost:8080/api/v1/buses/5/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ON_TRIP",
    "reason": "Started journey to Chittagong"
  }'
```

### 13. Assign Bus to Route
```bash
# Assign Bus 4 to Route 1
curl -X PATCH http://localhost:8080/api/v1/buses/4/assign-route/1

# Assign Bus 5 to Route 2
curl -X PATCH http://localhost:8080/api/v1/buses/5/assign-route/2
```

### 14. Remove Bus from Route
```bash
# Remove Bus 4 from its route
curl -X PATCH http://localhost:8080/api/v1/buses/4/remove-route

# Remove Bus 5 from its route
curl -X PATCH http://localhost:8080/api/v1/buses/5/remove-route
```

### 15. Filter Buses

```bash
# Filter by status only
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE"
  }'

# Filter by route only
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1
  }'

# Filter by bus name
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green"
  }'

# Filter by driver name
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "driverName": "Karim"
  }'

# Complex filter (multiple criteria)
curl -X POST http://localhost:8080/api/v1/buses/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "status": "ACTIVE",
    "busName": "Green"
  }'
```

### 16. Get Bus Statistics

```bash
# Overall bus statistics
curl -X GET http://localhost:8080/api/v1/buses/statistics
```

### 17. Get Bus Statistics by Route

```bash
# Statistics for Route 1
curl -X GET http://localhost:8080/api/v1/buses/statistics/route/1

# Statistics for Route 2
curl -X GET http://localhost:8080/api/v1/buses/statistics/route/2
```

### 18. Delete a Bus

```bash
# Delete Bus 6
curl -X DELETE http://localhost:8080/api/v1/buses/6

# Verify deletion (should return 404)
curl -X GET http://localhost:8080/api/v1/buses/6
```

### 19. Test Error Cases

```bash
# Try to create duplicate bus number (should fail with 409 Conflict)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Duplicate Bus",
    "busNumber": "BUS-001",
    "status": "ACTIVE",
    "driverName": "Test Driver",
    "helperName": "Test Helper",
    "driverPhone": "01700000000",
    "helperPhone": "01800000000"
  }'

# Try to create bus with invalid phone number (should fail validation)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Invalid Phone",
    "busNumber": "BUS-007",
    "status": "ACTIVE",
    "driverName": "Test Driver",
    "helperName": "Test Helper",
    "driverPhone": "invalid",
    "helperPhone": "01800000000"
  }'

# Try to get non-existent bus (should return 404)
curl -X GET http://localhost:8080/api/v1/buses/999

# Try to update non-existent bus (should return 404)
curl -X PUT http://localhost:8080/api/v1/buses/999 \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Non Existent",
    "busNumber": "BUS-999",
    "status": "ACTIVE",
    "driverName": "Test",
    "helperName": "Test",
    "driverPhone": "01700000000",
    "helperPhone": "01800000000"
  }'

# Try to assign bus to non-existent route (should return 404)
curl -X PATCH http://localhost:8080/api/v1/buses/1/assign-route/999

# Try to delete non-existent bus (should return 404)
curl -X DELETE http://localhost:8080/api/v1/buses/999
```

### 20. Complete Test Flow (Run in Order)

```bash
# 1. Create test buses
echo "Creating test buses..."
curl -X POST http://localhost:8080/api/v1/buses -H "Content-Type: application/json" -d '{"busName":"Test Bus 1","busNumber":"TEST-001","status":"ACTIVE","driverName":"Driver 1","helperName":"Helper 1","driverPhone":"01711111111","helperPhone":"01811111111","routeId":1}'
curl -X POST http://localhost:8080/api/v1/buses -H "Content-Type: application/json" -d '{"busName":"Test Bus 2","busNumber":"TEST-002","status":"ACTIVE","driverName":"Driver 2","helperName":"Helper 2","driverPhone":"01722222222","helperPhone":"01822222222","routeId":1}'
curl -X POST http://localhost:8080/api/v1/buses -H "Content-Type: application/json" -d '{"busName":"Test Bus 3","busNumber":"TEST-003","status":"MAINTENANCE","driverName":"Driver 3","helperName":"Helper 3","driverPhone":"01733333333","helperPhone":"01833333333"}'

# 2. Get all buses
echo -e "\n\nGetting all buses..."
curl -X GET http://localhost:8080/api/v1/buses

# 3. Get buses by route
echo -e "\n\nGetting buses for route 1..."
curl -X GET http://localhost:8080/api/v1/buses/route/1

# 4. Get available buses
echo -e "\n\nGetting available buses..."
curl -X GET http://localhost:8080/api/v1/buses/available

# 5. Update status
echo -e "\n\nUpdating bus 3 status to ACTIVE..."
curl -X PATCH http://localhost:8080/api/v1/buses/3/status -H "Content-Type: application/json" -d '{"status":"ACTIVE","reason":"Maintenance completed"}'

# 6. Get statistics
echo -e "\n\nGetting bus statistics..."
curl -X GET http://localhost:8080/api/v1/buses/statistics

# 7. Filter active buses
echo -e "\n\nFiltering active buses..."
curl -X POST http://localhost:8080/api/v1/buses/filter -H "Content-Type: application/json" -d '{"status":"ACTIVE"}'

# 8. Delete test bus
echo -e "\n\nDeleting bus 3..."
curl -X DELETE http://localhost:8080/api/v1/buses/3

# 9. Verify deletion
echo -e "\n\nVerifying deletion..."
curl -X GET http://localhost:8080/api/v1/buses
```

