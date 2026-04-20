import { defineStore } from 'pinia'
import axios from 'axios'

const API_URL = '/api'

export const useGameStore = defineStore('game', {
  state: () => ({
    users: [],
    configurations: [],
    rooms: [],
    history: [],
    selectedRoom: null,
    gameRunning: false,
    raceProgress: 0,
    raceParticipants: []
  }),

  actions: {
    async loadUsers() {
      try {
        const response = await axios.get(`${API_URL}/users`)
        this.users = response.data
      } catch (error) {
        console.error('Ошибка загрузки пользователей:', error)
      }
    },

    async loadConfigurations() {
      try {
        const response = await axios.get(`${API_URL}/configurations`)
        this.configurations = response.data
      } catch (error) {
        console.error('Ошибка загрузки конфигураций:', error)
      }
    },

    async loadRooms() {
      try {
        const response = await axios.get(`${API_URL}/rooms`)
        this.rooms = response.data
      } catch (error) {
        console.error('Ошибка загрузки комнат:', error)
      }
    },

    async createRoom(configurationId) {
      try {
        const response = await axios.post(`${API_URL}/rooms`, { configurationId })
        await this.loadRooms()
        return response.data
      } catch (error) {
        console.error('Ошибка создания комнаты:', error)
        throw error
      }
    },

    async joinRoom(roomId, userId, buyBoost = false) {
      try {
        const response = await axios.post(`${API_URL}/rooms/${roomId}/join`, {
          userId,
          buyBoost
        })
        await this.loadRooms()
        await this.loadUsers()
        return response.data
      } catch (error) {
        console.error('Ошибка присоединения к комнате:', error)
        throw error
      }
    },

    async buyBoost(roomId, userId) {
      try {
        const response = await axios.post(`${API_URL}/rooms/${roomId}/boost`, {
          userId
        })
        await this.loadRooms()
        await this.loadUsers()
        return response.data
      } catch (error) {
        console.error('Ошибка покупки буста:', error)
        throw error
      }
    },

    async startRound(roomId) {
      try {
        const response = await axios.post(`${API_URL}/rooms/${roomId}/start`)
        await this.loadRooms()
        return response.data
      } catch (error) {
        console.error('Ошибка запуска раунда:', error)
        throw error
      }
    },

    async finishRound(roomId) {
      try {
        const response = await axios.post(`${API_URL}/rooms/${roomId}/finish`)
        await this.loadRooms()
        await this.loadUsers()
        return response.data
      } catch (error) {
        console.error('Ошибка завершения раунда:', error)
        throw error
      }
    },

    async createConfiguration(config) {
      try {
        const response = await axios.post(`${API_URL}/configurations`, config)
        await this.loadConfigurations()
        return response.data
      } catch (error) {
        console.error('Ошибка создания конфигурации:', error)
        throw error
      }
    },

    async updateConfiguration(id, config) {
      try {
        const response = await axios.put(`${API_URL}/configurations/${id}`, config)
        await this.loadConfigurations()
        return response.data
      } catch (error) {
        console.error('Ошибка обновления конфигурации:', error)
        throw error
      }
    },

    async deactivateConfiguration(id) {
      try {
        await axios.delete(`${API_URL}/configurations/${id}`)
        await this.loadConfigurations()
      } catch (error) {
        console.error('Ошибка деактивации конфигурации:', error)
        throw error
      }
    },

    setRaceProgress(progress) {
      this.raceProgress = progress
    },

    setRaceParticipants(participants) {
      this.raceParticipants = participants
    },

    setGameRunning(running) {
      this.gameRunning = running
    }
  }
})
