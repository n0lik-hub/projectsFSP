# VIP Бонус-Арена «Космический Слалом»

## Описание проекта

MVP веб-приложения быстрых бонусных игр для VIP-аудитории компании «Столото». Приложение представляет собой систему игровых комнат с короткими раундами, где участники соревнуются за призовой фонд, используя бонусные баллы.

**Игровая метафора:** «Космический Слалом» — гонка космических кораблей через астероидное поле.

## Структура проекта

```
/workspace
├── backend/                 # Spring Boot бэкенд
│   ├── src/main/java/com/stoloto/bonusarena/
│   │   ├── model/          # Entity классы
│   │   ├── repository/     # JPA репозитории
│   │   ├── service/        # Бизнес-логика
│   │   ├── controller/     # REST API контроллеры
│   │   ├── config/         # Конфигурация
│   │   └── dto/            # Data Transfer Objects
│   ├── pom.xml
│   └── src/main/resources/
│       └── application.properties
│
└── frontend/               # Vue.js фронтенд
    ├── src/
    │   ├── components/     # Vue компоненты
    │   ├── pages/          # Страницы приложения
    │   ├── store/          # Pinia store
    │   └── App.vue
    ├── package.json
    └── vite.config.js
```

## Быстрый старт

### Требования

- Java 17+
- Node.js 18+
- Maven 3.8+

### Запуск бэкенда

```bash
cd /workspace/backend
mvn spring-boot:run
```

Бэкенд будет доступен по адресу: http://localhost:8080

### Запуск фронтенда

```bash
cd /workspace/frontend
npm install
npm run dev
```

Фронтенд будет доступен по адресу: http://localhost:3000

---

## 🪟 Руководство по запуску в Windows

Данный раздел содержит пошаговые инструкции для запуска приложения «VIP Бонус-Арена» на операционной системе Windows.

### Шаг 1: Установка необходимых компонентов

#### 1.1. Установка Java Development Kit (JDK) 17

