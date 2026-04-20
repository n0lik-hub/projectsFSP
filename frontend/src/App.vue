<template>
  <div class="app">
    <header class="header">
      <div class="header-content">
        <h1 class="logo">🚀 VIP Бонус-Арена</h1>
        <div class="user-info" v-if="currentUser">
          <span class="balance">💰 {{ currentUser.bonusBalance }} баллов</span>
          <select v-model="selectedUserId" @change="switchUser" class="user-select">
            <option v-for="user in users" :key="user.id" :value="user.id">
              {{ user.username }} {{ user.isAdmin ? '(Админ)' : '' }}
            </option>
          </select>
        </div>
      </div>
    </header>

    <nav class="nav">
      <button 
        :class="['nav-btn', { active: currentPage === 'rooms' }]" 
        @click="currentPage = 'rooms'"
      >
        🎮 Игровые комнаты
      </button>
      <button 
        :class="['nav-btn', { active: currentPage === 'config' }]" 
        @click="currentPage = 'config'"
        v-if="currentUser?.isAdmin"
      >
        ⚙️ Конфигуратор
      </button>
      <button 
        :class="['nav-btn', { active: currentPage === 'history' }]" 
        @click="currentPage = 'history'"
      >
        📊 История
      </button>
    </nav>

    <main class="main-content">
      <RoomsPage 
        v-if="currentPage === 'rooms'" 
        :current-user="currentUser"
        @refresh="loadData"
      />
      <ConfigPage 
        v-if="currentPage === 'config'" 
        @refresh="loadData"
      />
      <HistoryPage 
        v-if="currentPage === 'history'"
      />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useGameStore } from './store/gameStore'
import RoomsPage from './pages/RoomsPage.vue'
import ConfigPage from './pages/ConfigPage.vue'
import HistoryPage from './pages/HistoryPage.vue'

const gameStore = useGameStore()
const currentPage = ref('rooms')
const selectedUserId = ref(null)

const users = computed(() => gameStore.users)
const currentUser = computed(() => 
  users.value.find(u => u.id === selectedUserId.value) || users.value[0]
)

const loadData = async () => {
  await gameStore.loadUsers()
  await gameStore.loadConfigurations()
  await gameStore.loadRooms()
}

const switchUser = () => {
  localStorage.setItem('selectedUserId', selectedUserId.value)
}

onMounted(async () => {
  await loadData()
  const savedUserId = localStorage.getItem('selectedUserId')
  if (savedUserId) {
    selectedUserId.value = parseInt(savedUserId)
  } else if (users.value.length > 0) {
    selectedUserId.value = users.value[0].id
  }
})
</script>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: rgba(0, 0, 0, 0.3);
  padding: 1rem 2rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.8rem;
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.balance {
  background: rgba(0, 212, 255, 0.2);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  border: 1px solid rgba(0, 212, 255, 0.3);
}

.user-select {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
}

.nav {
  background: rgba(0, 0, 0, 0.2);
  padding: 1rem 2rem;
  display: flex;
  gap: 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.nav-btn {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 1rem;
}

.nav-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.nav-btn.active {
  background: linear-gradient(90deg, #00d4ff, #7b2cbf);
}

.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}
</style>
