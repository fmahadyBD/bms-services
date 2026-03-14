
## Updated Bus Slot API Testing Commands with Bus Assignment

### Prerequisites
First, make sure you have some buses created:
```bash
# Create a bus for testing
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Volvo AC Bus",
    "busNumber": "DHAKA-METRO-1234",
    "status": "ACTIVE",
    "driverName": "John Doe",
    "helperName": "Jane Smith",
    "driverPhone": "01712345678",
    "helperPhone": "01787654321"
  }'
```

### 1. Create Bus Slots with Bus Assignment

```bash
# Create first bus slot (Morning Express) - Route 1, No bus assigned
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Morning Express",
    "pickupTime": "07:30:00",
    "dropTime": "17:30:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "description": "Morning express bus service from Dhaka to Chittagong",
    "regular": true,
    "regularDays": "MONDAY,WEDNESDAY,FRIDAY"
  }'
```

```bash
# Create second bus slot (Evening Special) - Route 1, No bus assigned
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Evening Special",
    "pickupTime": "18:30:00",
    "dropTime": "23:30:00",
    "fromLocation": "Chittagong",
    "toLocation": "Dhaka",
    "status": "ACTIVE",
    "description": "Evening return service from Chittagong to Dhaka",
    "regular": true,
    "regularDays": "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY"
  }'
```

```bash
# Create third bus slot (Weekend Service) - Route 2, No bus assigned
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 2,
    "slotName": "Weekend Special",
    "pickupTime": "09:00:00",
    "dropTime": "20:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Sylhet",
    "status": "INACTIVE",
    "description": "Weekend service only from Dhaka to Sylhet",
    "regular": true,
    "regularDays": "SATURDAY,SUNDAY"
  }'
```

```bash
# Create fourth bus slot (Holiday Special) - Route 3, No bus assigned
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 3,
    "slotName": "Holiday Special",
    "pickupTime": "08:00:00",
    "dropTime": "21:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Coxs Bazar",
    "description": "Holiday destination service to Coxs Bazar",
    "regular": false
  }'
```

```bash
# Create fifth bus slot (with bus assignment) - Route 1, Bus 1
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Morning Express with Bus",
    "pickupTime": "06:30:00",
    "dropTime": "16:30:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "description": "Morning express with assigned bus",
    "regular": true,
    "regularDays": "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY"
  }'
```

### 2. Get All Bus Slots (with bus information in response)
```bash
curl -X GET http://localhost:8080/api/v1/bus-slots | json_pp
```

### 3. Get Bus Slot by ID
```bash
# Get slot with bus assignment
curl -X GET http://localhost:8080/api/v1/bus-slots/5 | json_pp

# Get slot without bus assignment
curl -X GET http://localhost:8080/api/v1/bus-slots/1 | json_pp
```

### 4. Get Slots by Route
```bash
# Get all slots for Route 1 (should include bus assignments)
curl -X GET http://localhost:8080/api/v1/bus-slots/route/1 | json_pp
```

### 5. Get Slots by Bus
```bash
# Get all slots assigned to Bus 1
curl -X GET http://localhost:8080/api/v1/bus-slots/bus/1 | json_pp

# Try with non-existent bus (should return empty array)
curl -X GET http://localhost:8080/api/v1/bus-slots/bus/999 | json_pp
```

### 6. Update Entire Slot (PUT) - Including Bus Assignment

```bash
# Update slot 1 - Assign bus to it
curl -X PUT http://localhost:8080/api/v1/bus-slots/1 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Morning Express - With Bus",
    "pickupTime": "07:15:00",
    "dropTime": "17:15:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "description": "Morning express with AC bus assigned",
    "regular": true,
    "regularDays": "MONDAY,WEDNESDAY,FRIDAY"
  }'
```

```bash
# Update slot 2 - Change bus assignment
curl -X PUT http://localhost:8080/api/v1/bus-slots/2 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Evening Special - Updated",
    "pickupTime": "19:00:00",
    "dropTime": "23:59:00",
    "fromLocation": "Chittagong",
    "toLocation": "Dhaka",
    "status": "ACTIVE",
    "description": "Updated evening service with bus assigned",
    "regular": true,
    "regularDays": "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY"
  }'
```