1. Перейдите на официальный сайт [Adoptium](https://adoptium.net/) или [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
2. Скачайте установщик JDK 17 для Windows (файл `.msi`)
3. Запустите установщик и следуйте инструкциям мастера установки
4. После установки откройте командную строку (`Win + R` → введите `cmd` → нажмите Enter)
5. Проверьте версию Java:
   ```cmd
   java -version
   ```
   Должна отобразиться версия 17 или выше

6. **Настройка переменной окружения JAVA_HOME** (если не настроена автоматически):
   - Нажмите `Win + X` → выберите «Система»
   - Нажмите «Дополнительные параметры системы»
   - Нажмите кнопку «Переменные среды»
   - В разделе «Системные переменные» нажмите «Создать»
   - Имя переменной: `JAVA_HOME`
   - Значение: путь к установке JDK (например, `C:\Program Files\Java\jdk-17`)
   - Нажмите OK
   - Найдите переменную `Path` в системных переменных, выберите её и нажмите «Изменить»
   - Добавьте новую строку: `%JAVA_HOME%\bin`
   - Сохраните изменения

#### 1.2. Установка Apache Maven

1. Перейдите на сайт [Apache Maven](https://maven.apache.org/download.cgi)
2. Скачайте архив с последней версией Maven (файл `.zip`)
3. Распакуйте архив в удобную директорию, например: `C:\Program Files\Apache\Maven`
4. Настройте переменные окружения:
   - Откройте «Переменные среды» (как описано выше)
   - Создайте системную переменную `MAVEN_HOME`
   - Имя: `MAVEN_HOME`
   - Значение: `C:\Program Files\Apache\Maven` (путь к распакованной директории)
   - Измените переменную `Path`, добавив: `%MAVEN_HOME%\bin`
5. Проверьте установку:
   ```cmd
   mvn -version
   ```

#### 1.3. Установка Node.js и npm

1. Перейдите на официальный сайт [Node.js](https://nodejs.org/)
2. Скачайте LTS-версию (рекомендуется версия 18 или выше)
3. Запустите установщик и следуйте инструкциям
4. Убедитесь, что опция «Automatically install the necessary tools» выбрана
5. После установки перезапустите командную строку
6. Проверьте версии:
   ```cmd
   node -v
   npm -v
   ```

### Шаг 2: Подготовка проекта

#### 2.1. Клонирование репозитория (если необходимо)

Если проект ещё не загружен:

```cmd
git clone <URL_репозитория>
cd VIP-Bonus-Arena
```

Или скачайте ZIP-архив проекта и распакуйте его в удобную директорию, например: `C:\Projects\VIP-Bonus-Arena`

#### 2.2. Проверка структуры проекта

Убедитесь, что у вас есть следующая структура:

```
VIP-Bonus-Arena/
├── backend/
│   ├── pom.xml
│   └── src/
└── frontend/
    ├── package.json
    └── src/
```

### Шаг 3: Запуск бэкенда (Spring Boot)

1. Откройте командную строку (`cmd`) или PowerShell
2. Перейдите в директорию бэкенда:
   ```cmd
   cd C:\Projects\VIP-Bonus-Arena\backend
   ```
3. Очистите и соберите проект (первый запуск):
   ```cmd
   mvn clean install -DskipTests
   ```
4. Запустите приложение:
   ```cmd
   mvn spring-boot:run
   ```

   **Альтернативный способ** (если установлен Maven wrapper):
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```

5. Дождитесь сообщения об успешном запуске:
   ```
   Started BonusArenaApplication in X.XXX seconds
   Tomcat started on port(s): 8080 (http)
   ```

6. **Важно:** Не закрывайте это окно терминала — бэкенд должен работать постоянно

7. Проверьте доступность API, открыв в браузере: http://localhost:8080/api/users

### Шаг 4: Запуск фронтенда (Vue.js)

1. Откройте **новое** окно командной строки или PowerShell (не закрывая бэкенд)
2. Перейдите в директорию фронтенда:
   ```cmd
   cd C:\Projects\VIP-Bonus-Arena\frontend
   ```
3. Установите зависимости (только при первом запуске или после обновления `package.json`):
   ```cmd
   npm install
   ```
   
   ⏱️ Этот процесс может занять несколько минут в зависимости от скорости интернета
   
4. Запустите сервер разработки:
   ```cmd
   npm run dev
   ```

5. Дождитесь сообщения:
   ```
   VITE ready in XXX ms
   ➜  Local:   http://localhost:3000/
   ➜  Network: use --host to expose
   ```

6. Откройте браузер и перейдите по адресу: http://localhost:3000

### Шаг 5: Проверка работоспособности

1. **Проверка бэкенда:**
   - Откройте http://localhost:8080/api/configurations
   - Должен вернуться JSON со списком конфигураций комнат

2. **Проверка фронтенда:**
   - Убедитесь, что главная страница загрузилась
   - Выберите пользователя из выпадающего списка (player1, player2, admin)
   - Проверьте отображение баланса

3. **Тестовый сценарий:**
   - Войдите под пользователем `admin`
   - Создайте новую комнату
   - Переключитесь на `player1` в другой вкладке браузера
   - Присоединитесь к созданной комнате
   - Вернитесь к `admin` и запустите раунд

### Решение распространённых проблем

#### Проблема 1: «java не является внутренней или внешней командой»

**Решение:**
- Проверьте, что Java установлена: `C:\Program Files\Java\jdk-17\bin\java.exe`
- Убедитесь, что переменная `JAVA_HOME` настроена правильно
- Перезапустите командную строку после настройки переменных окружения

#### Проблема 2: Ошибка при запуске Maven «Unrecognized VM option»

**Решение:**
- Убедитесь, что используется JDK 17, а не JRE
- Проверьте: `java -version`
- При необходимости переустановите JDK

#### Проблема 3: npm install завершается ошибкой

**Решение:**
- Очистите кэш npm: `npm cache clean --force`
- Удалите папку `node_modules` и файл `package-lock.json`
- Повторите: `npm install`
- Если проблема с сетью, попробуйте настроить прокси или использовать VPN

#### Проблема 4: Порт 8080 или 3000 уже занят

**Решение:**
- Закройте другие приложения, использующие эти порты
- Для проверки порта выполните:
  ```cmd
  netstat -ano | findstr :8080
  ```
- Завершите процесс:
  ```cmd
  taskkill /PID <PID> /F
  ```
- Или измените порт в конфигурации:
  - Бэкенд: отредактируйте `application.properties`, добавьте `server.port=8081`
  - Фронтенд: в `vite.config.js` измените порт

#### Проблема 5: Браузер блокирует соединение с бэкендом (CORS)

**Решение:**
- Убедитесь, что бэкенд запущен и доступен
- Проверьте настройки CORS в `WebConfig.java`
- Попробуйте открыть приложение в режиме инкогнито

#### Проблема 6: H2 база данных не создаётся

**Решение:**
- Проверьте права на запись в директорию проекта
- Убедитесь, что антивирус не блокирует создание файлов
- Очистите директорию с базой данных (обычно в `target/` или корне проекта)

### Дополнительные команды для Windows

#### Сборка production-версии фронтенда

```cmd
cd C:\Projects\VIP-Bonus-Arena\frontend
npm run build
```

Собранные файлы появятся в папке `dist/`

#### Создание JAR-файла бэкенда

```cmd
cd C:\Projects\VIP-Bonus-Arena\backend
mvn clean package -DskipTests
```

JAR-файл будет создан в `target/` директории

#### Запуск бэкенда из JAR

```cmd
java -jar target/bonus-arena-0.0.1-SNAPSHOT.jar
```

### Использование PowerShell вместо cmd

Все команды совместимы с PowerShell. Альтернативные команды PowerShell:

```powershell
# Переход в директорию
Set-Location C:\Projects\VIP-Bonus-Arena\backend

# Запуск бэкенда
mvn spring-boot:run

# Проверка порта
Get-NetTCPConnection -LocalPort 8080

# Завершение процесса по порту
Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess -Force
```

### Автоматизация запуска (опционально)

Создайте BAT-файл `start-backend.bat` в корне проекта:

```batch
@echo off
echo Запуск бэкенда VIP Бонус-Арена...
cd /d %~dp0backend
call mvn spring-boot:run
pause
```

И `start-frontend.bat`:

```batch
@echo off
echo Запуск фронтенда VIP Бонус-Арена...
cd /d %~dp0frontend
call npm run dev
pause
```

Запускайте файлы двойным кликом или из командной строки.

---

## 🐧 Руководство по запуску в Linux

Данный раздел содержит пошаговые инструкции для запуска приложения «VIP Бонус-Арена» на операционной системе Linux (Ubuntu/Debian, CentOS/RHEL, Fedora).

### Шаг 1: Установка необходимых компонентов

#### 1.1. Установка Java Development Kit (JDK) 17

**Для Ubuntu/Debian:**

```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
```

**Для CentOS/RHEL:**

```bash
sudo yum install java-17-openjdk-devel -y
```

**Для Fedora:**

```bash
sudo dnf install java-17-openjdk-devel -y
```

**Проверка установки:**

```bash
java -version
javac -version
```

Должна отобразиться версия 17 или выше.

**Настройка JAVA_HOME (опционально):**

```bash
# Найдите путь установки Java
update-alternatives --config java

# Добавьте в ~/.bashrc или ~/.profile
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Примените изменения
source ~/.bashrc
```

#### 1.2. Установка Apache Maven

**Для Ubuntu/Debian:**

```bash
sudo apt install maven -y
```

**Для CentOS/RHEL:**

```bash
sudo yum install maven -y
```

**Для Fedora:**

```bash
sudo dnf install maven -y
```

**Альтернативная ручная установка (для всех дистрибутивов):**

```bash
# Скачайте последнюю версию Maven
cd /tmp
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz

# Распакуйте
sudo tar -xzf apache-maven-3.9.6-bin.tar.gz -C /opt/

# Создайте символическую ссылку
sudo ln -s /opt/apache-maven-3.9.6 /opt/maven

# Добавьте в ~/.bashrc
export M2_HOME=/opt/maven
export M2=$M2_HOME/bin
export PATH=$M2:$PATH

# Примените изменения
source ~/.bashrc
```

**Проверка установки:**

```bash
mvn -version
```

#### 1.3. Установка Node.js и npm

**Рекомендуемый способ через NVM (Node Version Manager):**

```bash
# Установите NVM
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Активируйте NVM
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

# Установите Node.js LTS версии
nvm install --lts

# Проверьте версии
node -v
npm -v
```

**Альтернативный способ для Ubuntu/Debian:**

```bash
# Добавьте репозиторий NodeSource
curl -fsSL https://deb.nodesource.com/setup_lts.x | sudo -E bash -

# Установите Node.js
sudo apt install -y nodejs

# Проверьте версии
node -v
npm -v
```

**Альтернативный способ для Fedora:**

```bash
sudo dnf install nodejs npm -y
```

### Шаг 2: Подготовка проекта

#### 2.1. Клонирование репозитория

```bash
cd ~
git clone <URL_репозитория>
cd VIP-Bonus-Arena
```

Или распакуйте ZIP-архив:

```bash
unzip vip-bonus-arena.zip
cd VIP-Bonus-Arena
```

#### 2.2. Проверка структуры проекта

```bash
ls -la
# Должны быть видны директории backend/ и frontend/

ls backend/
# Должен быть виден pom.xml

ls frontend/
# Должны быть видны package.json и src/
```

### Шаг 3: Запуск бэкенда (Spring Boot)

1. Откройте терминал
2. Перейдите в директорию бэкенда:
   ```bash
   cd ~/VIP-Bonus-Arena/backend
   ```

3. Очистите и соберите проект (первый запуск):
   ```bash
   mvn clean install -DskipTests
   ```
   
   ⏱️ Первая сборка может занять 5-10 минут

4. Запустите приложение:
   ```bash
   mvn spring-boot:run
   ```

   **Альтернативный способ** (если есть Maven wrapper):
   ```bash
   ./mvnw spring-boot:run
   ```

5. Дождитесь сообщения об успешном запуске:
   ```
   Started BonusArenaApplication in X.XXX seconds
   Tomcat started on port(s): 8080 (http)
   ```

6. **Важно:** Не закрывайте этот терминал — бэкенд должен работать постоянно
   
7. **Совет:** Для работы в фоне используйте `nohup` или `tmux`:
   ```bash
   # С использованием nohup
   nohup mvn spring-boot:run > backend.log 2>&1 &
   
   # С использованием tmux (рекомендуется)
   tmux new -s backend
   mvn spring-boot:run
   # Нажмите Ctrl+B, затем D для отключения от сессии
   ```

8. Проверьте доступность API:
   ```bash
   curl http://localhost:8080/api/users
   ```

### Шаг 4: Запуск фронтенда (Vue.js)

1. Откройте **новый** терминал (не закрывая бэкенд)
2. Перейдите в директорию фронтенда:
   ```bash
   cd ~/VIP-Bonus-Arena/frontend
   ```

3. Установите зависимости (только при первом запуске):
   ```bash
   npm install
   ```
   
   ⏱️ Процесс может занять 2-5 минут

4. Запустите сервер разработки:
   ```bash
   npm run dev
   ```

5. Дождитесь сообщения:
   ```
   VITE ready in XXX ms
   ➜  Local:   http://localhost:3000/
   ➜  Network: use --host to expose
   ```

6. Откройте браузер и перейдите по адресу: http://localhost:3000

**Запуск в фоновом режиме:**

```bash
# С использованием nohup
nohup npm run dev > frontend.log 2>&1 &

# Или с использованием tmux
tmux new -s frontend
npm run dev
# Ctrl+B, затем D
```

### Шаг 5: Проверка работоспособности

1. **Проверка бэкенда:**
   ```bash
   curl http://localhost:8080/api/configurations
   ```
   Должен вернуться JSON со списком конфигураций

2. **Проверка фронтенда:**
   - Откройте http://localhost:3000 в браузере
   - Выберите пользователя (player1, player2, admin)
   - Проверьте баланс

3. **Тестовый сценарий:**
   - Войдите как `admin`
   - Создайте комнату
   - В другой вкладке войдите как `player1`
   - Присоединитесь к комнате
   - Запустите раунд как администратор

### Решение распространённых проблем

#### Проблема 1: Команда `java` не найдена

**Решение:**
```bash
# Проверьте установку
which java

# Если не найдена, установите JDK
sudo apt install openjdk-17-jdk -y  # Ubuntu/Debian
sudo yum install java-17-openjdk-devel -y  # CentOS/RHEL

# Проверьте JAVA_HOME
echo $JAVA_HOME

# При необходимости настройте
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

#### Проблема 2: Ошибка Maven «Permission denied»

**Решение:**
```bash
# Проверьте права на директорию Maven
ls -la /opt/maven/bin/

# Исправьте права при необходимости
sudo chmod +x /opt/maven/bin/mvn
```

#### Проблема 3: npm install завершается ошибкой EACCES

**Решение:**
```bash
# Очистите кэш npm
npm cache clean --force

# Удалите node_modules и package-lock.json
rm -rf node_modules package-lock.json

# Попробуйте снова
npm install

# Если проблема с правами, измените владельца глобальной директории npm
mkdir ~/.npm-global
npm config set prefix '~/.npm-global'
echo 'export PATH=~/.npm-global/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

#### Проблема 4: Порт 8080 или 3000 уже занят

**Решение:**
```bash
# Найдите процесс, использующий порт
sudo lsof -i :8080
# или
sudo netstat -tulpn | grep :8080

# Завершите процесс
sudo kill -9 <PID>

# Или измените порт в конфигурации
# Бэкенд: application.properties
echo "server.port=8081" >> src/main/resources/application.properties

# Фронтенд: vite.config.js или .env
echo "VITE_PORT=3001" >> .env
```

#### Проблема 5: Ошибка CORS в браузере

**Решение:**
- Убедитесь, что бэкенд запущен
- Проверьте конфигуратор CORS в `WebConfig.java`
- Попробуйте режим инкогнито в браузере

#### Проблема 6: Недостаточно памяти для сборки

**Решение:**
```bash
# Увеличьте память для Maven
export MAVEN_OPTS="-Xmx2048m"
mvn clean install -DskipTests

# Или уменьшите параллелизм
mvn clean install -DskipTests -T 1
```

### Дополнительные команды для Linux

#### Сборка production-версии фронтенда

```bash
cd ~/VIP-Bonus-Arena/frontend
npm run build
```

Файлы появятся в `dist/`

#### Создание JAR-файла бэкенда

```bash
cd ~/VIP-Bonus-Arena/backend
mvn clean package -DskipTests
```

JAR-файл: `target/bonus-arena-0.0.1-SNAPSHOT.jar`

#### Запуск бэкенда из JAR

```bash
java -jar target/bonus-arena-0.0.1-SNAPSHOT.jar
```

#### Создание systemd сервисов (для продакшена)

**Сервис для бэкенда** `/etc/systemd/system/bonus-arena-backend.service`:

```ini
[Unit]
Description=VIP Bonus Arena Backend
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/vip-bonus-arena/backend
ExecStart=/usr/bin/java -jar bonus-arena-0.0.1-SNAPSHOT.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

**Сервис для фронтенда** `/etc/systemd/system/bonus-arena-frontend.service`:

```ini
[Unit]
Description=VIP Bonus Arena Frontend
After=network.target bonus-arena-backend.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/vip-bonus-arena/frontend
ExecStart=/usr/bin/npm run dev
Environment=NODE_ENV=production
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

**Активация сервисов:**

```bash
sudo systemctl daemon-reload
sudo systemctl enable bonus-arena-backend
sudo systemctl enable bonus-arena-frontend
sudo systemctl start bonus-arena-backend
sudo systemctl start bonus-arena-frontend

# Проверка статуса
sudo systemctl status bonus-arena-backend
sudo systemctl status bonus-arena-frontend
```

#### Скрипт автоматического запуска

Создайте файл `start-all.sh` в корне проекта:

```bash
#!/bin/bash

echo "🚀 Запуск VIP Бонус-Арена..."

# Запуск бэкенда в отдельной сессии tmux
tmux new -d -s backend "cd $(pwd)/backend && mvn spring-boot:run"
echo "✅ Бэкенд запущен в сессии tmux 'backend'"

# Ожидание запуска бэкенда
sleep 10

# Запуск фронтенда в отдельной сессии tmux
tmux new -d -s frontend "cd $(pwd)/frontend && npm run dev"
echo "✅ Фронтенд запущен в сессии tmux 'frontend'"

echo ""
echo "📍 Бэкенд: http://localhost:8080"
echo "📍 Фронтенд: http://localhost:3000"
echo ""
echo "Для подключения к сессиям:"
echo "  tmux attach -t backend"
echo "  tmux attach -t frontend"
echo ""
echo "Для остановки:"
echo "  tmux kill-session -t backend"
echo "  tmux kill-session -t frontend"
```

**Сделайте скрипт исполняемым и запустите:**

```bash
chmod +x start-all.sh
./start-all.sh
```

#### Скрипт остановки

Создайте файл `stop-all.sh`:

```bash
#!/bin/bash

echo "🛑 Остановка VIP Бонус-Арена..."

# Завершение процессов по портам
sudo fuser -k 8080/tcp 2>/dev/null && echo "✅ Бэкенд остановлен"
sudo fuser -k 3000/tcp 2>/dev/null && echo "✅ Фронтенд остановлен"

# Или завершение сессий tmux
tmux kill-session -t backend 2>/dev/null
tmux kill-session -t frontend 2>/dev/null

echo "✅ Все сервисы остановлены"
```

---

## 🐳 Запуск через Docker (универсальный способ)

Для всех операционных систем (Windows, Linux, macOS) доступен запуск через Docker.

### Требования

- Docker Desktop (Windows/macOS) или Docker Engine (Linux)
- Docker Compose

### Быстрый старт

```bash
# Перейдите в корень проекта
cd /workspace

# Запустите все сервисы
docker-compose up -d

# Проверьте статус
docker-compose ps

# Просмотр логов
docker-compose logs -f
```

### Остановка

```bash
docker-compose down
```

### Структура docker-compose.yml

Файл `docker-compose.yml` должен содержать:
- Сервис базы данных (PostgreSQL/H2)
- Сервис бэкенда (Spring Boot)
- Сервис фронтенда (Vue.js + Nginx)

---

## Демонстрационные пользователи

При первом запуске создаются тестовые пользователи:

| Username | Password | Роль | Баланс |
|----------|----------|------|--------|
| player1  | -        | Игрок | 10000 баллов |
| player2  | -        | Игрок | 10000 баллов |
| admin    | -        | Админ | 50000 баллов |

> **Примечание:** В MVP версии аутентификация упрощена — просто выберите пользователя из выпадающего списка в шапке приложения.

## Основные сценарии использования

### 1. Просмотр и создание комнат

1. Откройте страницу «Игровые комнаты»
2. Нажмите «Создать комнату» или «Создать первую комнату»
3. Выберите конфигурацию из списка

### 2. Присоединение к игре

1. Выберите комнату со статусом «Ожидание»
2. Нажмите «Войти»
3. При желании купите буст (кнопка «⚡ Купить буст»)
4. Оживайте запуска раунда

### 3. Запуск раунда (только администратор)

1. Будучи администратором, нажмите «▶️ Запустить» в комнате
2. Система автоматически заполнит свободные места ботами
3. Начнётся анимация «Космический слалом»

### 4. Завершение раунда

1. Нажмите «🏁 Финиш» когда раунд запущен
2. Система определит победителя на основе весов участников
3. Победитель получит призовой фонд (если это не бот)

### 5. Конфигуратор комнат (администратор)

1. Перейдите на вкладку «Конфигуратор»
2. Просмотрите существующие конфигурации
3. Создайте новую конфигурацию с желаемыми параметрами
4. Система покажет предупреждения о проблемных настройках

## API Endpoints

### Пользователи
- `GET /api/users` - Список всех пользователей
- `POST /api/users` - Создание нового пользователя
- `GET /api/users/{id}` - Получение пользователя по ID

### Конфигурации
- `GET /api/configurations` - Список активных конфигураций
- `POST /api/configurations` - Создание новой конфигурации
- `PUT /api/configurations/{id}` - Обновление конфигурации
- `DELETE /api/configurations/{id}` - Деактивация конфигурации

### Комнаты
- `GET /api/rooms` - Список всех комнат
- `GET /api/rooms/{id}` - Получение комнаты по ID
- `POST /api/rooms` - Создание новой комнаты
- `POST /api/rooms/{roomId}/join` - Присоединение к комнате
- `POST /api/rooms/{roomId}/boost` - Покупка буста
- `POST /api/rooms/{roomId}/start` - Запуск раунда
- `POST /api/rooms/{roomId}/finish` - Завершение раунда

## Игровая механика

### Определение победителя

Победитель определяется случайным образом с вероятностью, пропорциональной весу участника:

- **Базовый вес:** 1
- **Вес с бустом:** 1 × коэффициент_буста (по умолчанию 2)

**Пример:**
- Игрок А с бустом: вес = 2 (шанс 50%)
- Игрок Б без буста: вес = 1 (шанс 25%)
- Бот: вес = 1 (шанс 25%)

### Экономика

- **Призовой фонд:** процент от суммы взносов реальных игроков
- **Комиссия системы:** разница между всеми взносами и призовым фондом
- **Победа бота:** призовой фонд остаётся у системы

## Технологический стек

### Бэкенд
- Java 17
- Spring Boot 3.2
- Spring Data JPA
- H2 Database (для демонстрации)
- WebSocket (STOMP)

### Фронтенд
- Vue 3
- Pinia (state management)
- Axios (HTTP client)
- Vite (build tool)

## Архитектурные решения

### Хранение состояния комнат

В MVP версии состояние активных комнат хранится в базе данных H2. Для продакшена рекомендуется:
- Использовать PostgreSQL
- Добавить Redis для кэширования и pub/sub событий

### Масштабирование WebSocket

Для горизонтального масштабирования WebSocket подключений:
- Используйте sticky sessions на load balancer
- Настройте Redis broker для синхронизации сообщений между инстансами

### Изоляция раундов

Все финансовые операции (списания/начисления) выполняются в рамках транзакций базы данных с уровнем изоляции READ COMMITTED.

## Документация для администратора

### Параметры конфигурации комнат

| Параметр | Описание | Рекомендуемый диапазон |
|----------|----------|------------------------|
| Макс. игроков | Количество мест в комнате | 2-10 |
| Стоимость входа | Цена участия в баллах | 50-500 |
| Призовой фонд | Процент возврата игрокам | 80-95% |
| Множитель буста | Увеличение шанса победы | 1.5-3.0 |
| Стоимость буста | Цена усиления | 25-50% от входа |
| Таймер ожидания | Время до старта | 30-90 сек |

### Предупреждения системы

Система автоматически предупреждает о:
- Низком RTP (< 80%) — комната непривлекательна
- Отрицательной марже — комната убыточна
- Дорогом бусте (> 80% от стоимости входа)
- Нестандартном множителе буста

## Лицензия

Проект создан для демонстрационных целей.
