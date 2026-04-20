package com.stoloto.bonusarena.service;

import com.stoloto.bonusarena.dto.*;
import com.stoloto.bonusarena.model.RoomConfiguration;
import com.stoloto.bonusarena.repository.RoomConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigurationService {
    
    private final RoomConfigurationRepository configRepository;
    
    /**
     * Получение всех активных конфигураций
     */
    public List<RoomConfigurationDto> getAllConfigurations() {
        return configRepository.findByActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Создание новой конфигурации комнаты
     */
    @Transactional
    public RoomConfigurationDto createConfiguration(CreateRoomConfigRequest request) {
        // Валидация и сбор предупреждений
        List<String> warnings = new ArrayList<>();
        
        if (request.getEntryCost() <= 0) {
            throw new RuntimeException("Стоимость входа должна быть положительной");
        }
        
        if (request.getMaxPlayers() < 2 || request.getMaxPlayers() > 10) {
            throw new RuntimeException("Количество мест должно быть от 2 до 10");
        }
        
        if (request.getPrizePoolPercent() < 50 || request.getPrizePoolPercent() > 100) {
            throw new RuntimeException("Процент призового фонда должен быть от 50 до 100");
        }
        
        if (request.getBoostMultiplier() < 1.5 || request.getBoostMultiplier() > 5.0) {
            warnings.add("Коэффициент буста вне рекомендуемого диапазона (1.5-5.0)");
        }
        
        // Проверка привлекательности для игроков
        double expectedRTP = request.getPrizePoolPercent();
        if (expectedRTP < 80) {
            warnings.add("Низкий RTP (" + expectedRTP + "%). Комната может быть непривлекательна для игроков.");
        }
        
        // Проверка выгоды для системы
        double systemMargin = 100.0 - request.getPrizePoolPercent();
        if (systemMargin <= 0) {
            warnings.add("Отрицательная или нулевая маржа системы. Комната убыточна.");
        }
        
        // Проверка стоимости буста
        if (request.getBoostCost() > request.getEntryCost() * 0.8) {
            warnings.add("Буст слишком дорогой относительно стоимости входа.");
        }
        
        RoomConfiguration config = RoomConfiguration.builder()
                .name(request.getName())
                .maxPlayers(request.getMaxPlayers())
                .entryCost(request.getEntryCost())
                .prizePoolPercent(request.getPrizePoolPercent())
                .boostMultiplier(request.getBoostMultiplier())
                .boostCost(request.getBoostCost())
                .waitTimeSeconds(request.getWaitTimeSeconds())
                .active(true)
                .build();
        
        config = configRepository.save(config);
        
        log.info("Создана новая конфигурация комнаты: {}", config.getName());
        
        RoomConfigurationDto dto = convertToDto(config);
        dto.setWarnings(warnings);
        
        return dto;
    }
    
    /**
     * Обновление конфигурации
     */
    @Transactional
    public RoomConfigurationDto updateConfiguration(Long id, CreateRoomConfigRequest request) {
        RoomConfiguration config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Конфигурация не найдена"));
        
        config.setName(request.getName());
        config.setMaxPlayers(request.getMaxPlayers());
        config.setEntryCost(request.getEntryCost());
        config.setPrizePoolPercent(request.getPrizePoolPercent());
        config.setBoostMultiplier(request.getBoostMultiplier());
        config.setBoostCost(request.getBoostCost());
        config.setWaitTimeSeconds(request.getWaitTimeSeconds());
        
        config = configRepository.save(config);
        
        log.info("Обновлена конфигурация комнаты: {}", config.getName());
        
        return convertToDto(config);
    }
    
    /**
     * Деактивация конфигурации
     */
    @Transactional
    public void deactivateConfiguration(Long id) {
        RoomConfiguration config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Конфигурация не найдена"));
        
        config.setActive(false);
        configRepository.save(config);
        
        log.info("Деактивирована конфигурация: {}", config.getName());
    }
    
    /**
     * Конвертация в DTO
     */
    private RoomConfigurationDto convertToDto(RoomConfiguration config) {
        return RoomConfigurationDto.builder()
                .id(config.getId())
                .name(config.getName())
                .maxPlayers(config.getMaxPlayers())
                .entryCost(config.getEntryCost())
                .prizePoolPercent(config.getPrizePoolPercent())
                .boostMultiplier(config.getBoostMultiplier())
                .boostCost(config.getBoostCost())
                .waitTimeSeconds(config.getWaitTimeSeconds())
                .active(config.getActive())
                .expectedRTP(config.getExpectedRTP())
                .systemMargin(config.getSystemMargin())
                .warnings(new ArrayList<>())
                .build();
    }
}
