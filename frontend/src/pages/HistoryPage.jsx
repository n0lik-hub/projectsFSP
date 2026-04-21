import React from 'react';
import './HistoryPage.css';

const HistoryPage = () => {
  return (
    <div className="history-page">
      <h2 className="page-title">📊 История раундов</h2>
      
      <div className="history-info">
        <p>Здесь будет отображаться история всех проведённых раундов.</p>
        <p className="note">Для просмотра истории завершите хотя бы один раунд в игровых комнатах.</p>
      </div>
    </div>
  );
};

export default HistoryPage;
