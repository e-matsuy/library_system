<%--
  Created by IntelliJ IDEA.
  User: matsuda.yu
  Date: 2020/07/16
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>書籍一覧表示・検索</title>
    <script src="js/jquery-3.5.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="css/common.css">
    <link rel="stylesheet" type="text/css" href="css/list.css">
</head>
<body>
<header><p>図書管理システム</p></header>
<div id="wrapper">
    <div><span>書籍一覧</span><button id="open_search">蔵書検索</button></div>
    <table>
        <tr>
            <th>No.</th>
            <th>タイトル</th>
            <th>出版社</th>
            <th>貸出状況</th>
            <th>ジャンル</th>
        </tr>
    </table>
</div>
<dialog id="search_form">
    <form>
        <label></label>
        <input>
    </form>
</dialog>
</body>
<script src="js/common.js"></script>
<script src="js/list.js"></script>
</html>
