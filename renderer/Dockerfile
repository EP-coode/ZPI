# ONLY FOR DEVELOPMENT 

FROM node:16-slim

WORKDIR /usr/src/app/

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 3000

CMD ["npm", "run", "dev"]