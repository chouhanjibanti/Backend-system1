# Exception Handling Package

This package provides comprehensive exception handling for the Restaurant Billing System.

## 📁 Package Structure

```
exception/
├── README.md                           # This documentation
├── GlobalExceptionHandler.java          # Global exception handler with @RestControllerAdvice
├── ErrorResponse.java                  # Standard error response DTO
├── CustomValidationMessages.java      # Centralized validation messages
├── ResourceNotFoundException.java      # 404 Not Found exceptions
├── BusinessException.java              # 400 Bad Request business logic errors
├── ValidationException.java             # 422 Unprocessable Entity validation errors
├── EmailServiceException.java           # 500 Internal Server Error email failures
├── PDFGenerationException.java         # 500 Internal Server Error PDF failures
└── DuplicateResourceException.java     # 409 Conflict duplicate resource errors
```

## 🚀 Features

### ✅ Global Exception Handling
- **@RestControllerAdvice** handles all exceptions globally
- **Consistent error response format** across all endpoints
- **Proper HTTP status codes** for different exception types
- **Detailed error information** with timestamps and paths

### ✅ Custom Exception Types
- **ResourceNotFoundException** - When resources are not found (404)
- **BusinessException** - Business logic violations (400)
- **ValidationException** - Input validation failures (422)
- **EmailServiceException** - Email sending failures (500)
- **PDFGenerationException** - PDF generation failures (500)
- **DuplicateResourceException** - Duplicate resource attempts (409)

### ✅ Standardized Error Response
```json
{
  "timestamp": "2024-03-09T11:30:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Order not found with id : 123",
  "path": "/api/orders/123"
}
```

## 🔧 Usage Examples

### 1. Throwing Custom Exceptions

```java
// In Service Layer
public Order addItem(Long orderId, Long menuItemId, int qty) {
    if (qty <= 0) {
        throw new ValidationException(CustomValidationMessages.INVALID_QUANTITY);
    }
    
    Order order = orderRepo.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
    
    if (!menuItem.isAvailable()) {
        throw new BusinessException(CustomValidationMessages.MENU_ITEM_NOT_AVAILABLE);
    }
    
    // ... business logic
}
```

### 2. Exception Handling in Controllers

```java
// No need for try-catch in controllers - GlobalExceptionHandler handles it!
@PostMapping("/{id}/add-item")
public Order addItem(@PathVariable Long id, @RequestBody OrderItemRequest request) {
    // GlobalExceptionHandler will catch and handle any exceptions
    return orderService.addItem(id, request.getMenuItemId(), request.getQuantity());
}
```

### 3. Using Custom Validation Messages

```java
// Centralized messages for consistency
throw new BusinessException(CustomValidationMessages.ORDER_ALREADY_PAID);
throw new ValidationException(CustomValidationMessages.INVALID_PRICE);
throw new EmailServiceException(CustomValidationMessages.EMAIL_SENDING_FAILED);
```

## 📋 Exception Types & HTTP Status Codes

| Exception Type | HTTP Status | Use Case |
|----------------|-------------|----------|
| `ResourceNotFoundException` | 404 | Entity not found in database |
| `BusinessException` | 400 | Business rule violations |
| `ValidationException` | 422 | Input validation failures |
| `DuplicateResourceException` | 409 | Duplicate resource creation |
| `EmailServiceException` | 500 | Email sending failures |
| `PDFGenerationException` | 500 | PDF generation failures |
| `MethodArgumentNotValidException` | 400 | Jakarta validation failures |
| `ConstraintViolationException` | 400 | Database constraint violations |
| `DataIntegrityViolationException` | 409 | Database integrity violations |
| `MethodArgumentTypeMismatchException` | 400 | Type conversion failures |
| `HttpMessageNotReadableException` | 400 | Malformed JSON requests |
| `Exception` | 500 | Catch-all for unexpected errors |

## 🎯 Best Practices

### ✅ DO
- Use specific exception types for different error scenarios
- Provide meaningful error messages using `CustomValidationMessages`
- Let exceptions propagate to controllers (don't catch and rethrow unnecessarily)
- Use proper HTTP status codes for each exception type
- Include context in error messages (resource name, field name, values)

### ❌ DON'T
- Use generic `RuntimeException` for business logic errors
- Catch exceptions just to rethrow them
- Return null instead of throwing exceptions for missing resources
- Use different exception types for the same error scenario
- Include sensitive information in error messages

## 🔍 Error Response Format

All errors follow this consistent format:

```json
{
  "timestamp": "2024-03-09T11:30:00.123",    // When error occurred
  "status": 404,                                // HTTP status code
  "error": "Resource Not Found",               // Error category
  "message": "Order not found with id : 123",   // Detailed message
  "path": "/api/orders/123"                    // Request path
}
```

## 🧪 Testing Exception Handling

### Example Test Case

```java
@Test
void testAddItem_InvalidQuantity_ShouldThrowValidationException() {
    // Given
    Long orderId = 1L;
    Long menuItemId = 1L;
    int invalidQty = -1;
    
    // When & Then
    assertThrows(ValidationException.class, () -> {
        orderService.addItem(orderId, menuItemId, invalidQty);
    });
}

@Test
void testAddItem_OrderNotFound_ShouldThrowResourceNotFoundException() {
    // Given
    Long nonExistentOrderId = 999L;
    Long menuItemId = 1L;
    int validQty = 2;
    
    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> {
        orderService.addItem(nonExistentOrderId, menuItemId, validQty);
    });
}
```

## 🔄 Integration with Frontend

The frontend can handle different error types based on HTTP status codes:

```javascript
// Frontend error handling
try {
    const response = await orderService.addItem(orderId, menuItemId, quantity);
    // Handle success
} catch (error) {
    if (error.response.status === 404) {
        // Handle Resource Not Found
        showError('Order or menu item not found');
    } else if (error.response.status === 400) {
        // Handle Business Logic Error
        showError(error.response.data.message);
    } else if (error.response.status === 422) {
        // Handle Validation Error
        showError('Invalid input: ' + error.response.data.message);
    } else {
        // Handle other errors
        showError('An unexpected error occurred');
    }
}
```

## 📝 Adding New Exceptions

1. **Create new exception class:**
```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NewCustomException extends RuntimeException {
    public NewCustomException(String message) {
        super(message);
    }
}
```

2. **Add message to CustomValidationMessages:**
```java
public static final String NEW_ERROR_MESSAGE = "Description of the new error";
```

3. **Add handler in GlobalExceptionHandler:**
```java
@ExceptionHandler(NewCustomException.class)
public ResponseEntity<ErrorResponse> handleNewCustomException(
        NewCustomException ex, WebRequest request) {
    // Handler implementation
}
```

This comprehensive exception handling system ensures consistent error responses, proper HTTP status codes, and maintainable error management across the entire Restaurant Billing System! 🎉
