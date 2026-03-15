

```bash
# ============================================
# GREEN UNIVERSITY BUS SLOT MANAGEMENT API TESTS
# Location: Purbachole, Kanchon
# Routes: Campus to/from Various Dhaka Locations
# ============================================

# ─────────────────────────────────────────────────
# PREREQUISITES: Create Buses and Routes First
# ─────────────────────────────────────────────────

# Create a bus for testing (using BUS-XXX format)
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University AC Express",
    "busNumber": "BUS-101",
    "status": "ACTIVE",
    "driverName": "Md. Karim Ullah",
    "helperName": "Rahim Miah",
    "driverPhone": "01712345678",
    "helperPhone": "01812345678"
  }'

# Create another bus
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -d '{
    "busName": "Green University Non-AC",
    "busNumber": "BUS-102",
    "status": "ACTIVE",
    "driverName": "Shahidul Islam",
    "helperName": "Jahangir Alam",
    "driverPhone": "01912345678",
    "helperPhone": "01612345678"
  }'

# Create Route 1: Dhaka → Purbachole Campus
curl -X POST http://localhost:8080/api/v1/routes \
  -H "Content-Type: application/json" \
  -d '{
    "busNo": "BUS-101",
    "routeName": "Dhaka (Farmgate) → Purbachole Campus",
    "routeLine": "Farmgate → Shahbag → Malibagh → Rampura → Badda → Kanchon Bridge → Purbachole (GUB)",
    "operatingDays": ["SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"],
    "pickupPoints": [
      {"placeName": "Farmgate", "placeDetails": "Farmgate Metro Station", "pickupTime": "07:00", "stopOrder": 1},
      {"placeName": "Shahbag", "placeDetails": "TSC (Dhaka University)", "pickupTime": "07:15", "stopOrder": 2},
      {"placeName": "Green University Campus", "placeDetails": "Purbachole, Kanchon", "pickupTime": "08:30", "stopOrder": 7}
    ]
  }'

# ─────────────────────────────────────────────────
# 1. CREATE BUS SLOTS (Green University Routes)
# ─────────────────────────────────────────────────

# Slot 1: Morning Slot - Dhaka to Campus (No bus assigned)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Morning Dhaka → Campus",
    "pickupTime": "07:00:00",
    "dropTime": "08:30:00",
    "fromLocation": "Farmgate, Dhaka",
    "toLocation": "Purbachole, Kanchon (GUB Campus)",
    "status": "ACTIVE",
    "description": "Morning pickup from Dhaka to Green University Campus",
    "regular": true,
    "regularDays": "SATURDAY,SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY"
  }'

# Slot 2: Evening Slot - Campus to Dhaka (No bus assigned)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Evening Campus → Dhaka",
    "pickupTime": "16:30:00",
    "dropTime": "18:00:00",
    "fromLocation": "Purbachole, Kanchon (GUB Campus)",
    "toLocation": "Motijheel, Dhaka",
    "status": "ACTIVE",
    "description": "Evening return from Green University to Dhaka",
    "regular": true,
    "regularDays": "SATURDAY,SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY"
  }'

# Slot 3: Mid-day Slot - Express Service (With bus assigned)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Mid-day Express Dhaka ↔ Campus",
    "pickupTime": "12:00:00",
    "dropTime": "13:30:00",
    "fromLocation": "Farmgate, Dhaka",
    "toLocation": "Purbachole, Kanchon (GUB Campus)",
    "status": "ACTIVE",
    "description": "Mid-day express service with AC bus",
    "regular": true,
    "regularDays": "SATURDAY,MONDAY,WEDNESDAY"
  }'

# Slot 4: Women's Special Slot
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 2,
    "slotName": "Women'\''s Special Morning",
    "pickupTime": "07:30:00",
    "dropTime": "09:00:00",
    "fromLocation": "Mirpur 10, Dhaka",
    "toLocation": "Purbachole, Kanchon (GUB Campus)",
    "status": "ACTIVE",
    "description": "Women's special bus service",
    "regular": true,
    "regularDays": "SATURDAY,WEDNESDAY"
  }'

# Slot 5: Weekend Holiday Slot
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Friday Holiday Special",
    "pickupTime": "09:00:00",
    "dropTime": "20:00:00",
    "fromLocation": "Green University Campus",
    "toLocation": "Cox'\''s Bazar",
    "status": "INACTIVE",
    "description": "Weekend holiday trip from campus",
    "regular": false
  }'

# Slot 6: Late Evening Slot (With bus assignment)
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Late Evening Campus → Dhaka",
    "pickupTime": "20:00:00",
    "dropTime": "21:30:00",
    "fromLocation": "Purbachole, Kanchon (GUB Campus)",
    "toLocation": "Uttara, Dhaka",
    "status": "ACTIVE",
    "description": "Late evening service for north Dhaka residents",
    "regular": true,
    "regularDays": "SUNDAY,TUESDAY,THURSDAY"
  }'

# ─────────────────────────────────────────────────
# 2. GET ALL BUS SLOTS
# ─────────────────────────────────────────────────
curl -X GET http://localhost:8080/api/v1/bus-slots

# ─────────────────────────────────────────────────
# 3. GET BUS SLOT BY ID
# ─────────────────────────────────────────────────
curl -X GET http://localhost:8080/api/v1/bus-slots/1
curl -X GET http://localhost:8080/api/v1/bus-slots/3
curl -X GET http://localhost:8080/api/v1/bus-slots/4

# ─────────────────────────────────────────────────
# 4. GET SLOTS BY ROUTE
# ─────────────────────────────────────────────────
curl -X GET http://localhost:8080/api/v1/bus-slots/route/1

# ─────────────────────────────────────────────────
# 5. GET SLOTS BY BUS
# ─────────────────────────────────────────────────
curl -X GET http://localhost:8080/api/v1/bus-slots/bus/1
curl -X GET http://localhost:8080/api/v1/bus-slots/bus/2

# ─────────────────────────────────────────────────
# 6. GET SLOTS BY STATUS
# ─────────────────────────────────────────────────
curl -X GET http://localhost:8080/api/v1/bus-slots/status/ACTIVE
curl -X GET http://localhost:8080/api/v1/bus-slots/status/INACTIVE

# ─────────────────────────────────────────────────
# 7. GET SLOTS BY TIME RANGE
# ─────────────────────────────────────────────────
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=06:00:00&toTime=12:00:00"
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=16:00:00&toTime=22:00:00"

# ─────────────────────────────────────────────────
# 8. GET SLOTS BY ROUTE AND TIME RANGE
# ─────────────────────────────────────────────────
curl -X GET "http://localhost:8080/api/v1/bus-slots/route/1/time-range?fromTime=06:00:00&toTime=12:00:00"

# ─────────────────────────────────────────────────
# 9. GET SLOTS BY BUS AND TIME RANGE
# ─────────────────────────────────────────────────
curl -X GET "http://localhost:8080/api/v1/bus-slots/bus/1/time-range?fromTime=06:00:00&toTime=18:00:00"
curl -X GET "http://localhost:8080/api/v1/bus-slots/bus/1/time-range?fromTime=18:00:00&toTime=23:59:00"

# ─────────────────────────────────────────────────
# 10. UPDATE SLOT STATUS
# ─────────────────────────────────────────────────

# Activate Slot 5
curl -X PATCH http://localhost:8080/api/v1/bus-slots/5/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE",
    "reason": "Holiday trip approved for upcoming weekend"
  }'

# Deactivate Slot 4 for maintenance
curl -X PATCH http://localhost:8080/api/v1/bus-slots/4/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "INACTIVE",
    "reason": "Bus under maintenance"
  }'

# ─────────────────────────────────────────────────
# 11. UPDATE ENTIRE SLOT (PUT) - Including Bus Assignment
# ─────────────────────────────────────────────────

# Update Slot 1 - Assign bus to it
curl -X PUT http://localhost:8080/api/v1/bus-slots/1 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 1,
    "slotName": "Morning Dhaka → Campus (AC Bus)",
    "pickupTime": "06:45:00",
    "dropTime": "08:15:00",
    "fromLocation": "Farmgate, Dhaka",
    "toLocation": "Purbachole, Kanchon (GUB Campus)",
    "status": "ACTIVE",
    "description": "Early morning service with AC bus",
    "regular": true,
    "regularDays": "SATURDAY,SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY"
  }'

# Update Slot 2 - Change bus assignment
curl -X PUT http://localhost:8080/api/v1/bus-slots/2 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 2,
    "slotName": "Evening Campus → Dhaka (Express)",
    "pickupTime": "16:15:00",
    "dropTime": "17:45:00",
    "fromLocation": "Purbachole, Kanchon (GUB Campus)",
    "toLocation": "Motijheel, Dhaka",
    "status": "ACTIVE",
    "description": "Express evening service with Non-AC bus",
    "regular": true,
    "regularDays": "SATURDAY,SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY"
  }'

# Update Slot 3 - Remove bus assignment
curl -X PUT http://localhost:8080/api/v1/bus-slots/3 \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "slotName": "Mid-day Express (Temporary)",
    "pickupTime": "12:30:00",
    "dropTime": "14:00:00",
    "fromLocation": "Farmgate, Dhaka",
    "toLocation": "Purbachole, Kanchon (GUB Campus)",
    "status": "ACTIVE",
    "description": "Temporary service - bus TBD",
    "regular": true,
    "regularDays": "SATURDAY,MONDAY,WEDNESDAY"
  }'

# ─────────────────────────────────────────────────
# 12. FILTER SLOTS
# ─────────────────────────────────────────────────

# Filter by bus only
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1
  }'

# Filter by bus + status
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1,
    "status": "ACTIVE"
  }'

# Filter by bus + time range
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "busId": 1,
    "fromTime": "06:00:00",
    "toTime": "20:00:00"
  }'

# Filter by route + status
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "status": "ACTIVE"
  }'

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
  }'

# ─────────────────────────────────────────────────
# 13. GET STATISTICS
# ─────────────────────────────────────────────────

# Overall statistics
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics

# Statistics by route
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/route/1

# Statistics by bus
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/bus/1
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/bus/2

# ─────────────────────────────────────────────────
# 14. DELETE SLOT
# ─────────────────────────────────────────────────

# Delete Slot 5
curl -X DELETE http://localhost:8080/api/v1/bus-slots/5

# Verify deletion
curl -X GET http://localhost:8080/api/v1/bus-slots/5

# ─────────────────────────────────────────────────
# 15. ERROR TESTING
# ─────────────────────────────────────────────────

# Create slot with non-existent route
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 999,
    "slotName": "Invalid Route",
    "pickupTime": "07:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Campus"
  }'

# Create slot with non-existent bus
curl -X POST http://localhost:8080/api/v1/bus-slots \
  -H "Content-Type: application/json" \
  -d '{
    "routeId": 1,
    "busId": 999,
    "slotName": "Invalid Bus",
    "pickupTime": "07:00:00",
    "fromLocation": "Dhaka",
    "toLocation": "Campus"
  }'

# Get non-existent slot
curl -X GET http://localhost:8080/api/v1/bus-slots/999

# ─────────────────────────────────────────────────
# 16. COMPLETE TEST FLOW - Daily Operations
# ─────────────────────────────────────────────────

echo "============================================"
echo "GREEN UNIVERSITY DAILY BUS SLOT OPERATIONS"
echo "============================================"

# Step 1: Check morning slots
echo -e "\n[06:00 AM] Checking morning slots..."
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=06:00:00&toTime=09:00:00"

# Step 2: Get bus assignments for morning
echo -e "\n[06:30 AM] Checking bus assignments for morning slots..."
curl -X GET http://localhost:8080/api/v1/bus-slots/bus/1

# Step 3: Mid-day check
echo -e "\n[12:00 PM] Checking mid-day slots..."
curl -X POST http://localhost:8080/api/v1/bus-slots/filter \
  -H "Content-Type: application/json" \
  -d '{
    "fromTime": "12:00:00",
    "toTime": "15:00:00",
    "status": "ACTIVE"
  }'

# Step 4: Evening slots
echo -e "\n[04:00 PM] Checking evening return slots..."
curl -X GET "http://localhost:8080/api/v1/bus-slots/time-range?fromTime=16:00:00&toTime=20:00:00"

# Step 5: Update slot status for next day
echo -e "\n[08:00 PM] Updating slots for next day..."
curl -X PATCH http://localhost:8080/api/v1/bus-slots/3/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "ACTIVE",
    "reason": "Ready for next day service"
  }'

# Step 6: End of day statistics
echo -e "\n[09:00 PM] End of day statistics..."
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics
curl -X GET http://localhost:8080/api/v1/bus-slots/statistics/bus/1

echo -e "\n============================================"
echo "BUS SLOT API TESTING COMPLETE"
echo "============================================"
```

## Key Features of Updated Bus Slot Tests:

1. **Green University Context**: All slots are for routes to/from Purbachole, Kanchon campus
2. **Realistic Slot Names**:
   - "Morning Dhaka → Campus"
   - "Evening Campus → Dhaka" 
   - "Women's Special Morning"
   - "Friday Holiday Special"

3. **Bus Assignments**:
   - Some slots with bus assigned (busId: 1, 2)
   - Some slots without bus assignment
   - Ability to update/remove bus assignments

4. **Time-based Operations**:
   - Morning slots (6 AM - 9 AM)
   - Mid-day slots (12 PM - 3 PM)
   - Evening slots (4 PM - 8 PM)
   - Late evening slots (8 PM - 10 PM)

5. **Complete Test Coverage**:
   - CRUD operations
   - Filtering by multiple criteria
   - Statistics by route and bus
   - Error cases
   - Daily operations flow
