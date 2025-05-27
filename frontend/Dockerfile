# Этап сборки (builder)
FROM node:18-alpine AS builder

WORKDIR /app

# Копируем package.json и lock-файлы, чтобы установить зависимости
COPY package.json package-lock.json ./

# Устанавливаем pnpm и зависимости проекта
RUN npm install -g pnpm
RUN pnpm install

# Копируем исходники и собираем фронтенд
COPY . .
RUN pnpm run build

# Этап запуска (production)
FROM node:18-alpine

WORKDIR /app

# Устанавливаем serve для отдачи статики
RUN npm install -g serve

# Копируем собранную папку build из стадии сборки
COPY --from=builder /app/build ./build

EXPOSE 4200

# Запускаем serve на 4200 порту
CMD ["serve", "-s", "build", "-l", "4200"]
