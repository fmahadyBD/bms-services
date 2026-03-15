
```bash
#!/bin/bash

# ============================================
# GREEN UNIVERSITY SURVEY MANAGEMENT API TESTS
# Location: Purbachole, Kanchon
# Using CURRENT DATES (March 2026)
# ============================================

echo "============================================"
echo "GREEN UNIVERSITY SURVEY MANAGEMENT API TESTS"
echo "Campus Location: Purbachole, Kanchon"
echo "Current Date: March 15, 2026"
echo "============================================"

# ─────────────────────────────────────────────────
# 1. CREATE SURVEYS WITH FUTURE DATES
# ─────────────────────────────────────────────────

echo -e "\n--- STEP 1: CREATING BUS TRANSPORTATION SURVEYS (With Future Dates) ---\n"

# Survey 1: Spring 2026 Semester Bus Requirement Survey
curl -X POST http://localhost:8080/api/v1/surveys \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring 2026 - Student Bus Transportation Survey",
    "description": "Please fill this survey to help us arrange buses for the Spring 2026 semester. This will help us determine routes, pickup points, and bus requirements.",
    "startDate": "2026-03-16",
    "endDate": "2026-03-30",
    "academicYear": "2025-2026",
    "semester": "Spring",
    "targetResponses": 500,
    "status": "DRAFT",
    "questions": [
      {
        "questionText": "Will you require university bus service this semester?",
        "questionType": "SINGLE_CHOICE",
        "options": "[\"Yes, daily\", \"Yes, 2-3 days a week\", \"Yes, occasionally\", \"No\"]",
        "displayOrder": 1,
        "required": true
      },
      {
        "questionText": "Which route will you use?",
        "questionType": "DROPDOWN",
        "options": "[\"Dhaka (Farmgate) → Purbachole Campus\", \"Uttara → Purbachole Campus\", \"Mirpur → Purbachole Campus\", \"Motijheel → Purbachole Campus\", \"Other\"]",
        "displayOrder": 2,
        "required": true
      },
      {
        "questionText": "Where will you board the bus?",
        "questionType": "TEXT",
        "displayOrder": 3,
        "required": true
      },
      {
        "questionText": "Where will you get down?",
        "questionType": "TEXT",
        "displayOrder": 4,
        "required": true
      },
      {
        "questionText": "Preferred morning pickup time",
        "questionType": "TIME",
        "displayOrder": 5,
        "required": true
      },
      {
        "questionText": "Preferred evening drop time",
        "questionType": "TIME",
        "displayOrder": 6,
        "required": true
      },
      {
        "questionText": "Do you need weekend bus service?",
        "questionType": "BOOLEAN",
        "displayOrder": 7,
        "required": false
      },
      {
        "questionText": "Any specific comments or requests?",
        "questionType": "TEXT",
        "displayOrder": 8,
        "required": false
      }
    ]
  }'

echo -e "\n"

# Survey 2: Women's Special Bus Service Survey
curl -X POST http://localhost:8080/api/v1/surveys \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Women'\''s Special Bus Service - Spring 2026",
    "description": "Survey for female students to assess demand for women's special bus service. This will help us arrange dedicated buses for female students.",
    "startDate": "2026-03-18",
    "endDate": "2026-04-02",
    "academicYear": "2025-2026",
    "semester": "Spring",
    "targetResponses": 200,
    "status": "DRAFT",
    "questions": [
      {
        "questionText": "Will you use women's special bus service?",
        "questionType": "SINGLE_CHOICE",
        "options": "[\"Yes, regularly\", \"Yes, occasionally\", \"No, I use regular bus\", \"No, I don'\''t need bus\"]",
        "displayOrder": 1,
        "required": true
      },
      {
        "questionText": "Which route do you prefer for women's special bus?",
        "questionType": "MULTIPLE_CHOICE",
        "options": "[\"Mirpur → Campus\", \"Uttara → Campus\", \"Farmgate → Campus\", \"Motijheel → Campus\", \"Dhanmondi → Campus\"]",
        "displayOrder": 2,
        "required": true
      },
      {
        "questionText": "Where will you board?",
        "questionType": "TEXT",
        "displayOrder": 3,
        "required": true
      },
      {
        "questionText": "Where will you get down?",
        "questionType": "TEXT",
        "displayOrder": 4,
        "required": true
      },
      {
        "questionText": "Preferred morning pickup time",
        "questionType": "TIME",
        "displayOrder": 5,
        "required": true
      },
      {
        "questionText": "Do you need evening return service?",
        "questionType": "BOOLEAN",
        "displayOrder": 6,
        "required": true
      },
      {
        "questionText": "Preferred evening drop time",
        "questionType": "TIME",
        "displayOrder": 7,
        "required": false
      }
    ]
  }'

echo -e "\n"

# Survey 3: Eid-ul-Fitr 2026 Holiday Survey
curl -X POST http://localhost:8080/api/v1/surveys \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Eid-ul-Fitr 2026 - Homebound Bus Service",
    "description": "Survey for students needing bus service for Eid holidays. We will arrange special buses for different destinations.",
    "startDate": "2026-03-20",
    "endDate": "2026-04-05",
    "academicYear": "2025-2026",
    "semester": "Spring",
    "targetResponses": 300,
    "status": "DRAFT",
    "questions": [
      {
        "questionText": "Do you need bus service for Eid holidays?",
        "questionType": "SINGLE_CHOICE",
        "options": "[\"Yes\", \"No, I have other arrangements\", \"Not sure yet\"]",
        "displayOrder": 1,
        "required": true
      },
      {
        "questionText": "Which district will you travel to?",
        "questionType": "DROPDOWN",
        "options": "[\"Dhaka\", \"Chittagong\", \"Sylhet\", \"Rajshahi\", \"Khulna\", \"Barisal\", \"Rangpur\", \"Mymensingh\", \"Comilla\", \"Other\"]",
        "displayOrder": 2,
        "required": true
      },
      {
        "questionText": "Preferred departure date from campus",
        "questionType": "DATE",
        "displayOrder": 3,
        "required": true
      },
      {
        "questionText": "Preferred return date to campus",
        "questionType": "DATE",
        "displayOrder": 4,
        "required": true
      },
      {
        "questionText": "Preferred departure time",
        "questionType": "TIME",
        "displayOrder": 5,
        "required": true
      },
      {
        "questionText": "How many people will travel with you?",
        "questionType": "SINGLE_CHOICE",
        "options": "[\"1 (Just me)\", \"2\", \"3\", \"4\", \"5 or more\"]",
        "displayOrder": 6,
        "required": true
      },
      {
        "questionText": "Do you prefer AC or Non-AC bus?",
        "questionType": "SINGLE_CHOICE",
        "options": "[\"AC\", \"Non-AC\", \"No preference\"]",
        "displayOrder": 7,
        "required": false
      },
      {
        "questionText": "Any specific pickup point in your district?",
        "questionType": "TEXT",
        "displayOrder": 8,
        "required": false
      }
    ]
  }'

echo -e "\n"

# ─────────────────────────────────────────────────
# 2. GET ALL SURVEYS
# ─────────────────────────────────────────────────

echo -e "\n--- STEP 2: GETTING ALL SURVEYS ---"
curl -X GET http://localhost:8080/api/v1/surveys

# ─────────────────────────────────────────────────
# 3. GET SURVEY BY ID
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 3: GETTING SURVEY 1 DETAILS ---"
curl -X GET http://localhost:8080/api/v1/surveys/1

# ─────────────────────────────────────────────────
# 4. UPDATE SURVEY STATUS (Publish surveys)
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 4: PUBLISHING SURVEY 1 ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/1/status?status=PUBLISHED"

echo -e "\n\n--- STEP 5: PUBLISHING SURVEY 2 ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/2/status?status=PUBLISHED"

echo -e "\n\n--- STEP 6: PUBLISHING SURVEY 3 ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/3/status?status=PUBLISHED"

# ─────────────────────────────────────────────────
# 5. GET ACTIVE SURVEYS
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 7: GETTING ACTIVE SURVEYS ---"
curl -X GET http://localhost:8080/api/v1/surveys/active

# ─────────────────────────────────────────────────
# 6. GET CURRENT SURVEYS (Today's date - should include surveys)
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 8: GETTING CURRENT SURVEYS ---"
curl -X GET http://localhost:8080/api/v1/surveys/current

# ─────────────────────────────────────────────────
# 7. STUDENTS SUBMIT RESPONSES
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 9: STUDENTS SUBMITTING RESPONSES FOR SURVEY 1 ---"

# Student 1: Karim from CSE (Dhaka route)
curl -X POST http://localhost:8080/api/v1/surveys/1/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-CSE-001",
    "studentName": "Md. Karim Ullah",
    "studentEmail": "karim.ullah@green.edu.bd",
    "studentPhone": "01712345678",
    "studentDepartment": "Computer Science and Engineering",
    "studentSemester": "6th Semester",
    "boardingPoint": "Farmgate, Dhaka",
    "dropPoint": "Purbachole Campus, Kanchon",
    "pickupTime": "07:00",
    "responseData": "{\"willUseBus\":\"Yes, daily\",\"route\":\"Dhaka (Farmgate) → Purbachole Campus\",\"needWeekend\":false,\"comments\":\"Prefer AC bus if available\"}",
    "additionalNotes": "Will board at Farmgate Metro Station"
  }'

echo -e "\n"

# Student 2: Fatema from EEE (Women's bus interest)
curl -X POST http://localhost:8080/api/v1/surveys/1/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-EEE-002",
    "studentName": "Fatema Begum",
    "studentEmail": "fatema.begum@green.edu.bd",
    "studentPhone": "01812345678",
    "studentDepartment": "Electrical and Electronic Engineering",
    "studentSemester": "4th Semester",
    "boardingPoint": "Mirpur 10, Dhaka",
    "dropPoint": "Purbachole Campus, Kanchon",
    "pickupTime": "07:15",
    "responseData": "{\"willUseBus\":\"Yes, daily\",\"route\":\"Mirpur → Purbachole Campus\",\"needWeekend\":false,\"comments\":\"Interested in women'\''s special bus\"}",
    "additionalNotes": "Will use women's section if available"
  }'

echo -e "\n"

# Student 3: Rahman from BBA (Uttara route)
curl -X POST http://localhost:8080/api/v1/surveys/1/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-BBA-003",
    "studentName": "Rahman Miah",
    "studentEmail": "rahman.miah@green.edu.bd",
    "studentPhone": "01912345678",
    "studentDepartment": "Business Administration",
    "studentSemester": "8th Semester",
    "boardingPoint": "Uttara Sector 3, Dhaka",
    "dropPoint": "Purbachole Campus, Kanchon",
    "pickupTime": "06:45",
    "responseData": "{\"willUseBus\":\"Yes, 2-3 days a week\",\"route\":\"Uttara → Purbachole Campus\",\"needWeekend\":true,\"comments\":\"Need evening return on Tuesday and Thursday\"}",
    "additionalNotes": "Will need evening return at 5:00 PM"
  }'

echo -e "\n"

# Student 4: Nusrat from English (Women's survey)
curl -X POST http://localhost:8080/api/v1/surveys/2/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-ENG-004",
    "studentName": "Nusrat Jahan",
    "studentEmail": "nusrat.jahan@green.edu.bd",
    "studentPhone": "01612345678",
    "studentDepartment": "English",
    "studentSemester": "2nd Semester",
    "boardingPoint": "Mirpur 10, Dhaka",
    "dropPoint": "Purbachole Campus, Kanchon",
    "pickupTime": "07:30",
    "responseData": "{\"willUseWomenBus\":\"Yes, regularly\",\"preferredRoutes\":[\"Mirpur → Campus\",\"Farmgate → Campus\"],\"needEveningReturn\":true,\"eveningTime\":\"17:00\"}",
    "additionalNotes": "Need women's special bus with female helper"
  }'

echo -e "\n"

# Student 5: Hasan from CSE (Eid survey)
curl -X POST http://localhost:8080/api/v1/surveys/3/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-CSE-005",
    "studentName": "Hasan Ali",
    "studentEmail": "hasan.ali@green.edu.bd",
    "studentPhone": "01512345678",
    "studentDepartment": "Computer Science and Engineering",
    "studentSemester": "6th Semester",
    "boardingPoint": "Purbachole Campus, Kanchon",
    "dropPoint": "Chittagong City",
    "pickupTime": "08:00",
    "responseData": "{\"needService\":\"Yes\",\"destination\":\"Chittagong\",\"departureDate\":\"2026-04-08\",\"returnDate\":\"2026-04-18\",\"travelers\":\"1 (Just me)\",\"busType\":\"AC\",\"pickupPoint\":\"Chittagong Railway Station\"}",
    "additionalNotes": "Need AC bus to Chittagong"
  }'

echo -e "\n"

# Student 6: Sharmin from Pharmacy (Women's survey)
curl -X POST http://localhost:8080/api/v1/surveys/2/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-PHA-006",
    "studentName": "Sharmin Akter",
    "studentEmail": "sharmin.akter@green.edu.bd",
    "studentPhone": "01312345678",
    "studentDepartment": "Pharmacy",
    "studentSemester": "4th Semester",
    "boardingPoint": "Dhanmondi 32, Dhaka",
    "dropPoint": "Purbachole Campus, Kanchon",
    "pickupTime": "07:45",
    "responseData": "{\"willUseWomenBus\":\"Yes, regularly\",\"preferredRoutes\":[\"Dhanmondi → Campus\"],\"needEveningReturn\":true,\"eveningTime\":\"16:30\"}",
    "additionalNotes": "Will board near Dhanmondi Lake"
  }'

echo -e "\n"

# Student 7: Rifat from CSE (Eid survey - Sylhet)
curl -X POST http://localhost:8080/api/v1/surveys/3/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-CSE-007",
    "studentName": "Rifat Khan",
    "studentEmail": "rifat.khan@green.edu.bd",
    "studentPhone": "01412345678",
    "studentDepartment": "Computer Science and Engineering",
    "studentSemester": "8th Semester",
    "boardingPoint": "Purbachole Campus, Kanchon",
    "dropPoint": "Sylhet City",
    "pickupTime": "09:00",
    "responseData": "{\"needService\":\"Yes\",\"destination\":\"Sylhet\",\"departureDate\":\"2026-04-07\",\"returnDate\":\"2026-04-19\",\"travelers\":\"2\",\"busType\":\"Non-AC\",\"pickupPoint\":\"Sylhet Ambarkhana\"}",
    "additionalNotes": "Traveling with friend"
  }'

echo -e "\n"

# Student 8: Tanvir from EEE (Survey 1 - Motijheel route)
curl -X POST http://localhost:8080/api/v1/surveys/1/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-EEE-008",
    "studentName": "Tanvir Hossain",
    "studentEmail": "tanvir.hossain@green.edu.bd",
    "studentPhone": "01787654321",
    "studentDepartment": "Electrical and Electronic Engineering",
    "studentSemester": "6th Semester",
    "boardingPoint": "Motijheel, Dhaka",
    "dropPoint": "Purbachole Campus, Kanchon",
    "pickupTime": "07:30",
    "responseData": "{\"willUseBus\":\"Yes, daily\",\"route\":\"Motijheel → Purbachole Campus\",\"needWeekend\":false,\"comments\":\"Will board near Shapla Chattar\"}",
    "additionalNotes": "Need stop at Malibagh"
  }'

echo -e "\n"

# ─────────────────────────────────────────────────
# 8. GET ALL RESPONSES FOR A SURVEY
# ─────────────────────────────────────────────────

echo -e "\n--- STEP 10: GETTING ALL RESPONSES FOR SURVEY 1 ---"
curl -X GET http://localhost:8080/api/v1/surveys/1/responses

# ─────────────────────────────────────────────────
# 9. GET STUDENT'S RESPONSE
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 11: GETTING RESPONSE FOR STUDENT GUB-CSE-001 ---"
curl -X GET http://localhost:8080/api/v1/surveys/1/responses/student/GUB-CSE-001

# ─────────────────────────────────────────────────
# 10. GET ALL RESPONSES BY STUDENT ID
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 12: GETTING ALL RESPONSES FOR STUDENT GUB-CSE-001 ---"
curl -X GET http://localhost:8080/api/v1/surveys/responses/student/GUB-CSE-001

# ─────────────────────────────────────────────────
# 11. UPDATE RESPONSE STATUS (Manager confirms seats)
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 13: CONFIRMING STUDENT RESPONSE (Karim) ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/responses/1/status?status=CONFIRMED&reason=Seat%20allocated%20-%20Bus%20101"

echo -e "\n\n--- STEP 14: WAITLISTING STUDENT RESPONSE (Fatema) ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/responses/2/status?status=WAITLISTED&reason=Bus%20full%20-%20Added%20to%20waitlist%20position%205"

echo -e "\n\n--- STEP 15: CONFIRMING WOMEN'S BUS RESPONSE (Nusrat) ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/responses/4/status?status=CONFIRMED&reason=Women%27s%20special%20bus%20-%20Seat%20confirmed"

# ─────────────────────────────────────────────────
# 12. GET SURVEY STATISTICS
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 16: GETTING STATISTICS FOR SURVEY 1 (Semester Bus) ---"
curl -X GET http://localhost:8080/api/v1/surveys/1/statistics

echo -e "\n\n--- STEP 17: GETTING STATISTICS FOR SURVEY 2 (Women's Special) ---"
curl -X GET http://localhost:8080/api/v1/surveys/2/statistics

echo -e "\n\n--- STEP 18: GETTING STATISTICS FOR SURVEY 3 (Eid Holiday) ---"
curl -X GET http://localhost:8080/api/v1/surveys/3/statistics

# ─────────────────────────────────────────────────
# 13. EXPORT SURVEY RESPONSES
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 19: EXPORTING SURVEY 1 RESPONSES ---"
curl -X GET http://localhost:8080/api/v1/surveys/1/export

# ─────────────────────────────────────────────────
# 14. UPDATE SURVEY (Extend deadline)
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 20: EXTENDING SURVEY 2 DEADLINE ---"
curl -X PUT http://localhost:8080/api/v1/surveys/2 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Women'\''s Special Bus Service - Spring 2026 (Extended)",
    "description": "Extended deadline for female students to assess demand for women's special bus service. More responses needed.",
    "startDate": "2026-03-18",
    "endDate": "2026-04-05",
    "academicYear": "2025-2026",
    "semester": "Spring",
    "targetResponses": 250,
    "status": "PUBLISHED",
    "questions": [
      {
        "questionText": "Will you use women's special bus service?",
        "questionType": "SINGLE_CHOICE",
        "options": "[\"Yes, regularly\", \"Yes, occasionally\", \"No, I use regular bus\", \"No, I don'\''t need bus\"]",
        "displayOrder": 1,
        "required": true
      },
      {
        "questionText": "Which route do you prefer for women's special bus?",
        "questionType": "MULTIPLE_CHOICE",
        "options": "[\"Mirpur → Campus\", \"Uttara → Campus\", \"Farmgate → Campus\", \"Motijheel → Campus\", \"Dhanmondi → Campus\"]",
        "displayOrder": 2,
        "required": true
      },
      {
        "questionText": "Where will you board?",
        "questionType": "TEXT",
        "displayOrder": 3,
        "required": true
      },
      {
        "questionText": "Where will you get down?",
        "questionType": "TEXT",
        "displayOrder": 4,
        "required": true
      },
      {
        "questionText": "Preferred morning pickup time",
        "questionType": "TIME",
        "displayOrder": 5,
        "required": true
      },
      {
        "questionText": "Do you need evening return service?",
        "questionType": "BOOLEAN",
        "displayOrder": 6,
        "required": true
      },
      {
        "questionText": "Preferred evening drop time",
        "questionType": "TIME",
        "displayOrder": 7,
        "required": false
      },
      {
        "questionText": "Would you prefer female bus helper?",
        "questionType": "BOOLEAN",
        "displayOrder": 8,
        "required": false
      }
    ]
  }'

# ─────────────────────────────────────────────────
# 15. PARTIAL UPDATE (PATCH) - Update only target responses
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 21: PARTIALLY UPDATING SURVEY 3 TARGET ---"
curl -X PATCH http://localhost:8080/api/v1/surveys/3 \
  -H "Content-Type: application/json" \
  -d '{
    "targetResponses": 400,
    "description": "Updated Eid survey - More students expected this year"
  }'

# ─────────────────────────────────────────────────
# 16. CLOSE SURVEY (After deadline)
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 22: CLOSING SURVEY 1 ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/1/status?status=CLOSED"

echo -e "\n\n--- STEP 23: CLOSING SURVEY 2 ---"
curl -X PATCH "http://localhost:8080/api/v1/surveys/2/status?status=CLOSED"

# ─────────────────────────────────────────────────
# 17. ERROR TESTING
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 24: ERROR TESTING - DUPLICATE RESPONSE (Should fail) ---"
curl -X POST http://localhost:8080/api/v1/surveys/1/responses \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "GUB-CSE-001",
    "studentName": "Md. Karim Ullah",
    "studentEmail": "karim.ullah@green.edu.bd",
    "studentPhone": "01712345678",
    "studentDepartment": "Computer Science and Engineering",
    "studentSemester": "6th Semester",
    "boardingPoint": "Farmgate, Dhaka",
    "dropPoint": "Purbachole Campus, Kanchon"
  }'

echo -e "\n\n--- STEP 25: ERROR TESTING - INVALID SURVEY ID (Should return 404) ---"
curl -X GET http://localhost:8080/api/v1/surveys/999

# ─────────────────────────────────────────────────
# 18. DELETE SURVEY (Soft delete - mark as inactive)
# ─────────────────────────────────────────────────

echo -e "\n\n--- STEP 26: DELETING SURVEY 3 (Soft Delete) ---"
curl -X DELETE http://localhost:8080/api/v1/surveys/3

echo -e "\n\n--- STEP 27: VERIFYING DELETION (Should show inactive) ---"
curl -X GET http://localhost:8080/api/v1/surveys/3

# ─────────────────────────────────────────────────
# 19. FINAL SUMMARY
# ─────────────────────────────────────────────────

echo -e "\n\n============================================"
echo "GREEN UNIVERSITY SURVEY WORKFLOW SUMMARY"
echo "============================================"
echo "✓ Created 3 surveys for Spring 2026 semester"
echo "✓ Dates set: March 16, 2026 - April 5, 2026"
echo "✓ Published surveys for student responses"
echo "✓ Collected 8 student responses across surveys"
echo "✓ Updated response statuses (Confirmed/Waitlisted)"
echo "✓ Generated statistics for all surveys"
echo "✓ Exported survey data for analysis"
echo "✓ Closed completed surveys"
echo "============================================"

echo -e "\n--- FINAL STEP: GETTING ALL SURVEYS (Final State) ---"
curl -X GET http://localhost:8080/api/v1/surveys

echo -e "\n\n============================================"
echo "SURVEY API TESTING COMPLETE"
echo "============================================"
```

