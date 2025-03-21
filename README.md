# Chat App - Backend

## Table of Contents

<ol>
  <li><a href="#about">About</a></li>
  <li><a href="#features">Features</a></li>
  <li><a href="#deployment">Deployment</a></li>
  <li><a href="#demo">Demo</a></li>
  
</ol>

## About

This is a relatively chat application created using Spring Boot in the backend. It leverages StompJS and SockJS (WebSocket) for real-time user communication and MySQL for data persistence. Users can create accounts via OAuth 2.0, form custom group chats, and exchange messages seamlessly. This application draws inspiration from earlier versions of Discord, aiming to deliver a robust and user-friendly chat experience.

## Features

- Spring Boot REST API
- WebSocket support with STOMP
- Real-time message broadcasting
- OAuth 2.0 integration
- Group chat management
- Real-time chat list updates
- Real-time notifications system
- Rate limiting
- Security measures:
  - CORS configuration
  - Rate limiting protection
  - Session management
  - WebSocket authentication

## Deployment

- AWS EC2 for hosting
- PostgreSQL on AWS RDS for database
- DuckDNS for domain management
- Nginx as a reverse proxy
- HTTPS/SSL support
- WebSocket secure connection

## Demo
![Chat App Demo](https://res.cloudinary.com/dls9pinnl/image/upload/t_chatappresize/v1742251907/chatapp_tsqv7p.png)

[Live Demo](https://chatapp-franklin.duckdns.org/)
