<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Divelog</title>
    <style>
        body {
            overflow-x: hidden;
            overflow-y: hidden;
            margin: 0;
            padding: 0;
        }
        .redirect-twitter-container {
            width: 100vw;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #00A4EF;
            color: white;
            font-size: 1.75vw;
        }
    </style>
</head>
<body>
    <div class="redirect-twitter-container">
        Loading...
    </div>
<script>
    window.onload = function() {
        <%--var receiver = document.getElementById('receiver').contentWindow;--%>
        <%--console.log("RECEIVER");--%>
        <%--console.log(receiver);--%>
        <%--function sendMessage(e) {--%>
        <%--    e.preventDefault();--%>
        <%--    receiver.postMessage("${jwtToken}", 'http://localhost:3000');--%>
        <%--}--%>
        <%--sendMessage();--%>
        <%--console.log("window.location.href");--%>
        window.location.href="http://localhost:3000/twitter/${jwtToken}";
    }

    <%--localStorage.setItem("twitterJwtToken", "${jwtToken}");--%>
    // window.location.href="http://localhost:3000/twitter";
</script>
</body>
</html>