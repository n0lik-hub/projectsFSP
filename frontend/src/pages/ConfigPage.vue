<template>
  <div class="config-page">
    <h2 class="page-title">⚙️ Конфигуратор комнат</h2>
    
    <div class="configs-list">
      <div v-for="config in store.configurations" :key="config.id" class="config-card">
        <div class="config-header">
          <h3>{{ config.name }}</h3>
          <span :class="['status-badge', config.active ? 'active' : 'inactive']">
            {{ config.active ? 'Активна' : 'Неактивна' }}
          </span>
        </div>
        
        <div class="config-params">
          <div class="param-row">
            <span>👥 Мест:</span>
            <strong>{{ config.maxPlayers }}</strong>
          </div>
          <div class="param-row">
            <span>💰 Вход:</span>
            <strong>{{ config.entryCost }} баллов</strong>
          </div>
          <div class="param-row">
            <span>🏆 Призовой фонд:</span>
            <strong>{{ config.prizePoolPercent }}%</strong>
          </div>
          <div class="param-row">
            <span>🚀 Буст (x{{ config.boostMultiplier }}):</span>
            <strong>{{ config.boostCost }} баллов</strong>
          </div>
          <div class="param-row">
            <span>⏱️ Таймер:</span>
            <strong>{{ config.waitTimeSeconds }} сек</strong>
          </div>
        </div>
        
        <div class="config-metrics">
          <div class="metric">
            <span class="metric-label">RTP:</span>
            <span class="metric-value good">{{ config.expectedRTP }}%</span>
          </div>
          <div class="metric">
            <span class="metric-label">Маржа:</span>
            <span class="metric-value">{{ config.systemMargin }}%</span>
          </div>
        </div>
        
        <div class="config-warnings" v-if="config.warnings?.length > 0">
          <div v-for="(warning, idx) in config.warnings" :key="idx" class="warning">
            ⚠️ {{ warning }}
          </div>
        </div>
        
        <div class="config-actions">
          <button @click="deactivateConfig(config.id)" class="btn-deactivate" v-if="config.active">
            Деактивировать
          </button>
        </div>
      </div>
    </div>
    
    <div class="create-config-section">
      <h3>Создать новую конфигурацию</h3>
      
      <form @submit.prevent="createConfig" class="config-form">
        <div class="form-row">
          <div class="form-group">
            <label>Название</label>
            <input v-model="newConfig.name" required placeholder="Например: Премиум гонка" />
          </div>
          <div class="form-group">
            <label>Макс. игроков</label>
            <input v-model.number="newConfig.maxPlayers" type="number" min="2" max="10" required />
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>Стоимость входа (баллы)</label>
            <input v-model.number="newConfig.entryCost" type="number" min="1" required />
          </div>
          <div class="form-group">
            <label>Призовой фонд (%)</label>
            <input v-model.number="newConfig.prizePoolPercent" type="number" min="50" max="100" required />
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>Множитель буста</label>
            <input v-model.number="newConfig.boostMultiplier" type="number" step="0.1" min="1" required />
          </div>
          <div class="form-group">
            <label>Стоимость буста</label>
            <input v-model.number="newConfig.boostCost" type="number" min="0" required />
          </div>
          <div class="form-group">
            <label>Таймер ожидания (сек)</label>
            <input v-model.number="newConfig.waitTimeSeconds" type="number" min="10" required />
          </div>
        </div>
        
        <div class="form-warnings" v-if="formWarnings.length > 0">
          <div v-for="(warning, idx) in formWarnings" :key="idx" class="form-warning">
            ⚠️ {{ warning }}
          </div>
        </div>
        
        <button type="submit" class="btn-create-config">Создать конфигурацию</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useGameStore } from '../store/gameStore'

const store = useGameStore()

const newConfig = ref({
  name: '',
  maxPlayers: 10,
  entryCost: 100,
  prizePoolPercent: 90,
  boostMultiplier: 2.0,
  boostCost: 50,
  waitTimeSeconds: 60
})

const formWarnings = ref([])

const validateConfig = () => {
  formWarnings.value = []
  
  if (newConfig.value.prizePoolPercent < 80) {
    formWarnings.value.push('Низкий RTP. Комната может быть непривлекательна для игроков.')
  }
  
  if (newConfig.value.prizePoolPercent >= 100) {
    formWarnings.value.push('Отрицательная или нулевая маржа системы. Комната убыточна.')
  }
  
  if (newConfig.value.boostCost > newConfig.value.entryCost * 0.8) {
    formWarnings.value.push('Буст слишком дорогой относительно стоимости входа.')
  }
  
  if (newConfig.value.boostMultiplier < 1.5 || newConfig.value.boostMultiplier > 5) {
    formWarnings.value.push('Коэффициент буста вне рекомендуемого диапазона (1.5-5.0)')
  }
}

const createConfig = async () => {
  validateConfig()
  
  try {
    await store.createConfiguration(newConfig.value)
    newConfig.value = {
      name: '',
      maxPlayers: 10,
      entryCost: 100,
      prizePoolPercent: 90,
      boostMultiplier: 2.0,
      boostCost: 50,
      waitTimeSeconds: 60
    }
    formWarnings.value = []
  } catch (error) {
    alert(error.response?.data?.message || 'Ошибка создания конфигурации')
  }
}

const deactivateConfig = async (id) => {
  if (confirm('Вы уверены, что хотите деактивировать эту конфигурацию?')) {
    try {
      await store.deactivateConfiguration(id)
    } catch (error) {
      alert('Ошибка деактивации конфигурации')
    }
  }
}
</script>

<style scoped>
.config-page {
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

.configs-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.config-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
}

.config-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.config-header h3 {
  margin: 0;
  font-size: 1.3rem;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
}

.status-badge.active {
  background: rgba(76, 175, 80, 0.2);
  color: #4caf50;
}

.status-badge.inactive {
  background: rgba(128, 128, 128, 0.2);
  color: #888;
}

.config-params {
  margin-bottom: 1rem;
}

.param-row {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.config-metrics {
  display: flex;
  gap: 2rem;
  padding: 1rem 0;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 1rem;
}

.metric {
  display: flex;
  flex-direction: column;
}

.metric-label {
  font-size: 0.85rem;
  color: #888;
}

.metric-value {
  font-size: 1.2rem;
  font-weight: bold;
}

.metric-value.good {
  color: #4caf50;
}

.config-warnings {
  margin-bottom: 1rem;
}

.warning {
  background: rgba(255, 193, 7, 0.1);
  border-left: 3px solid #ffc107;
  padding: 0.5rem 1rem;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: #ffc107;
}

.config-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-deactivate {
  background: rgba(244, 67, 54, 0.2);
  border: 1px solid #f44336;
  color: #f44336;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
}

.create-config-section {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 2rem;
}

.create-config-section h3 {
  margin-bottom: 1.5rem;
}

.config-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-size: 0.9rem;
  color: #aaa;
}

.form-group input {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  padding: 0.75rem;
  color: white;
  font-size: 1rem;
}

.form-warnings {
  margin: 1rem 0;
}

.form-warning {
  background: rgba(255, 193, 7, 0.1);
  border-left: 3px solid #ffc107;
  padding: 0.75rem 1rem;
  margin-bottom: 0.5rem;
  color: #ffc107;
}

.btn-create-config {
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  border: none;
  color: white;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-size: 1.1rem;
  cursor: pointer;
  align-self: start;
}
</style>
