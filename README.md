# Waqjel-Java26-backend-helpdesk
Backend Lab2 - Helpdesk / Ticket Management System

## About Helpdesk
Web application where users creates and track support tickets. The system demonstrates authentication, authorization, database management using Spring Data JPA, and
server-rendered web pages using Thymeleaf.

## Docker Setup, Install SQL, check process and run docker container
```bash
docker run --name helpdesk-mysql -e MYSQL_ROOT_PASSWORD=rootpass -e MYSQL_DATABASE=helpdeskdb -p 3306:3306 -d mysql:8.4
docker ps
docker start helpdesk-mysql
```




