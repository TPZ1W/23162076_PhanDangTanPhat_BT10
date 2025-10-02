<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${pageTitle}</title>
            <link rel="stylesheet" href="/css/style.css">
            <style>
                .error-container {
                    max-width: 600px;
                    margin: 5rem auto;
                    padding: 2rem;
                    background: white;
                    border-radius: 8px;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
                    text-align: center;
                }

                .error-icon {
                    font-size: 4rem;
                    color: #e53e3e;
                    margin-bottom: 1rem;
                }

                .error-code {
                    font-size: 2rem;
                    font-weight: bold;
                    color: #2d3748;
                    margin-bottom: 1rem;
                }

                .error-message {
                    font-size: 1.2rem;
                    color: #718096;
                    margin-bottom: 2rem;
                }

                .error-actions {
                    display: flex;
                    gap: 1rem;
                    justify-content: center;
                }
            </style>
        </head>

        <body>
            <div class="error-container">
                <div class="error-icon">⚠️</div>
                <div class="error-code">Lỗi ${errorCode}</div>
                <div class="error-message">
                    <c:out value="${errorMessage}" />
                </div>
                <div class="error-actions">
                    <button class="btn btn-primary" onclick="history.back()">Quay lại</button>
                    <a href="/web/dashboard" class="btn btn-secondary">Về trang chủ</a>
                </div>
            </div>
        </body>

        </html>