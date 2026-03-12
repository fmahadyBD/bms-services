package com.fmahadybd.bms_services.auth;

import org.springframework.security.core.userdetails.UserDetails;
import java.security.Principal;

public interface BaseUser extends UserDetails, Principal {
    String getUserType();
    Long getId();
    String getEmail(); // Add this
    String getName(); // Add this
    String getPassword(); // Already in UserDetails
    String getUsername(); // Already in UserDetails
}