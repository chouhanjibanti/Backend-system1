package com.restaurant.billing.exception;

public class CustomValidationMessages {
    
    // Order Related Messages
    public static final String ORDER_NOT_FOUND = "Order not found with the provided ID";
    public static final String ORDER_ALREADY_CHECKED_OUT = "Order has already been checked out";
    public static final String ORDER_ALREADY_PAID = "Order has already been paid";
    public static final String ORDER_CANNOT_BE_MODIFIED = "Order cannot be modified after payment";
    public static final String ORDER_EMPTY = "Order cannot be created without items";
    
    // Menu Item Related Messages
    public static final String MENU_ITEM_NOT_FOUND = "Menu item not found with the provided ID";
    public static final String MENU_ITEM_NOT_AVAILABLE = "Menu item is not available";
    public static final String MENU_ITEM_ALREADY_EXISTS = "Menu item with this name already exists";
    public static final String INVALID_PRICE = "Price must be greater than zero";
    public static final String INVALID_QUANTITY = "Quantity must be greater than zero";
    
    // Payment Related Messages
    public static final String INVALID_PAYMENT_METHOD = "Invalid payment method provided";
    public static final String PAYMENT_AMOUNT_INVALID = "Payment amount is invalid";
    public static final String PAYMENT_FAILED = "Payment processing failed";
    
    // Email Related Messages
    public static final String EMAIL_SENDING_FAILED = "Failed to send email";
    public static final String INVALID_EMAIL_ADDRESS = "Invalid email address provided";
    public static final String EMAIL_SERVICE_UNAVAILABLE = "Email service is currently unavailable";
    
    // PDF Related Messages
    public static final String PDF_GENERATION_FAILED = "Failed to generate PDF";
    public static final String PDF_TEMPLATE_NOT_FOUND = "PDF template not found";
    
    // General Messages
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String UNAUTHORIZED_ACCESS = "You are not authorized to perform this action";
    public static final String FORBIDDEN_ACCESS = "Access to this resource is forbidden";
    public static final String INVALID_REQUEST = "Invalid request parameters";
    public static final String SERVER_ERROR = "An unexpected error occurred";
}
