import React, { useState } from 'react';
import { useGame } from '../context/GameContext';
import RaceAnimation from '../components/RaceAnimation';
import './RoomsPage.css';

const RoomsPage = () => {
  const { 
    rooms, configurations, currentUser, 
    createRoom, joinRoom, buyBoost, startRound, finishRound 
  } = useGame();
  
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [selectedConfigId, setSelectedConfigId] = useState(null);
  const [raceVisible, setRaceVisible] = useState(false);
  const [currentRaceParticipants, setCurrentRaceParticipants] = useState([]);
  const [raceWinner, setRaceWinner] = useState(null);

  const isInRoom = (room) => {
    return room.participants?.some(p => p.displayName === currentUser?.username);
  };

  const hasBoost = (room) => {
    const participant = room.participants?.find(p => p.displayName === currentUser?.username);
    return participant?.hasBoost || false;
  };

  const getStatusText = (status) => {
    const texts = {
      'WAITING': 'Ожидание',
      'STARTING': 'Запуск',
      'RUNNING': 'Гонка',
      'FINISHED': 'Завершена'
    };
    return texts[status] || status;
  };

  const handleCreateFirstRoom = async () => {
    if (configurations.length > 0) {
      setSelectedConfigId(configurations[0].id);
      await createRoom(configurations[0].id);
    }
  };

  const handleCreateRoom = async () => {
    if (selectedConfigId) {
      await createRoom(selectedConfigId);
      setShowCreateModal(false);
    }
  };

  const handleJoinRoom = async (room) => {
    try {
      await joinRoom(room.id, currentUser.id, false);
    } catch (error) {
      alert(error.response?.data?.message || 'Ошибка присоединения');
    }
  };

  const handleBuyBoost = async (room) => {
    try {
      await buyBoost(room.id, currentUser.id);
    } catch (error) {
      alert(error.response?.data?.message || 'Ошибка покупки буста');
    }
  };

  const handleStartRound = async (room) => {
    try {
      await startRound(room.id);
      const updatedRoom = rooms.find(r => r.id === room.id);
      if (updatedRoom) {
        setCurrentRaceParticipants(updatedRoom.participants || []);
        setRaceVisible(true);
      }
    } catch (error) {
      alert('Ошибка запуска раунда');
    }
  };

  const handleFinishRound = async (room) => {
    try {
      await finishRound(room.id);
      const winner = room.participants?.[0];
      setRaceWinner(winner);
      setCurrentRaceParticipants(room.participants || []);
      setRaceVisible(true);
      
      setTimeout(() => {
        alert(`Победитель: ${winner?.displayName}`);
        setRaceVisible(false);
        window.location.reload();
      }, 3000);
    } catch (error) {
      alert('Ошибка завершения раунда');
    }
  };

  return (
    <div className="rooms-page">
      <h2 className="page-title">🎮 Игровые комнаты</h2>
      
      <div className="controls">
        <button onClick={() => setShowCreateModal(true)} className="btn-create">
          + Создать комнату
        </button>
      </div>

      <div className="rooms-grid">
        {rooms.map(room => (
          <div 
            key={room.id} 
            className={`room-card ${room.status.toLowerCase()}`}
          >
            <div className="room-header">
              <h3>{room.configurationName}</h3>
              <span className={`status-badge ${room.status.toLowerCase()}`}>
                {getStatusText(room.status)}
              </span>
            </div>
            
            <div className="room-info">
              <div className="info-row">
                <span>💰 Вход:</span>
                <strong>{room.entryCost} баллов</strong>
              </div>
              <div className="info-row">
                <span>🏆 Призовой фонд:</span>
                <strong>{room.prizePoolPercent}%</strong>
              </div>
              <div className="info-row">
                <span>🚀 Буст:</span>
                <strong>{room.boostCost} балл. (x{room.boostMultiplier})</strong>
              </div>
              <div className="info-row">
                <span>👥 Места:</span>
                <strong>{room.occupiedSeats}/{room.maxPlayers}</strong>
              </div>
            </div>

            <div className="participants-preview">
              {(room.participants || []).slice(0, 5).map(p => (
                <div 
                  key={p.id}
                  className={`participant-chip ${p.isBot ? 'bot' : ''} ${p.hasBoost ? 'boost' : ''}`}
                >
                  {p.displayName}
                  {p.hasBoost && <span className="boost-icon">⚡</span>}
                </div>
              ))}
              {(room.participants || []).length > 5 && (
                <span className="more-participants">
                  +{(room.participants || []).length - 5} ещё
                </span>
              )}
            </div>

            <div className="room-actions">
              {room.status === 'WAITING' && (
                <>
                  <button 
                    onClick={() => handleJoinRoom(room)}
                    className="btn-join"
                    disabled={isInRoom(room)}
                  >
                    {isInRoom(room) ? 'Вы в комнате' : 'Войти'}
                  </button>
                  {isInRoom(room) && !hasBoost(room) && (
                    <button onClick={() => handleBuyBoost(room)} className="btn-boost">
                      ⚡ Купить буст
                    </button>
                  )}
                  {currentUser?.isAdmin && (
                    <button onClick={() => handleStartRound(room)} className="btn-start">
                      ▶️ Запустить
                    </button>
                  )}
                </>
              )}
              {room.status === 'RUNNING' && (
                <button onClick={() => handleFinishRound(room)} className="btn-finish">
                  🏁 Финиш
                </button>
              )}
            </div>
          </div>
        ))}

        {rooms.length === 0 && (
          <div className="no-rooms">
            <p>Нет активных комнат</p>
            <button onClick={handleCreateFirstRoom} className="btn-create-first">
              Создать первую комнату
            </button>
          </div>
        )}
      </div>

      {showCreateModal && (
        <div className="modal-overlay" onClick={() => setShowCreateModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>Создание новой комнаты</h3>
            <div className="form-group">
              <label>Конфигурация:</label>
              <select value={selectedConfigId || ''} onChange={(e) => setSelectedConfigId(parseInt(e.target.value))}>
                {configurations.map(config => (
                  <option key={config.id} value={config.id}>
                    {config.name} - {config.entryCost} баллов
                  </option>
                ))}
              </select>
            </div>
            <div className="modal-actions">
              <button onClick={() => setShowCreateModal(false)} className="btn-cancel">Отмена</button>
              <button onClick={handleCreateRoom} className="btn-confirm">Создать</button>
            </div>
          </div>
        </div>
      )}

      {raceVisible && (
        <RaceAnimation 
          participants={currentRaceParticipants}
          winner={raceWinner}
          onClose={() => setRaceVisible(false)}
        />
      )}
    </div>
  );
};

export default RoomsPage;
