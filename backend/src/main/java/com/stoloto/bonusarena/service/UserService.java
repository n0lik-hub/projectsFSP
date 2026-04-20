package com.stoloto.bonusarena.service;

import com.stoloto.bonusarena.model.User;
import com.stoloto.bonusarena.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Создание тестового пользователя (для демонстрации)
     */
    @Transactional
    public User createUser(String username, Boolean isAdmin) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        
        User user = User.builder()
                .username(username)
                .bonusBalance(10000) // Стартовый баланс для демонстрации
                .isAdmin(isAdmin != null ? isAdmin : false)
                .build();
        
        return userRepository.save(user);
    }
    
    /**
     * Получение пользователя по ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
    
    /**
     * Получение пользователя по имени
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Получение всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Обновление баланса пользователя
     */
    @Transactional
    public User updateBalance(Long userId, Integer newBalance) {
        User user = getUserById(userId);
        user.setBonusBalance(newBalance);
        return userRepository.save(user);
    }
}
