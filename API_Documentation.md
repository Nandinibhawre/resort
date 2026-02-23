# Elite Resort - QA API Documentation

## Base URL
```
http://localhost:8080
```

---

## 1. Authentication APIs

### 1.1 User Registration
**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

---

### 1.2 User Login
**Endpoint:** `POST /api/auth/user/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

---

### 1.3 Admin Login
**Endpoint:** `POST /api/auth/admin/login`

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```

**Response (200 OK):**
```
"eyJhbGciOiJIUzI1NiIs..." (JWT Token string)
```

---

## 2. Room APIs (Public)

### 2.1 Get All Rooms
**Endpoint:** `GET /api/rooms`

**Response (200 OK):**
```json
[
  {
    "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
    "roomNumber": "101",
    "type": "Deluxe",
    "pricePerNight": 150.00,
    "capacity": 2,
    "available": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-20T14:45:00"
  }
]
```

---

### 2.2 Get Room by ID
**Endpoint:** `GET /api/rooms/{id}`

**Path Parameter:**
- `id` (String): Room ID

**Response (200 OK):**
```json
{
  "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
  "roomNumber": "101",
  "type": "Deluxe",
  "pricePerNight": 150.00,
  "capacity": 2,
  "available": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

---

## 3. Booking APIs

### 3.1 Create Booking
**Endpoint:** `POST /bookings/rooms/{roomId}`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Path Parameter:**
- `roomId` (String): Room ID

**Request Body:**
```json
{
  "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "checkInDate": "2024-03-01",
  "checkOutDate": "2024-03-05"
}
```

**Response (200 OK):**
```json
{
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
  "checkIn": "2024-03-01",
  "checkOut": "2024-03-05",
  "createdAt": "2024-02-21T10:00:00",
  "updatedAt": "2024-02-21T10:00:00",
  "totalAmount": 600.00,
  "status": "CONFIRMED",
  "paymentId": null,
  "paymentDoneAt": null
}
```

---

### 3.2 Cancel Booking
**Endpoint:** `PUT /bookings/{id}/cancel`

**Path Parameter:**
- `id` (String): Booking ID

**Response (200 OK):**
```
"Booking cancelled successfully"
```

---

## 4. Payment APIs

### 4.1 Make Payment
**Endpoint:** `POST /api/payments/pay`

**Request Body:**
```json
{
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "method": "UPI",
  "transactionId": "TXN123456789"
}
```

**Response (200 OK):**
```json
{
  "paymentId": "65d4e5f6g7h8i9j0k1l2m3n4",
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "amount": 600.00,
  "method": "UPI",
  "status": "SUCCESS",
  "transactionId": "TXN123456789",
  "paidAt": "2024-02-21T10:30:00"
}
```

---

### 4.2 Cancel Payment
**Endpoint:** `PUT /api/payments/{paymentId}/cancel`

**Path Parameter:**
- `paymentId` (String): Payment ID

**Response (200 OK):**
```
"Payment and booking cancelled successfully"
```

---

## 5. Profile APIs

### 5.1 Create/Update Profile
**Endpoint:** `POST /api/profile`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Request Body:**
```json
{
  "phone": "+91-9876543210",
  "address": "123 Main Street, City, Country",
  "idProof": "PASSPORT-AB123456",
  "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
}
```

**Response (200 OK):**
```json
{
  "id": "65e5f6g7h8i9j0k1l2m3n4o5",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+91-9876543210",
  "address": "123 Main Street, City, Country",
  "idProof": "PASSPORT-AB123456",
  "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
}
```

---

### 5.2 Get My Profile
**Endpoint:** `GET /api/profile/me`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response (200 OK):**
```json
{
  "id": "65e5f6g7h8i9j0k1l2m3n4o5",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+91-9876543210",
  "address": "123 Main Street, City, Country",
  "idProof": "PASSPORT-AB123456",
  "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
}
```

---

## 6. Invoice APIs

### 6.1 Get Invoice by Booking ID
**Endpoint:** `GET /api/invoices/{bookingId}`

**Path Parameter:**
- `bookingId` (String): Booking ID

**Response (200 OK):**
```json
{
  "id": "65f6g7h8i9j0k1l2m3n4o5p6",
  "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
  "roomAmount": 600.00,
  "finalAmount": 600.00,
  "paymentStatus": "PAID",
  "createdAt": "2024-02-21T10:30:00"
}
```

---

## 7. Contact APIs

### 7.1 Submit Contact Message
**Endpoint:** `POST /api/contact`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phoneNo": "+91-9876543210",
  "subject": "Booking Inquiry",
  "message": "I would like to know about room availability."
}
```

**Response (200 OK):**
```
"Message submitted successfully"
```

---

## 8. Image APIs (Admin Only)

