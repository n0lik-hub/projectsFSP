import React, { useState, useEffect } from 'react';
import { useGame } from '../context/GameContext';
import RoomsPage from './pages/RoomsPage';
import ConfigPage from './pages/ConfigPage';
import HistoryPage from './pages/HistoryPage';
import './App.css';

const App = () => {
  const { users, currentUser, loadUsers, loadConfigurations, loadRooms, switchUser } = useGame();
  const [currentPage, setCurrentPage] = useState('rooms');
  const [selectedUserId, setSelectedUserId] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      await loadUsers();
      await loadConfigurations();
      await loadRooms();
    };
    loadData();

    const savedUserId = localStorage.getItem('selectedUserId');
    if (savedUserId) {
      setSelectedUserId(parseInt(savedUserId));
    }
  }, [loadUsers, loadConfigurations, loadRooms]);

  const handleSwitchUser = (e) => {
    const userId = parseInt(e.target.value);
    setSelectedUserId(userId);
    switchUser(userId);
  };

  return (
    <div className="app">
      <header className="header">
        <div className="header-content">
          <h1 className="logo">🚀 VIP Бонус-Арена</h1>
          {currentUser && (
            <div className="user-info">
              <span className="balance">💰 {currentUser.bonusBalance} баллов</span>
              <select 
                value={selectedUserId || ''} 
                onChange={handleSwitchUser} 
                className="user-select"
              >
                {users.map(user => (
                  <option key={user.id} value={user.id}>
                    {user.username} {user.isAdmin ? '(Админ)' : ''}
                  </option>
                ))}
              </select>
            </div>
          )}
        </div>
      </header>

      <nav className="nav">
        <button 
          className={`nav-btn ${currentPage === 'rooms' ? 'active' : ''}`} 
          onClick={() => setCurrentPage('rooms')}
        >
          🎮 Игровые комнаты
        </button>
        {currentUser?.isAdmin && (
          <button 
            className={`nav-btn ${currentPage === 'config' ? 'active' : ''}`} 
            onClick={() => setCurrentPage('config')}
          >
            ⚙️ Конфигуратор
          </button>
        )}
        <button 
          className={`nav-btn ${currentPage === 'history' ? 'active' : ''}`} 
          onClick={() => setCurrentPage('history')}
        >
          📊 История
        </button>
      </nav>

      <main className="main-content">
        {currentPage === 'rooms' && <RoomsPage />}
        {currentPage === 'config' && currentUser?.isAdmin && <ConfigPage />}
        {currentPage === 'history' && <HistoryPage />}
      </main>
    </div>
  );
};

export default App;
