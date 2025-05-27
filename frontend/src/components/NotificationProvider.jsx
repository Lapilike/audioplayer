import React, { createContext, useContext, useState } from 'react';

const NotificationContext = createContext();

export const useNotification = () => {
    const context = useContext(NotificationContext);
    if (!context) {
        throw new Error('useNotification must be used within a NotificationProvider');
    }
    return context;
};

export const NotificationProvider = ({ children }) => {
    const [notification, setNotification] = useState(null);

    const showNotification = (message, type = 'info') => {
        setNotification({ message, type });
        // Скрываем через 3 секунды
        setTimeout(() => setNotification(null), 3000);
    };

    return (
        <NotificationContext.Provider value={{ showNotification }}>
            {children}
            {notification && (
                <div style={{
                    position: 'fixed',
                    bottom: 20,
                    left: '50%',
                    transform: 'translateX(-50%)',
                    background: getBackgroundColor(notification.type),
                    color: '#fff',
                    padding: '12px 24px',
                    borderRadius: '8px',
                    boxShadow: '0 4px 12px rgba(0,0,0,0.2)',
                    zIndex: 1000,
                    fontSize: '16px',
                    minWidth: '200px',
                    textAlign: 'center',
                    transition: 'opacity 0.3s ease',
                }}>
                    {notification.message}
                </div>
            )}
        </NotificationContext.Provider>
    );
};

const getBackgroundColor = (type) => {
    switch (type) {
        case 'success': return '#4caf50';
        case 'error': return '#f44336';
        case 'info': return '#2196f3';
        case 'warning': return '#ff9800';
        default: return '#333';
    }
};
