
FROM node:10.14.2 as react-build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install
RUN npm install react-scripts@3.4.1 -g --silent

COPY . ./

CMD ["npm", "start"]