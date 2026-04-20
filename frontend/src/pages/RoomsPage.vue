<template>
  <div class="rooms-page">
    <h2 class="page-title">🎮 Игровые комнаты</h2>
    
    <div class="controls">
      <button @click="showCreateModal = true" class="btn-create">
        + Создать комнату
      </button>
    </div>

    <div class="rooms-grid">
      <div 
        v-for="room in store.rooms" 
        :key="room.id" 
        class="room-card"
        :class="{ 'running': room.status === 'RUNNING', 'finished': room.status === 'FINISHED' }"
      >
        <div class="room-header">
          <h3>{{ room.configurationName }}</h3>
          <span :class="['status-badge', room.status.toLowerCase()]">
            {{ getStatusText(room.status) }}
          </span>
        </div>
        
        <div class="room-info">
          <div class="info-row">
            <span>💰 Вход:</span>
            <strong>{{ room.entryCost }} баллов</strong>
          </div>
          <div class="info-row">
            <span>🏆 Призовой фонд:</span>
            <strong>{{ room.prizePoolPercent }}%</strong>
          </div>
          <div class="info-row">
            <span>🚀 Буст:</span>
            <strong>{{ room.boostCost }} балл. (x{{ room.boostMultiplier }})</strong>
          </div>
          <div class="info-row">
            <span>👥 Места:</span>
            <strong>{{ room.occupiedSeats }}/{{ room.maxPlayers }}</strong>
          </div>
        </div>

        <div class="participants-preview" v-if="room.participants?.length > 0">
          <div 
            v-for="p in room.participants.slice(0, 5)" 
            :key="p.id"
            class="participant-chip"
            :class="{ bot: p.isBot, boost: p.hasBoost }"
          >
            {{ p.displayName }}
            <span v-if="p.hasBoost" class="boost-icon">⚡</span>
          </div>
          <span v-if="room.participants.length > 5" class="more-participants">
            +{{ room.participants.length - 5 }} ещё
          </span>
        </div>

        <div class="room-actions">
          <button 
            v-if="room.status === 'WAITING'" 
            @click="joinRoom(room)"
            class="btn-join"
            :disabled="isInRoom(room)"
          >
            {{ isInRoom(room) ? 'Вы в комнате' : 'Войти' }}
          </button>
          <button 
            v-if="room.status === 'WAITING' && isInRoom(room) && !hasBoost(room)" 
            @click="buyBoost(room)"
            class="btn-boost"
          >
            ⚡ Купить буст
          </button>
          <button 
            v-if="room.status === 'WAITING' && currentUser?.isAdmin" 
            @click="startRound(room)"
            class="btn-start"
          >
            ▶️ Запустить
          </button>
          <button 
            v-if="room.status === 'RUNNING'" 
            @click="finishRound(room)"
            class="btn-finish"
          >
            🏁 Финиш
          </button>
        </div>
      </div>

      <div v-if="store.rooms.length === 0" class="no-rooms">
        <p>Нет активных комнат</p>
        <button @click="createFirstRoom" class="btn-create-first">
          Создать первую комнату
        </button>
      </div>
    </div>

    <!-- Модальное окно создания комнаты -->
    <div v-if="showCreateModal" class="modal-overlay" @click="showCreateModal = false">
      <div class="modal" @click.stop>
        <h3>Создание новой комнаты</h3>
        <div class="form-group">
          <label>Конфигурация:</label>
          <select v-model="selectedConfigId">
            <option v-for="config in store.configurations" :key="config.id" :value="config.id">
              {{ config.name }} - {{ config.entryCost }} баллов
            </option>
          </select>
        </div>
        <div class="modal-actions">
          <button @click="showCreateModal = false" class="btn-cancel">Отмена</button>
          <button @click="createRoom" class="btn-confirm">Создать</button>
        </div>
      </div>
    </div>

    <!-- Экран гонки -->
    <RaceAnimation 
      v-if="raceVisible" 
      :participants="currentRaceParticipants"
      :winner="raceWinner"
      @close="raceVisible = false"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useGameStore } from '../store/gameStore'
import RaceAnimation from '../components/RaceAnimation.vue'

const props = defineProps({
  currentUser: Object
})

const emit = defineEmits(['refresh'])

const store = useGameStore()
const showCreateModal = ref(false)
const selectedConfigId = ref(null)
const raceVisible = ref(false)
const currentRaceParticipants = ref([])
const raceWinner = ref(null)

const isInRoom = (room) => {
  return room.participants?.some(p => p.displayName === props.currentUser?.username)
}

const hasBoost = (room) => {
  const participant = room.participants?.find(p => p.displayName === props.currentUser?.username)
  return participant?.hasBoost || false
}

