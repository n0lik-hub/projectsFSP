<template>
  <div class="race-container" @click="$emit('close')">
    <div class="race-overlay">
      <h2 class="race-title">🚀 Космический Слалом</h2>
      
      <div class="race-track">
        <div 
          v-for="(participant, index) in participantsWithPositions" 
          :key="participant.id"
          class="racer-lane"
        >
          <div class="racer-info">
            <span class="racer-name" :class="{ bot: participant.isBot, winner: isWinner(participant) }">
              {{ participant.displayName }}
              <span v-if="participant.hasBoost" class="boost-badge">⚡</span>
              <span v-if="isWinner(participant)" class="winner-badge">🏆</span>
            </span>
            <span class="racer-weight">Вес: {{ participant.weight }}</span>
          </div>
          
          <div class="lane-track">
            <div class="finish-line"></div>
            <div 
              class="spaceship" 
              :class="{ 'boost-active': participant.hasBoost }"
              :style="{ left: `${participant.position || 0}%` }"
            >
              🚀
            </div>
          </div>
        </div>
      </div>
      
      <div class="race-status">
        <div v-if="!raceFinished" class="countdown">
          <span>Гонка в процессе...</span>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: `${raceProgress}%` }"></div>
          </div>
        </div>
        <div v-else class="winner-announcement">
          <h3>🎉 Победитель!</h3>
          <p class="winner-name">{{ winner?.displayName }}</p>
          <p v-if="winner?.isBot" class="bot-winner-note">Призовой фонд остался у системы</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  participants: Array,
  winner: Object
})

const emit = defineEmits(['close'])

const raceProgress = ref(0)
const raceFinished = ref(false)
const participantsWithPositions = ref([])

// Инициализация позиций участников
onMounted(() => {
  participantsWithPositions.value = (props.participants || []).map(p => ({
    ...p,
    position: 0
  }))
  
  // Анимация гонки
  animateRace()
})

const animateRace = () => {
  let progress = 0
  const duration = 5000 // 5 секунд на анимацию
  
  const interval = setInterval(() => {
    progress += 100 / (duration / 50)
    
    if (progress >= 100) {
      progress = 100
      clearInterval(interval)
      raceFinished.value = true
      
      // Устанавливаем финальные позиции
      participantsWithPositions.value.forEach((p, index) => {
        if (props.winner && p.id === props.winner.id) {
          p.position = 90
        } else {
          p.position = 70 - (index * 5)
        }
      })
    } else {
      raceProgress.value = progress
      
      // Обновляем позиции с некоторой случайностью
      participantsWithPositions.value.forEach((p, index) => {
        const baseProgress = progress
        const variation = Math.sin(progress / 10 + index) * 5
        p.position = Math.min(85, baseProgress + variation)
      })
    }
  }, 50)
}

const isWinner = (participant) => {
  return props.winner && participant.id === props.winner.id
}
</script>

<style scoped>
.race-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  cursor: pointer;
}

.race-overlay {
  background: linear-gradient(135deg, #0c0c1e 0%, #1a1a3e 100%);
  border-radius: 24px;
  padding: 2rem;
  min-width: 800px;
  max-width: 90vw;
  border: 2px solid rgba(0, 212, 255, 0.3);
  box-shadow: 0 0 60px rgba(0, 212, 255, 0.2);
  cursor: default;
}

.race-title {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 2rem;
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.race-track {
  background: rgba(0, 0, 0, 0.3);
  border-radius: 16px;
  padding: 1rem;
  margin-bottom: 2rem;
}

.racer-lane {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  padding: 0.5rem 0;
}

.racer-lane:last-child {
  margin-bottom: 0;
}

.racer-info {
  width: 200px;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.racer-name {
  font-weight: bold;
  font-size: 1.1rem;
}

.racer-name.bot {
  color: #888;
}

.racer-name.winner {
  color: #ffd700;
  font-size: 1.3rem;
}

.boost-badge {
  margin-left: 0.5rem;
  font-size: 0.9rem;
}

.winner-badge {
  margin-left: 0.5rem;
}

.racer-weight {
  font-size: 0.85rem;
  color: #888;
}

.lane-track {
  flex: 1;
  position: relative;
  height: 50px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 25px;
  overflow: hidden;
}

.finish-line {
  position: absolute;
  right: 50px;
  top: 0;
  bottom: 0;
  width: 4px;
  background: repeating-linear-gradient(
    0deg,
    #fff 0px,
    #fff 10px,
    #000 10px,
    #000 20px
  );
}

.spaceship {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  font-size: 2rem;
  transition: left 0.1s ease-out;
  filter: drop-shadow(0 0 10px rgba(0, 212, 255, 0.5));
}

.spaceship.boost-active {
  filter: drop-shadow(0 0 20px rgba(255, 215, 0, 0.8));
}

.race-status {
  text-align: center;
  padding: 1rem;
}

.countdown {
  font-size: 1.2rem;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  margin-top: 1rem;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  transition: width 0.1s;
}

.winner-announcement h3 {
  font-size: 2rem;
  margin-bottom: 1rem;
  color: #ffd700;
}

.winner-name {
  font-size: 1.5rem;
  color: #00d4ff;
  margin-bottom: 0.5rem;
}

.bot-winner-note {
  color: #888;
  font-style: italic;
}
</style>
