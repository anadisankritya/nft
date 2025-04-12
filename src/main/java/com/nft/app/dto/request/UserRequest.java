package com.nft.app.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(

    @NotEmpty(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-z][a-z0-9]*$", message = "Username can have only small letters and numbers")
    String username,

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,

    @NotEmpty(message = "Phone number is required")
    @Size(min = 10, message = "Phone number must be at least 10 digits")
    String phoneNo,

    String referralCode,

    @NotEmpty(message = "Email OTP is required")
    @Size(min = 6, message = "Email OTP must be at least 8 digits long")
    String emailOtp,

    @NotEmpty(message = "SMS OTP is required")
    @Size(min = 6, message = "SMS OTP must be at least 8 digits long")
    String smsOtp
) {
}