const getStatusText = (status) => {
  const texts = {
    'WAITING': 'Ожидание',
    'STARTING': 'Запуск',
    'RUNNING': 'Гонка',
    'FINISHED': 'Завершена'
  }
  return texts[status] || status
}

const createFirstRoom = async () => {
  if (store.configurations.length > 0) {
    selectedConfigId.value = store.configurations[0].id
    await store.createRoom(selectedConfigId.value)
  }
}

const createRoom = async () => {
  if (selectedConfigId.value) {
    await store.createRoom(selectedConfigId.value)
    showCreateModal.value = false
  }
}

const joinRoom = async (room) => {
  try {
    await store.joinRoom(room.id, props.currentUser.id, false)
  } catch (error) {
    alert(error.response?.data?.message || 'Ошибка присоединения')
  }
}

const buyBoost = async (room) => {
  try {
    await store.buyBoost(room.id, props.currentUser.id)
  } catch (error) {
    alert(error.response?.data?.message || 'Ошибка покупки буста')
  }
}

const startRound = async (room) => {
  try {
    await store.startRound(room.id)
    // Запускаем анимацию гонки
    const updatedRoom = store.rooms.find(r => r.id === room.id)
    if (updatedRoom) {
      currentRaceParticipants.value = updatedRoom.participants || []
      raceVisible.value = true
    }
  } catch (error) {
    alert('Ошибка запуска раунда')
  }
}

const finishRound = async (room) => {
  try {
    const result = await store.finishRound(room.id)
    // Определяем победителя для анимации
    const winner = room.participants?.[0] // Упрощённо
    raceWinner.value = winner
    currentRaceParticipants.value = room.participants || []
    raceVisible.value = true
    
    setTimeout(() => {
      alert(`Победитель: ${winner?.displayName}`)
      raceVisible.value = false
      emit('refresh')
    }, 3000)
  } catch (error) {
    alert('Ошибка завершения раунда')
  }
}
</script>

<style scoped>
.rooms-page {
  padding: 1rem;
}

.page-title {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.controls {
  margin-bottom: 2rem;
}

.btn-create {
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  border: none;
  color: white;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-size: 1.1rem;
  cursor: pointer;
  transition: transform 0.2s;
}

.btn-create:hover {
  transform: scale(1.05);
}

.rooms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.room-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
  transition: all 0.3s;
}

.room-card:hover {
  background: rgba(255, 255, 255, 0.08);
  transform: translateY(-2px);
}

.room-card.running {
  border-color: #00d4ff;
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.3);
}

.room-card.finished {
  border-color: #7b2cbf;
  opacity: 0.7;
}

.room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.room-header h3 {
  font-size: 1.3rem;
  margin: 0;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  text-transform: uppercase;
}

.status-badge.waiting {
  background: rgba(255, 193, 7, 0.2);
  color: #ffc107;
}

.status-badge.running {
  background: rgba(0, 212, 255, 0.2);
  color: #00d4ff;
}

.status-badge.finished {
  background: rgba(123, 44, 191, 0.2);
  color: #7b2cbf;
}

.room-info {
  margin-bottom: 1rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.participants-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
  min-height: 2rem;
}

.participant-chip {
  background: rgba(255, 255, 255, 0.1);
  padding: 0.25rem 0.75rem;
  border-radius: 15px;
  font-size: 0.85rem;
}

.participant-chip.bot {
  background: rgba(128, 128, 128, 0.3);
  color: #aaa;
}

.participant-chip.boost {
  border: 1px solid #ffd700;
}

.boost-icon {
  margin-left: 0.25rem;
}

.more-participants {
  color: #888;
  font-size: 0.85rem;
  align-self: center;
}

.room-actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.room-actions button {
  flex: 1;
  min-width: 100px;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-join {
  background: linear-gradient(90deg, #00d4ff, #0099cc);
  color: white;
}

.btn-boost {
  background: linear-gradient(90deg, #ffd700, #ffaa00);
  color: black;
}

.btn-start {
  background: linear-gradient(90deg, #4caf50, #45a049);
  color: white;
}

.btn-finish {
  background: linear-gradient(90deg, #f44336, #da190b);
  color: white;
}

.room-actions button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.no-rooms {
  grid-column: 1 / -1;
  text-align: center;
  padding: 4rem 2rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
}

.btn-create-first {
  margin-top: 1rem;
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  border: none;
  color: white;
  padding: 1rem 2rem;
  border-radius: 12px;
  cursor: pointer;
}

/* Модальное окно */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: #1a1a3e;
  padding: 2rem;
  border-radius: 16px;
  min-width: 400px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.modal h3 {
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group select {
  width: 100%;
  padding: 0.75rem;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.btn-cancel, .btn-confirm {
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  border: none;
  cursor: pointer;
}

.btn-cancel {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.btn-confirm {
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  color: white;
}
</style>