### 8.1 Upload Image
**Endpoint:** `POST /api/images/upload`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: multipart/form-data
```

**Request Body (Form Data):**
- `file` (File): Image file to upload

**Response (200 OK):**
```
"https://s3.amazonaws.com/bucket/images/filename.jpg"
```

---

## 9. Admin Room APIs (Admin Only)

### 9.1 Add Room
**Endpoint:** `POST /api/admin/rooms`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Request Body:**
```json
{
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 250.00,
  "capacity": 4,
  "available": true
}
```

**Response (200 OK):**
```json
{
  "roomId": "65g7h8i9j0k1l2m3n4o5p6q7",
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 250.00,
  "capacity": 4,
  "available": true,
  "createdAt": "2024-02-21T11:00:00",
  "updatedAt": "2024-02-21T11:00:00"
}
```

---

### 9.2 Update Room
**Endpoint:** `PUT /api/admin/rooms/{id}`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameter:**
- `id` (String): Room ID

**Request Body:**
```json
{
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 300.00,
  "capacity": 4,
  "available": false
}
```

**Response (200 OK):**
```json
{
  "roomId": "65g7h8i9j0k1l2m3n4o5p6q7",
  "roomNumber": "102",
  "type": "Suite",
  "pricePerNight": 300.00,
  "capacity": 4,
  "available": false,
  "createdAt": "2024-02-21T11:00:00",
  "updatedAt": "2024-02-21T12:00:00"
}
```

---

### 9.3 Delete Room
**Endpoint:** `DELETE /api/admin/rooms/{id}`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameter:**
- `id` (String): Room ID

**Response (200 OK):**
```
"Room deleted successfully"
```

---

### 9.4 Get All Rooms (Admin)
**Endpoint:** `GET /api/admin/rooms`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
    "roomNumber": "101",
    "type": "Deluxe",
    "pricePerNight": 150.00,
    "capacity": 2,
    "available": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-20T14:45:00"
  }
]
```

---

### 9.5 Get Room by ID (Admin)
**Endpoint:** `GET /api/admin/rooms/{id}`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Path Parameter:**
- `id` (String): Room ID

**Response (200 OK):**
```json
{
  "roomId": "65a1b2c3d4e5f6g7h8i9j0k1",
  "roomNumber": "101",
  "type": "Deluxe",
  "pricePerNight": 150.00,
  "capacity": 2,
  "available": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T14:45:00"
}
```

---

### 9.6 Get All Bookings (Admin)
**Endpoint:** `GET /api/admin/rooms/bookings`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "bookingId": "65c3d4e5f6g7h8i9j0k1l2m3",
    "checkIn": "2024-03-01",
    "checkOut": "2024-03-05",
    "totalAmount": 600.00,
    "bookingStatus": "CONFIRMED",
    "userName": "John Doe",
    "userEmail": "john@example.com",
    "phone": "+91-9876543210",
    "address": "123 Main Street, City, Country",
    "idProof": "PASSPORT-AB123456",
    "roomNumber": "101",
    "roomType": "Deluxe",
    "paymentId": "65d4e5f6g7h8i9j0k1l2m3n4",
    "paymentMethod": "UPI",
    "paymentStatus": "SUCCESS",
    "paymentDate": "2024-02-21T10:30:00"
  }
]
```

---

## 10. Admin Profile APIs (Admin Only)

### 10.1 Get All Profiles
**Endpoint:** `GET /api/profile/all`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Response (200 OK):**
```json
[
  {
    "id": "65e5f6g7h8i9j0k1l2m3n4o5",
    "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+91-9876543210",
    "address": "123 Main Street, City, Country",
    "idProof": "PASSPORT-AB123456",
    "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
  }
]
```

---

### 10.2 Search Profiles by Name
**Endpoint:** `GET /api/profile/search?name={name}`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameter:**
- `name` (String): Name to search for

**Response (200 OK):**
```json
[
  {
    "id": "65e5f6g7h8i9j0k1l2m3n4o5",
    "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+91-9876543210",
    "address": "123 Main Street, City, Country",
    "idProof": "PASSPORT-AB123456",
    "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
  }
]
```

---

### 10.3 Get Profile by Email
**Endpoint:** `GET /api/profile/email?email={email}`

**Headers:**
```
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

**Query Parameter:**
- `email` (String): Email to search for

**Response (200 OK):**
```json
{
  "id": "65e5f6g7h8i9j0k1l2m3n4o5",
  "userId": "65b2c3d4e5f6g7h8i9j0k1l2",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+91-9876543210",
  "address": "123 Main Street, City, Country",
  "idProof": "PASSPORT-AB123456",
  "photoUrl": "https://s3.amazonaws.com/bucket/photo.jpg"
}
```

---

## Error Response Format

All error responses follow this structure:

```json
{
  "message": "Error description",
  "status": 400,
  "timestamp": "2024-02-21T10:00:00"
}
```

**Common HTTP Status Codes:**
- `200 OK` - Success
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Authentication Summary

| Role | Endpoints |
|------|-----------|
| **Public** | `/api/auth/**`, `/api/rooms/**`, `/api/contact/**` |
| **User (JWT)** | `/bookings/**`, `/api/profile/me`, `/api/payments/**`, `/api/invoices/**` |
| **Admin (JWT + ROLE_ADMIN)** | `/api/admin/**`, `/api/images/upload`, `/api/profile/all`, `/api/profile/search`, `/api/profile/email` |
