package com.store.productservice.service;

import com.store.productservice.model.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User saveUser(User user);
}
