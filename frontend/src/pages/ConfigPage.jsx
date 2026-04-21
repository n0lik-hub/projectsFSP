import React, { useState } from 'react';
import { useGame } from '../context/GameContext';
import './ConfigPage.css';

const ConfigPage = () => {
  const { configurations, createConfiguration, deactivateConfiguration } = useGame();
  
  const [newConfig, setNewConfig] = useState({
    name: '',
    maxPlayers: 10,
    entryCost: 100,
    prizePoolPercent: 90,
    boostMultiplier: 2.0,
    boostCost: 50,
    waitTimeSeconds: 60
  });
  
  const [formWarnings, setFormWarnings] = useState([]);

  const validateConfig = () => {
    const warnings = [];
    
    if (newConfig.prizePoolPercent < 80) {
      warnings.push('Низкий RTP. Комната может быть непривлекательна для игроков.');
    }
    
    if (newConfig.prizePoolPercent >= 100) {
      warnings.push('Отрицательная или нулевая маржа системы. Комната убыточна.');
    }
    
    if (newConfig.boostCost > newConfig.entryCost * 0.8) {
      warnings.push('Буст слишком дорогой относительно стоимости входа.');
    }
    
    if (newConfig.boostMultiplier < 1.5 || newConfig.boostMultiplier > 5) {
      warnings.push('Коэффициент буста вне рекомендуемого диапазона (1.5-5.0)');
    }
    
    setFormWarnings(warnings);
    return warnings;
  };

  const handleCreateConfig = async () => {
    const warnings = validateConfig();
    
    try {
      await createConfiguration(newConfig);
      setNewConfig({
        name: '',
        maxPlayers: 10,
        entryCost: 100,
        prizePoolPercent: 90,
        boostMultiplier: 2.0,
        boostCost: 50,
        waitTimeSeconds: 60
      });
      setFormWarnings([]);
    } catch (error) {
      alert(error.response?.data?.message || 'Ошибка создания конфигурации');
    }
  };

  const handleDeactivateConfig = async (id) => {
    if (window.confirm('Вы уверены, что хотите деактивировать эту конфигурацию?')) {
      try {
        await deactivateConfiguration(id);
      } catch (error) {
        alert('Ошибка деактивации конфигурации');
      }
    }
  };

  return (
    <div className="config-page">
      <h2 className="page-title">⚙️ Конфигуратор комнат</h2>
      
      <div className="configs-list">
        {configurations.map(config => (
          <div key={config.id} className="config-card">
            <div className="config-header">
              <h3>{config.name}</h3>
              <span className={`status-badge ${config.active ? 'active' : 'inactive'}`}>
                {config.active ? 'Активна' : 'Неактивна'}
              </span>
            </div>
            
            <div className="config-params">
              <div className="param-row">
                <span>👥 Мест:</span>
                <strong>{config.maxPlayers}</strong>
              </div>
              <div className="param-row">
                <span>💰 Вход:</span>
                <strong>{config.entryCost} баллов</strong>
              </div>
              <div className="param-row">
                <span>🏆 Призовой фонд:</span>
                <strong>{config.prizePoolPercent}%</strong>
              </div>
              <div className="param-row">
                <span>🚀 Буст (x{config.boostMultiplier}):</span>
                <strong>{config.boostCost} баллов</strong>
              </div>
              <div className="param-row">
                <span>⏱️ Таймер:</span>
                <strong>{config.waitTimeSeconds} сек</strong>
              </div>
            </div>
            
            <div className="config-metrics">
              <div className="metric">
                <span className="metric-label">RTP:</span>
                <span className={`metric-value ${config.expectedRTP >= 80 ? 'good' : ''}`}>
                  {config.expectedRTP}%
                </span>
              </div>
              <div className="metric">
                <span className="metric-label">Маржа:</span>
                <span className="metric-value">{config.systemMargin}%</span>
              </div>
            </div>
            
            {config.warnings && config.warnings.length > 0 && (
              <div className="config-warnings">
                {config.warnings.map((warning, idx) => (
                  <div key={idx} className="warning">
                    ⚠️ {warning}
                  </div>
                ))}
              </div>
            )}
            
            <div className="config-actions">
              {config.active && (
                <button onClick={() => handleDeactivateConfig(config.id)} className="btn-deactivate">
                  Деактивировать
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
      
      <div className="create-config-section">
        <h3>Создать новую конфигурацию</h3>
        
        <form onSubmit={(e) => { e.preventDefault(); handleCreateConfig(); }} className="config-form">
          <div className="form-row">
            <div className="form-group">
              <label>Название</label>
              <input 
                value={newConfig.name} 
                onChange={(e) => setNewConfig({...newConfig, name: e.target.value})} 
                required 
                placeholder="Например: Премиум гонка" 
              />
            </div>
            <div className="form-group">
              <label>Макс. игроков</label>
              <input 
                type="number" 
                min="2" 
                max="10" 
                value={newConfig.maxPlayers}
                onChange={(e) => setNewConfig({...newConfig, maxPlayers: parseInt(e.target.value)})}
                required 
              />
            </div>
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label>Стоимость входа (баллы)</label>
              <input 
                type="number" 
                min="1" 
                value={newConfig.entryCost}
                onChange={(e) => setNewConfig({...newConfig, entryCost: parseInt(e.target.value)})}
                required 
              />
            </div>
            <div className="form-group">
              <label>Призовой фонд (%)</label>
              <input 
                type="number" 
                min="50" 
                max="100" 
                value={newConfig.prizePoolPercent}
                onChange={(e) => setNewConfig({...newConfig, prizePoolPercent: parseInt(e.target.value)})}
                required 
              />
            </div>
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label>Множитель буста</label>
              <input 
                type="number" 
                step="0.1" 
                min="1" 
                value={newConfig.boostMultiplier}
                onChange={(e) => setNewConfig({...newConfig, boostMultiplier: parseFloat(e.target.value)})}
                required 
              />
            </div>
            <div className="form-group">
              <label>Стоимость буста</label>
              <input 
                type="number" 
                min="0" 
                value={newConfig.boostCost}
                onChange={(e) => setNewConfig({...newConfig, boostCost: parseInt(e.target.value)})}
                required 
              />
            </div>
            <div className="form-group">
              <label>Таймер ожидания (сек)</label>
              <input 
                type="number" 
                min="10" 
                value={newConfig.waitTimeSeconds}
                onChange={(e) => setNewConfig({...newConfig, waitTimeSeconds: parseInt(e.target.value)})}
                required 
              />
            </div>
          </div>
          
          {formWarnings.length > 0 && (
            <div className="form-warnings">
              {formWarnings.map((warning, idx) => (
                <div key={idx} className="form-warning">
                  ⚠️ {warning}
                </div>
              ))}
            </div>
          )}
          
          <button type="submit" className="btn-create-config">Создать конфигурацию</button>
        </form>
      </div>
    </div>
  );
};

export default ConfigPage;
