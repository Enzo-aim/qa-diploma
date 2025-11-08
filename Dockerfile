FROM node:18-alpine
WORKDIR /app
COPY ./gate-simulator .
EXPOSE 9999
ENTRYPOINT ["npm", "start"]



