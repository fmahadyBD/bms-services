
## Bus Slot API Testing Commands

### 1. Create Bus Slots

```bash
# Create first bus slot (Morning Express) - Route 1
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
# Create second bus slot (Evening Special) - Route 1
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
# Create third bus slot (Weekend Service) - Route 2
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
# Create fourth bus slot (Holiday Special) - Route 3
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

### 2. Get All Bus Slots
```bash
curl -X GET http://localhost:8080/api/v1/bus-slots
```

### 3. Get Bus Slot by ID
```bash
curl -X GET http://localhost:8080/api/v1/bus-slots/1
curl -X GET http://localhost:8080/api/v1/bus-slots/2
curl -X GET http://localhost:8080/api/v1/bus-slots/3
```

### 4. Get Slots by Route
```bash
# Get all slots for Route 1
curl -X GET http://localhost:8080/api/v1/bus-slots/route/1

# Get all slots for Route 2
curl -X GET http://localhost:8080/api/v1/bus-slots/route/2

# Get all slots for Route 3
curl -X GET http://localhost:8080/api/v1/bus-slots/route/3
```

### 5. Get Slots by Bus
```bash
# Get all slots assigned to Bus 1
curl -X GET http://localhost:8080/api/v1/bus-slots/bus/1
```

### 6. Get Slots by Status
```bash
# Get ACTIVE slots
curl -X GET http://localhost:8080/api/v1/bus-slots/status/ACTIVE

# Get INACTIVE slots
curl -X GET http://localhost:8080/api/v1/bus-slots/status/INACTIVE

# Get FULL slots
curl -X GET http://localhost:8080/api/v1/bus-slots/status/FULL

# Get CANCELLED slots
curl -X GET http://localhost:8080/api/v1/bus-slots/status/CANCELLED
```

### 7. Get Slots by Time Range
```bash
# Get slots between 6 AM and 12 PM
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=06:00:00&toTime=12:00:00"

# Get slots between 5 PM and 12 AM
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=17:00:00&toTime=23:59:00"

# Get slots between 7 AM and 9 AM
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=07:00:00&toTime=09:00:00"
```

### 8. Get Slots by Route and Time Range
```bash
# Get slots for Route 1 between 7 AM and 8 PM
curl -X GET "http://localhost:8080/api/v1/bus-slots/route/1/time-range?fromTime=07:00:00&toTime=20:00:00"

# Get slots for Route 2 between 8 AM and 10 PM
curl -X GET "http://localhost:8080/api/v1/bus-slots/route/2/time-range?fromTime=08:00:00&toTime=22:00:00"
```

### 9. Get Slots by Bus and Time Range
```bash
# Get slots for Bus 1 between 6 AM and 6 PM
curl -X GET "http://localhost:8080/api/v1/bus-slots/bus/1/time-range?fromTime=06:00:00&toTime=18:00:00"
```

### 10. Update Slot Status
```bash
# Update slot 1 to FULL
curl -X PATCH http://localhost:8080/api/v1/bus-slots/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "FULL",
    "reason": "All seats are booked for this week"
  }'

# Update slot 2 to CANCELLED
curl -X PATCH http://localhost:8080/api/v1/bus-slots/2/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "CANCELLED",
    "reason": "Service temporarily suspended due to maintenance"
  }'

# Update slot 3 to ACTIVE
curl -X PATCH http://localhost:8080/api/v1/bus-slots/3/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE",
    "reason": "Service resumed after maintenance"
  }'

# Update slot 4 to FULL
curl -X PATCH http://localhost:8080/api/v1/bus-slots/4/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "FULL",
    "reason": "Holiday special fully booked"
  }'
```

### 11. Update Entire Slot (PUT)
```bash
# Update slot 1 with new information
curl -X PUT http://localhost:8080/api/v1/bus-slots/1 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Morning Express - Premium",
    "pickupTime": "07:15:00",
    "dropTime": "17:15:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "description": "Premium morning express with AC bus",
    "regular": true,
    "regularDays": "MONDAY,WEDNESDAY,FRIDAY"
  }'

# Update slot 2 (remove bus assignment)
curl -X PUT http://localhost:8080/api/v1/bus-slots/2 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Evening Special - Updated",
    "pickupTime": "19:00:00",
    "dropTime": "23:59:00",
    "fromLocation": "Chittagong",
    "toLocation": "Dhaka",
    "status": "ACTIVE",
    "description": "Updated evening service with new timing",
    "regular": true,
    "regularDays": "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY"
  }'
```

### 12. Filter Slots
```bash
# Filter by route only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1
  }'

# Filter by bus only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1
  }'

# Filter by status only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE"
  }'

# Filter by time range only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "fromTime": "07:00:00",
    "toTime": "20:00:00"
  }'

# Filter by regular slots only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "isRegular": true
  }'

