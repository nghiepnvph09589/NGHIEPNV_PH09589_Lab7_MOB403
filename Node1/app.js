var express = require("express");
var app =  express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
var hostname="192.168.5.116";
var port=3000;
//server.listen(process.env.PORT || 3000);
server.listen(port,hostname);
var listUser = [];
io.sockets.on('connection',function(socket){
    console.log("co nguoi ket noi");
    socket.on('user_login',function(user_name){
        if(listUser.indexOf(user_name) > -1)
        {
            return;
        }
        listUser.push(user_name);
        socket.user = user_name;
        console.log(`xin chao ${user_name} `);
    });
    socket.on('send_message',function(message){
        io.sockets.emit('receiver_message',{data: socket.user+": "+message});
        console.log(`${socket.user} send:  ${message}` );
    });
});
console.log(`Server running at http://${hostname}:${port}`);