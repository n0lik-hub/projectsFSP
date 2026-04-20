<template>
  <div class="history-page">
    <h2 class="page-title">📊 История раундов</h2>
    
    <div class="history-info">
      <p>Здесь будет отображаться история всех проведённых раундов.</p>
      <p class="note">Для просмотра истории завершите хотя бы один раунд в игровых комнатах.</p>
    </div>
    
    <div class="history-list" v-if="false">
      <!-- Заглушка для будущей реализации -->
    </div>
  </div>
</template>

<script setup>
// Простая страница-заглушка для истории
</script>

<style scoped>
.history-page {
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

.history-info {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 3rem;
  text-align: center;
}

.history-info p {
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

.note {
  color: #888;
  font-style: italic;
}
</style>