# Complex filter (route + status + time range + regular)
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "status": "ACTIVE",
    "fromTime": "07:00:00",
    "toTime": "20:00:00",
    "isRegular": true
  }'

# Complex filter with bus
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1,
    "status": "ACTIVE",
    "fromTime": "06:00:00",
    "toTime": "18:00:00"
  }'
```

### 13. Get Statistics
```bash
# Overall statistics
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics
```

### 14. Get Statistics by Route
```bash
# Statistics for route 1
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/route/1

# Statistics for route 2
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/route/2

# Statistics for route 3
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/route/3
```

### 15. Get Statistics by Bus
```bash
# Statistics for bus 1
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/bus/1
```

### 16. Delete a Slot
```bash
# Delete slot 5
curl -X DELETE http://localhost:8080/api/v1/bus-slots/5

# Verify deletion (should return 404)
curl -X GET http://localhost:8080/api/v1/bus-slots/5
```

### 17. Test Error Cases

```bash
# Try to create duplicate slot (should fail with 409 Conflict)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Duplicate Morning",
    "pickupTime": "07:30:00",
    "dropTime": "17:30:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "regular": true,
    "regularDays": "MONDAY,WEDNESDAY,FRIDAY"
  }'

# Try to create slot with invalid route (should fail)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 999,
    "slotName": "Invalid Route",
    "pickupTime": "10:00:00",
    "fromLocation": "A",
    "toLocation": "B",
    "status": "ACTIVE"
  }'

# Try to create slot with invalid bus (should fail)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 999,
    "slotName": "Invalid Bus",
    "pickupTime": "11:00:00",
    "fromLocation": "A",
    "toLocation": "B",
    "status": "ACTIVE"
  }'

# Try to get non-existent slot (should return 404)
curl -X GET http://localhost:8080/api/v1/bus-slots/999

# Try to update non-existent slot (should return 404)
curl -X PUT http://localhost:8080/api/v1/bus-slots/999 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Non Existent",
    "pickupTime": "07:30:00",
    "fromLocation": "A",
    "toLocation": "B",
    "status": "ACTIVE"
  }'

# Try to delete non-existent slot (should return 404)
curl -X DELETE http://localhost:8080/api/v1/bus-slots/999

# Try to update status with invalid status (should fail)
curl -X PATCH http://localhost:8080/api/v1/bus-slots/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "INVALID_STATUS",
    "reason": "Test"
  }'
```

### 18. Complete Test Flow (Run in Order)

```bash
# 1. Create test slots
echo "Creating test slots..."
curl -X POST http://localhost:8080/api/v1/bus-slots -H "Content-Type: application/json" -d '{"routeId":1,"slotName":"Test Slot 1","pickupTime":"08:00:00","dropTime":"18:00:00","fromLocation":"A","toLocation":"B","status":"ACTIVE","regular":true,"regularDays":"MONDAY"}'
curl -X POST http://localhost:8080/api/v1/bus-slots -H "Content-Type: application/json" -d '{"routeId":1,"slotName":"Test Slot 2","pickupTime":"09:00:00","dropTime":"19:00:00","fromLocation":"B","toLocation":"A","status":"ACTIVE","regular":true,"regularDays":"TUESDAY"}'
curl -X POST http://localhost:8080/api/v1/bus-slots -H "Content-Type: application/json" -d '{"routeId":2,"slotName":"Test Slot 3","pickupTime":"10:00:00","dropTime":"20:00:00","fromLocation":"C","toLocation":"D","status":"INACTIVE","regular":false}'

# 2. Get all slots
echo -e "\n\nGetting all slots..."
curl -X GET http://localhost:8080/api/v1/bus-slots

# 3. Get by route
echo -e "\n\nGetting slots for route 1..."
curl -X GET http://localhost:8080/api/v1/bus-slots/route/1

# 4. Get by time range
echo -e "\n\nGetting slots between 7 AM and 10 AM..."
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=07:00:00&toTime=10:00:00"

# 5. Update status
echo -e "\n\nUpdating slot 1 status to FULL..."
curl -X PATCH http://localhost:8080/api/v1/bus-slots/1/status -H "Content-Type: application/json" -d '{"status":"FULL","reason":"Test booking"}'

# 6. Get statistics
echo -e "\n\nGetting overall statistics..."
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics

# 7. Filter active slots
echo -e "\n\nFiltering active slots..."
curl -X POST http://localhost:8080/api/v1/bus-slots/filter -H "Content-Type: application/json" -d '{"status":"ACTIVE"}'

# 8. Delete test slot
echo -e "\n\nDeleting slot 3..."
curl -X DELETE http://localhost:8080/api/v1/bus-slots/3

# 9. Verify deletion
echo -e "\n\nVerifying deletion..."
curl -X GET http://localhost:8080/api/v1/bus-slots
```

These commands cover all your Bus Slot API endpoints. Run them in sequence to test the complete functionality!