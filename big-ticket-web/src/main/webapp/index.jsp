<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%!
    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    private String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            +"|windows (phone|ce)|blackberry"
            +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            +"|laystation portable)|nokia|fennec|htc[-_]"
            +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    private String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    private Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    private Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    private boolean checkMobile(String userAgent){
        if(null == userAgent){
            userAgent = "";
        }
    // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        return matcherPhone.find() || matcherTable.find();
    }
%>
<%
    String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
    if(null == userAgent){
        userAgent = "";
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <meta name="keywords" content="hupu">
    <meta name="description" content="虎扑">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link href="img/easy.ico" rel="shortcut icon">
    <link href="img/easy.ico" rel="apple-touch-icon">
    <title>hupuEasy|更方便快捷的发掘热点</title>
</head>
<body ontouchstart>
<div id="container"></div>
<% if(checkMobile(userAgent)){ %>
<link href="dist/appmobile.css" rel="stylesheet">
<script type="text/javascript" src="dist/appmobile.js"></script>
<% } else { %>
<link href="dist/app.css" rel="stylesheet">
<script type="text/javascript" src="dist/vendor.js"></script>
<script type="text/javascript" src="dist/app.js"></script>
<% } %>
</body>
</html>