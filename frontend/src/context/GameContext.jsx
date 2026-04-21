import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import axios from 'axios';

const API_URL = '/api';

const GameContext = createContext(null);

export const useGame = () => {
  const context = useContext(GameContext);
  if (!context) {
    throw new Error('useGame must be used within a GameProvider');
  }
  return context;
};

export const GameProvider = ({ children }) => {
  const [users, setUsers] = useState([]);
  const [configurations, setConfigurations] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [raceProgress, setRaceProgress] = useState(0);
  const [raceParticipants, setRaceParticipants] = useState([]);
  const [gameRunning, setGameRunning] = useState(false);

  const loadUsers = useCallback(async () => {
    try {
      const response = await axios.get(`${API_URL}/users`);
      setUsers(response.data);
    } catch (error) {
      console.error('Ошибка загрузки пользователей:', error);
    }
  }, []);

  const loadConfigurations = useCallback(async () => {
    try {
      const response = await axios.get(`${API_URL}/configurations`);
      setConfigurations(response.data);
    } catch (error) {
      console.error('Ошибка загрузки конфигураций:', error);
    }
  }, []);

  const loadRooms = useCallback(async () => {
    try {
      const response = await axios.get(`${API_URL}/rooms`);
      setRooms(response.data);
    } catch (error) {
      console.error('Ошибка загрузки комнат:', error);
    }
  }, []);

  const createRoom = useCallback(async (configurationId) => {
    const response = await axios.post(`${API_URL}/rooms`, { configurationId });
    await loadRooms();
    return response.data;
  }, [loadRooms]);

  const joinRoom = useCallback(async (roomId, userId, buyBoost = false) => {
    const response = await axios.post(`${API_URL}/rooms/${roomId}/join`, {
      userId,
      buyBoost
    });
    await loadRooms();
    await loadUsers();
    return response.data;
  }, [loadRooms, loadUsers]);

  const buyBoost = useCallback(async (roomId, userId) => {
    const response = await axios.post(`${API_URL}/rooms/${roomId}/boost`, {
      userId
    });
    await loadRooms();
    await loadUsers();
    return response.data;
  }, [loadRooms, loadUsers]);

  const startRound = useCallback(async (roomId) => {
    const response = await axios.post(`${API_URL}/rooms/${roomId}/start`);
    await loadRooms();
    return response.data;
  }, [loadRooms]);

  const finishRound = useCallback(async (roomId) => {
    const response = await axios.post(`${API_URL}/rooms/${roomId}/finish`);
    await loadRooms();
    await loadUsers();
    return response.data;
  }, [loadRooms, loadUsers]);

  const createConfiguration = useCallback(async (config) => {
    const response = await axios.post(`${API_URL}/configurations`, config);
    await loadConfigurations();
    return response.data;
  }, [loadConfigurations]);

  const deactivateConfiguration = useCallback(async (id) => {
    await axios.delete(`${API_URL}/configurations/${id}`);
    await loadConfigurations();
  }, [loadConfigurations]);

  useEffect(() => {
    const savedUserId = localStorage.getItem('selectedUserId');
    if (savedUserId) {
      setSelectedUserId(parseInt(savedUserId));
    }
  }, []);

  const switchUser = (userId) => {
    setSelectedUserId(userId);
    localStorage.setItem('selectedUserId', userId);
  };

  const value = {
    users,
    configurations,
    rooms,
    selectedUserId,
    currentUser: users.find(u => u.id === selectedUserId) || users[0],
    raceProgress,
    raceParticipants,
    gameRunning,
    loadUsers,
    loadConfigurations,
    loadRooms,
    createRoom,
    joinRoom,
    buyBoost,
    startRound,
    finishRound,
    createConfiguration,
    deactivateConfiguration,
    switchUser,
    setRaceProgress,
    setRaceParticipants,
    setGameRunning
  };

  return <GameContext.Provider value={value}>{children}</GameContext.Provider>;
};