```bash
# Update slot 3 - Remove bus assignment (set busId to null or omit)
curl -X PUT http://localhost:8080/api/v1/bus-slots/3 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 2,
    "slotName": "Weekend Special - Updated",
    "pickupTime": "09:30:00",
    "dropTime": "20:30:00",
    "fromLocation": "Dhaka",
    "toLocation": "Sylhet",
    "status": "ACTIVE",
    "description": "Updated weekend service - no bus assigned",
    "regular": true,
    "regularDays": "SATURDAY,SUNDAY"
  }'
```

### 7. Get Slots by Bus and Time Range
```bash
# Get slots for Bus 1 between 6 AM and 6 PM
curl -X GET "http://localhost:8080/api/v1/bus-slots/bus/1/time-range?fromTime=06:00:00&toTime=18:00:00" | json_pp

# Get slots for Bus 1 between 5 PM and 12 AM
curl -X GET "http://localhost:8080/api/v1/bus-slots/bus/1/time-range?fromTime=17:00:00&toTime=23:59:00" | json_pp
```

### 8. Filter Slots with Bus Criteria

```bash
# Filter by bus only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1
  }' | json_pp
```

```bash
# Filter by bus + status
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1,
    "status": "ACTIVE"
  }' | json_pp
```

```bash
# Filter by bus + time range
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1,
    "fromTime": "06:00:00",
    "toTime": "20:00:00"
  }' | json_pp
```

```bash
# Complex filter with all parameters
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "status": "ACTIVE",
    "fromTime": "06:00:00",
    "toTime": "20:00:00",
    "isRegular": true
  }' | json_pp
```

### 9. Get Statistics by Bus
```bash
# Statistics for bus 1
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/bus/1 | json_pp

# Statistics for non-existent bus
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/bus/999 | json_pp
```

### 10. Get Overall Statistics
```bash
# Overall statistics (should include all slots)
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics | json_pp
```

### 11. Test Error Cases for Bus Assignment

```bash
# Try to create slot with non-existent bus (should fail)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 999,
    "slotName": "Invalid Bus",
    "pickupTime": "11:00:00",
    "dropTime": "21:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "regular": true
  }'
```

```bash
# Try to update slot with non-existent bus (should fail)
curl -X PUT http://localhost:8080/api/v1/bus-slots/1 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 999,
    "slotName": "Invalid Bus Update",
    "pickupTime": "07:30:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE"
  }'
```

### 12. Complete Test Flow with Bus Assignment

```bash
# 1. Create a new slot with bus assignment
echo "Creating slot with bus assignment..."
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Test Bus Slot",
    "pickupTime": "08:00:00",
    "dropTime": "18:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "description": "Test slot with bus",
    "regular": true,
    "regularDays": "MONDAY"
  }'

# 2. Get the created slot to verify bus info
echo -e "\n\nGetting created slot..."
curl -X GET http://localhost:8080/api/v1/bus-slots/6 | json_pp

# 3. Get all slots for bus 1
echo -e "\n\nGetting all slots for bus 1..."
curl -X GET http://localhost:8080/api/v1/bus-slots/bus/1 | json_pp

# 4. Update the slot to change bus (create another bus first if needed)
echo -e "\n\nUpdating slot to change bus..."
curl -X PUT http://localhost:8080/api/v1/bus-slots/6 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Test Bus Slot Updated",
    "pickupTime": "08:30:00",
    "dropTime": "18:30:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "description": "Updated test slot",
    "regular": true,
    "regularDays": "MONDAY,WEDNESDAY"
  }'

# 5. Filter slots by bus
echo -e "\n\nFiltering slots by bus..."
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{"busId": 1}' | json_pp

# 6. Get statistics by bus
echo -e "\n\nGetting statistics for bus 1..."
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/bus/1 | json_pp
```

