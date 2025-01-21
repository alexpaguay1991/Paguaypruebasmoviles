# Usa una imagen base de Node.js
FROM node:14 AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos de tu proyecto
COPY package*.json ./
RUN npm install
COPY . .

# Construye la aplicación
RUN npm run build

# Usa Nginx para servir la aplicación
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html

# Expone el puerto en el que Nginx escucha
EXPOSE 80

# Comando para iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]
