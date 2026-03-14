

## 1. Create Bus Slots

```bash
# Create first bus slot (Morning Express)
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
    "description": "Morning express bus service",
    "regular": true,
    "regularDays": "MONDAY,WEDNESDAY,FRIDAY"
  }'
```

```bash
# Create second bus slot (Evening Special)
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
    "description": "Evening return service",
    "regular": true,
    "regularDays": "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY"
  }'
```

```bash
# Create third bus slot (Weekend Service)
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
    "description": "Weekend service only",
    "regular": true,
    "regularDays": "SATURDAY,SUNDAY"
  }'
```

```bash
# Create fourth bus slot (Holiday Special - without status, will default to ACTIVE)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 3,
    "slotName": "Holiday Special",
    "pickupTime": "08:00:00",
    "dropTime": "21:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Coxs Bazar",
    "description": "Holiday destination service",
    "regular": false
  }'
```

## 2. Get All Bus Slots

```bash
curl -X GET http://localhost:8080/api/v1/bus-slots
```

## 3. Get Bus Slot by ID

```bash
# Replace 1 with actual ID from create response
curl -X GET http://localhost:8080/api/v1/bus-slots/1
```

## 4. Get Slots by Route

```bash
curl -X GET http://localhost:8080/api/v1/bus-slots/route/1
```

## 5. Get Slots by Status

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

## 6. Get Slots by Time Range

```bash
# Get slots between 7 AM and 10 AM
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=07:00:00&toTime=10:00:00"

# Get slots between 5 PM and 9 PM
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=17:00:00&toTime=21:00:00"
```

## 7. Get Slots by Route and Time Range

```bash
# Get slots for route 1 between 7 AM and 8 PM
curl -X GET "http://localhost:8080/api/v1/bus-slots/route/1/time-range?fromTime=07:00:00&toTime=20:00:00"
```

## 8. Update Slot Status

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
    "reason": "Service temporarily suspended"
  }'

# Update slot 3 to ACTIVE
curl -X PATCH http://localhost:8080/api/v1/bus-slots/3/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE",
    "reason": "Service resumed"
  }'
```

## 9. Update Entire Slot

```bash
# Update slot 1 with new information
curl -X PUT http://localhost:8080/api/v1/bus-slots/1 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Morning Express - Updated",
    "pickupTime": "07:45:00",
    "dropTime": "18:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "description": "Updated morning express with new timing",
    "regular": true,
    "regularDays": "MONDAY,WEDNESDAY,FRIDAY"
  }'
```

## 10. Filter Slots

```bash
# Filter by route only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1
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
```

## 11. Get Statistics

```bash
# Overall statistics
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics
```

## 12. Get Statistics by Route

```bash
# Statistics for route 1
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/route/1

# Statistics for route 2
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/route/2
```

## 13. Delete a Slot

```bash
# Delete slot 4
curl -X DELETE http://localhost:8080/api/v1/bus-slots/4

# Verify deletion (should return 404)
curl -X GET http://localhost:8080/api/v1/bus-slots/4
```

## 14. Test Error Cases

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

# Try to get non-existent slot (should return 404)
curl -X GET http://localhost:8080/api/v1/bus-slots/999

# Try to update non-existent slot (should return 404)
curl -X PUT http://localhost:8080/api/v1/bus-slots/999 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Non Existent",
    "pickupTime": "07:30:00",
    "dropTime": "17:30:00",
    "fromLocation": "Dhaka",
    "toLocation": "Chittagong",
    "status": "ACTIVE",
    "regular": true
  }'

# Try to delete non-existent slot (should return 404)
curl -X DELETE http://localhost:8080/api/v1/bus-slots/999
```

## 15. Complete Test Flow (Run in Order)

```bash
# 1. Create slots
curl -X POST http://localhost:8080/api/v1/bus-slots -H "Content-Type: application/json" -d '{"routeId":1,"slotName":"Test1","pickupTime":"08:00:00","dropTime":"18:00:00","fromLocation":"A","toLocation":"B","status":"ACTIVE","regular":true,"regularDays":"MONDAY"}'

curl -X POST http://localhost:8080/api/v1/bus-slots -H "Content-Type: application/json" -d '{"routeId":1,"slotName":"Test2","pickupTime":"09:00:00","dropTime":"19:00:00","fromLocation":"B","toLocation":"A","status":"ACTIVE","regular":true,"regularDays":"TUESDAY"}'

# 2. Get all slots
curl -X GET http://localhost:8080/api/v1/bus-slots

# 3. Get by route
curl -X GET http://localhost:8080/api/v1/bus-slots/route/1

# 4. Update status
curl -X PATCH http://localhost:8080/api/v1/bus-slots/1/status -H "Content-Type: application/json" -d '{"status":"FULL","reason":"Test booking"}'

# 5. Get statistics
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics

# 6. Filter active slots
curl -X POST http://localhost:8080/api/v1/bus-slots/filter -H "Content-Type: application/json" -d '{"status":"ACTIVE"}'

# 7. Delete test slot
curl -X DELETE http://localhost:8080/api/v1/bus-slots/2

# 8. Verify deletion
curl -X GET http://localhost:8080/api/v1/bus-slots
```

