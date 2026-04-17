# Stage 1: Build the Angular app
FROM node:14 AS build

WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm install

COPY . .
ENV NODE_OPTIONS=--max_old_space_size=1024
RUN npx ng build --configuration production

# Stage 2: Serve with Nginx
FROM nginx:stable-alpine

COPY --from=build /app/dist/visualm /usr/share/nginx/html
# Nginx config is mounted via docker-compose
