import React, { useState, useEffect } from 'react';
import './RaceAnimation.css';

const RaceAnimation = ({ participants, winner, onClose }) => {
  const [raceProgress, setRaceProgress] = useState(0);
  const [raceFinished, setRaceFinished] = useState(false);
  const [participantsWithPositions, setParticipantsWithPositions] = useState([]);

  useEffect(() => {
    // Initialize positions
    setParticipantsWithPositions((participants || []).map(p => ({
      ...p,
      position: 0
    })));
    
    // Start animation
    animateRace();
  }, [participants]);

  const animateRace = () => {
    let progress = 0;
    const duration = 5000; // 5 seconds
    
    const interval = setInterval(() => {
      progress += 100 / (duration / 50);
      
      if (progress >= 100) {
        progress = 100;
        clearInterval(interval);
        setRaceFinished(true);
        
        // Set final positions
        setParticipantsWithPositions(prev => prev.map((p, index) => {
          if (winner && p.id === winner.id) {
            return { ...p, position: 90 };
          } else {
            return { ...p, position: 70 - (index * 5) };
          }
        }));
      } else {
        setRaceProgress(progress);
        
        // Update positions with some randomness
        setParticipantsWithPositions(prev => prev.map((p, index) => {
          const baseProgress = progress;
          const variation = Math.sin(progress / 10 + index) * 5;
          return { ...p, position: Math.min(85, baseProgress + variation) };
        }));
      }
    }, 50);
  };

  const isWinner = (participant) => {
    return winner && participant.id === winner.id;
  };

  return (
    <div className="race-container" onClick={onClose}>
      <div className="race-overlay" onClick={(e) => e.stopPropagation()}>
        <h2 className="race-title">🚀 Космический Слалом</h2>
        
        <div className="race-track">
          {participantsWithPositions.map((participant, index) => (
            <div key={participant.id} className="racer-lane">
              <div className="racer-info">
                <span className={`racer-name ${participant.isBot ? 'bot' : ''} ${isWinner(participant) ? 'winner' : ''}`}>
                  {participant.displayName}
                  {participant.hasBoost && <span className="boost-badge">⚡</span>}
                  {isWinner(participant) && <span className="winner-badge">🏆</span>}
                </span>
                <span className="racer-weight">Вес: {participant.weight}</span>
              </div>
              
              <div className="lane-track">
                <div className="finish-line"></div>
                <div 
                  className={`spaceship ${participant.hasBoost ? 'boost-active' : ''}`}
                  style={{ left: `${participant.position || 0}%` }}
                >
                  🚀
                </div>
              </div>
            </div>
          ))}
        </div>
        
        <div className="race-status">
          {!raceFinished ? (
            <div className="countdown">
              <span>Гонка в процессе...</span>
              <div className="progress-bar">
                <div className="progress-fill" style={{ width: `${raceProgress}%` }}></div>
              </div>
            </div>
          ) : (
            <div className="winner-announcement">
              <h3>🎉 Победитель!</h3>
              <p className="winner-name">{winner?.displayName}</p>
              {winner?.isBot && (
                <p className="bot-winner-note">Призовой фонд остался у системы</p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default RaceAnimation;
