package com.stoloto.bonusarena.config;

import com.stoloto.bonusarena.model.RoomConfiguration;
import com.stoloto.bonusarena.model.User;
import com.stoloto.bonusarena.repository.RoomConfigurationRepository;
import com.stoloto.bonusarena.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final RoomConfigurationRepository configRepository;
    
    @Override
    public void run(String... args) {
        log.info("Инициализация демонстрационных данных...");
        
        // Создание тестовых пользователей
        createTestUsers();
        
        // Создание конфигураций комнат по умолчанию
        createDefaultConfigurations();
        
        log.info("Инициализация завершена");
    }
    
    private void createTestUsers() {
        if (!userRepository.existsByUsername("player1")) {
            User player1 = User.builder()
                    .username("player1")
                    .bonusBalance(10000)
                    .isAdmin(false)
                    .build();
            userRepository.save(player1);
            log.info("Создан тестовый игрок: player1");
        }
        
        if (!userRepository.existsByUsername("player2")) {
            User player2 = User.builder()
                    .username("player2")
                    .bonusBalance(10000)
                    .isAdmin(false)
                    .build();
            userRepository.save(player2);
            log.info("Создан тестовый игрок: player2");
        }
        
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .bonusBalance(50000)
                    .isAdmin(true)
                    .build();
            userRepository.save(admin);
            log.info("Создан администратор: admin");
        }
    }
    
    private void createDefaultConfigurations() {
        if (configRepository.count() == 0) {
            // Конфигурация 1: Стандартная комната
            RoomConfiguration standard = RoomConfiguration.builder()
                    .name("Стандартная гонка")
                    .maxPlayers(10)
                    .entryCost(100)
                    .prizePoolPercent(90)
                    .boostMultiplier(2.0)
                    .boostCost(50)
                    .waitTimeSeconds(60)
                    .active(true)
                    .build();
            configRepository.save(standard);
            log.info("Создана конфигурация: Стандартная гонка");
            
            // Конфигурация 2: Быстрая комната
            RoomConfiguration fast = RoomConfiguration.builder()
                    .name("Быстрая гонка")
                    .maxPlayers(6)
                    .entryCost(50)
                    .prizePoolPercent(85)
                    .boostMultiplier(1.5)
                    .boostCost(25)
                    .waitTimeSeconds(30)
                    .active(true)
                    .build();
            configRepository.save(fast);
            log.info("Создана конфигурация: Быстрая гонка");
            
            // Конфигурация 3: VIP комната
            RoomConfiguration vip = RoomConfiguration.builder()
                    .name("VIP гонка")
                    .maxPlayers(5)
                    .entryCost(500)
                    .prizePoolPercent(95)
                    .boostMultiplier(3.0)
                    .boostCost(200)
                    .waitTimeSeconds(90)
                    .active(true)
                    .build();
            configRepository.save(vip);
            log.info("Создана конфигурация: VIP гонка");
        }
    }
}
