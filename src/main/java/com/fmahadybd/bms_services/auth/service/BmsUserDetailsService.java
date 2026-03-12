package com.fmahadybd.bms_services.auth.service;

import com.fmahadybd.bms_services.manager.repository.ManagerRepository;
import com.fmahadybd.bms_services.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BmsUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First try to find as student
        var student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return student.get();
        }

        // Then try as manager
        var manager = managerRepository.findByEmail(email);
        if (manager.isPresent()) {
            return manager.get();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}