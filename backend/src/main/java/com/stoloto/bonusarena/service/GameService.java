package com.stoloto.bonusarena.service;

import com.stoloto.bonusarena.dto.*;
import com.stoloto.bonusarena.model.*;
import com.stoloto.bonusarena.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    
    private final GameRoomRepository gameRoomRepository;
    private final RoomConfigurationRepository configRepository;
    private final GameParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final RoundHistoryRepository historyRepository;
    
    private final Random random = new Random();
    
    // Список имён для ботов
    private static final String[] BOT_NAMES = {
        "Бот-Альфа", "Бот-Омега", "Бот-Вега", "Бот-Сириус", "Бот-Орион",
        "Бот-Квант", "Бот-Небула", "Бот-Пульсар", "Бот-Комета", "Бот-Метеор"
    };
    
    /**
     * Создание новой игровой комнаты
     */
    @Transactional
    public GameRoom createRoom(Long configurationId) {
        RoomConfiguration config = configRepository.findById(configurationId)
                .orElseThrow(() -> new RuntimeException("Конфигурация не найдена"));
        
        GameRoom room = GameRoom.builder()
                .configuration(config)
                .status(GameRoom.RoomStatus.WAITING)
                .build();
        
        return gameRoomRepository.save(room);
    }
    
    /**
     * Присоединение игрока к комнате
     */
    @Transactional
    public GameParticipant joinRoom(Long roomId, Long userId, boolean buyBoost) {
        GameRoom room = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        
        if (room.getStatus() != GameRoom.RoomStatus.WAITING) {
            throw new RuntimeException("Комната недоступна для присоединения");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        int totalCost = room.getConfiguration().getEntryCost();
        if (buyBoost) {
            totalCost += room.getConfiguration().getBoostCost();
        }
        
        if (user.getBonusBalance() < totalCost) {
            throw new RuntimeException("Недостаточно бонусных баллов");
        }
        
        // Проверка, есть ли свободные места
        int occupiedSeats = (int) room.getParticipants().stream()
                .filter(p -> p.getUser() != null || !p.isBot())
                .count();
        
        if (occupiedSeats >= room.getConfiguration().getMaxPlayers()) {
            throw new RuntimeException("Нет свободных мест");
        }
        
        // Списываем баллы
        user.setBonusBalance(user.getBonusBalance() - totalCost);
        userRepository.save(user);
        
        // Создаём участника
        GameParticipant participant = GameParticipant.builder()
                .gameRoom(room)
                .user(user)
                .isBot(false)
                .hasBoost(buyBoost)
                .joinedAt(LocalDateTime.now())
                .build();
        
        participant.calculateWeight(room.getConfiguration().getBoostMultiplier());
        
        room.getParticipants().add(participant);
        gameRoomRepository.save(room);
        
        log.info("Игрок {} присоединился к комнате {}", user.getUsername(), roomId);
        
        return participant;
    }
    
    /**
     * Заполнение комнаты ботами и запуск раунда
     */
    @Transactional
    public GameRoom startRound(Long roomId) {
        GameRoom room = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        
        if (room.getStatus() != GameRoom.RoomStatus.WAITING) {
            throw new RuntimeException("Комната уже запущена или завершена");
        }
        
        RoomConfiguration config = room.getConfiguration();
        int maxPlayers = config.getMaxPlayers();
        int currentPlayers = room.getParticipants().size();
        
        // Добавляем ботов
        List<String> availableBotNames = new ArrayList<>(List.of(BOT_NAMES));
        for (int i = currentPlayers; i < maxPlayers; i++) {
            String botName = availableBotNames.get(random.nextInt(availableBotNames.size()));
            
            GameParticipant bot = GameParticipant.builder()
                    .gameRoom(room)
                    .isBot(true)
                    .botName(botName)
                    .hasBoost(false)
                    .joinedAt(LocalDateTime.now())
                    .build();
            
            bot.calculateWeight(config.getBoostMultiplier());
            room.getParticipants().add(bot);
        }
        
        // Рассчитываем призовой фонд
        int totalEntries = (int) room.getParticipants().stream()
                .filter(p -> !p.isBot())
                .count();
        int totalPool = totalEntries * config.getEntryCost();
        room.setTotalPrizePool((int) (totalPool * (config.getPrizePoolPercent() / 100.0)));
        
        // Обновляем веса всех участников
        for (GameParticipant participant : room.getParticipants()) {
            participant.calculateWeight(config.getBoostMultiplier());
        }
        
        room.setStatus(GameRoom.RoomStatus.RUNNING);
        room.setStartedAt(LocalDateTime.now());
        
        gameRoomRepository.save(room);
        
        log.info("Раунд в комнате {} запущен с {} участниками", roomId, room.getParticipants().size());
        
        return room;
    }
    
    /**
     * Определение победителя на основе весов
     */
    public GameParticipant determineWinner(GameRoom room) {
        List<GameParticipant> participants = room.getParticipants();
        
        // Сумма весов всех участников
        int totalWeight = participants.stream()
                .mapToInt(GameParticipant::getWeight)
                .sum();
        
        // Генерируем случайное число от 0 до totalWeight
        int randomValue = random.nextInt(totalWeight);
        
        // Находим победителя
        int cumulativeWeight = 0;
        GameParticipant winner = null;
        
        for (GameParticipant participant : participants) {
            cumulativeWeight += participant.getWeight();
            if (randomValue < cumulativeWeight) {
                winner = participant;
                break;
            }
        }
        
        log.info("Победитель раунда {}: {} (вес: {}, шанс: {:.1f}%)", 
                room.getId(), 
                winner.getDisplayName(),
                winner.getWeight(),
                (winner.getWeight() * 100.0) / totalWeight);
        
        return winner;
    }
    
    /**
     * Завершение раунда и начисление выигрыша
     */
    @Transactional
    public GameRoom finishRound(Long roomId, GameParticipant winner) {
        GameRoom room = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        
        room.setStatus(GameRoom.RoomStatus.FINISHED);
        room.setFinishedAt(LocalDateTime.now());
        room.setWinnerId(winner.getId());
        
        gameRoomRepository.save(room);
        
        // Если победитель бот - призовой фонд остаётся у системы
        if (!winner.isBot()) {
            User winnerUser = winner.getUser();
            winnerUser.setBonusBalance(winnerUser.getBonusBalance() + room.getTotalPrizePool());
            userRepository.save(winnerUser);
            
            log.info("Победитель {} получил {} бонусных баллов", 
                    winnerUser.getUsername(), room.getTotalPrizePool());
        } else {
            log.info("Победил бот {}. Призовой фонд {} остался у системы", 
                    winner.getDisplayName(), room.getTotalPrizePool());
        }
        
        // Сохраняем историю раунда
        saveRoundHistory(room, winner);
        
        return room;
    }
    
    /**
     * Сохранение истории раунда
     */
    private void saveRoundHistory(GameRoom room, GameParticipant winner) {
        RoomConfiguration config = room.getConfiguration();
        
        int systemCommission = room.getTotalPrizePool() > 0 
                ? (int) (room.getParticipants().stream()
                        .filter(p -> !p.isBot())
                        .count() * config.getEntryCost()) - room.getTotalPrizePool()
                : 0;
        
        String participantsInfo = room.getParticipants().stream()
                .map(p -> String.format("%s (бот: %s, буст: %s, вес: %d)", 
                        p.getDisplayName(), p.isBot(), p.hasBoost(), p.getWeight()))
                .collect(Collectors.joining("; "));
        
        RoundHistory history = RoundHistory.builder()
                .gameRoom(room)
                .startTime(room.getStartedAt())
                .endTime(room.getFinishedAt())
                .entryCost(config.getEntryCost())
                .prizePool(room.getTotalPrizePool())
                .boostMultiplier(config.getBoostMultiplier())
                .participantsInfo(participantsInfo)
                .winnerId(winner.getId())
                .winnerName(winner.getDisplayName())
                .winnerIsBot(winner.isBot())
                .systemCommission(systemCommission)
                .build();
        
        historyRepository.save(history);
    }
    
    /**
     * Получение списка комнат
     */
    public List<RoomDto> getAllRooms() {
        return gameRoomRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Получение комнаты по ID
     */
    public RoomDto getRoomById(Long roomId) {
        GameRoom room = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        return convertToDto(room);
    }
    
    /**
     * Конвертация GameRoom в RoomDto
     */
    private RoomDto convertToDto(GameRoom room) {
        RoomConfiguration config = room.getConfiguration();
        
        List<RoomDto.ParticipantDto> participantDtos = room.getParticipants().stream()
                .map(p -> RoomDto.ParticipantDto.builder()
                        .id(p.getId())
                        .displayName(p.getDisplayName())
                        .isBot(p.isBot())
                        .hasBoost(p.hasBoost())
                        .weight(p.getWeight())
                        .build())
                .collect(Collectors.toList());
        
        return RoomDto.builder()
                .id(room.getId())
                .configurationName(config.getName())
                .maxPlayers(config.getMaxPlayers())
                .entryCost(config.getEntryCost())
                .prizePoolPercent(config.getPrizePoolPercent())
                .boostMultiplier(config.getBoostMultiplier())
                .boostCost(config.getBoostCost())
                .waitTimeSeconds(config.getWaitTimeSeconds())
                .occupiedSeats(room.getParticipants().size())
                .status(room.getStatus().name())
                .createdAt(room.getCreatedAt())
                .participants(participantDtos)
                .build();
    }
    
    /**
     * Покупка буста игроком
     */
    @Transactional
    public GameParticipant buyBoost(Long roomId, Long userId) {
        GameRoom room = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        
        if (room.getStatus() != GameRoom.RoomStatus.WAITING) {
            throw new RuntimeException("Нельзя купить буст после начала раунда");
        }
        
        GameParticipant participant = room.getParticipants().stream()
                .filter(p -> p.getUser() != null && p.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Вы не участвуете в этой комнате"));
        
        if (participant.hasBoost()) {
            throw new RuntimeException("Буст уже куплен");
        }
        
        User user = participant.getUser();
        int boostCost = room.getConfiguration().getBoostCost();
        
        if (user.getBonusBalance() < boostCost) {
            throw new RuntimeException("Недостаточно бонусных баллов");
        }
        
        user.setBonusBalance(user.getBonusBalance() - boostCost);
        userRepository.save(user);
        
        participant.setHasBoost(true);
        participant.setBoughtBoost(true);
        participant.calculateWeight(room.getConfiguration().getBoostMultiplier());
        
        participantRepository.save(participant);
        
        log.info("Игрок {} купил буст в комнате {}", user.getUsername(), roomId);
        
        return participant;
    }
}
