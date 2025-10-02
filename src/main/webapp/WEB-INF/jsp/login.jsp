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
                .login-container {
                    max-width: 400px;
                    margin: 5rem auto;
                    padding: 2rem;
                    background: white;
                    border-radius: 8px;
                    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
                }

                .login-header {
                    text-align: center;
                    margin-bottom: 2rem;
                }

                .login-header h2 {
                    color: #2d3748;
                    margin-bottom: 0.5rem;
                }

                .login-header p {
                    color: #718096;
                }

                .form-group {
                    margin-bottom: 1.5rem;
                }

                .form-actions {
                    text-align: center;
                }

                .error-alert {
                    background-color: #fed7d7;
                    border: 1px solid #feb2b2;
                    color: #c53030;
                    padding: 1rem;
                    border-radius: 6px;
                    margin-bottom: 1rem;
                }

                .register-link {
                    margin-top: 1rem;
                    text-align: center;
                }

                .register-link a {
                    color: #3182ce;
                    text-decoration: none;
                }

                .register-link a:hover {
                    text-decoration: underline;
                }
            </style>
        </head>

        <body>
            <div class="login-container">
                <div class="login-header">
                    <h2>Đăng nhập</h2>
                    <p>Vui lòng đăng nhập để tiếp tục</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="error-alert">
                        <c:out value="${error}" />
                    </div>
                </c:if>

                <div id="messageArea"></div>

                <form id="loginForm">
                    <div class="form-group">
                        <label class="form-label" for="email">Email *</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="password">Mật khẩu *</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary" id="loginBtn">Đăng nhập</button>
                    </div>
                </form>

                <div class="register-link">
                    <p>Chưa có tài khoản? <a href="/web/register">Đăng ký ngay</a></p>
                </div>
            </div>

            <script>
                document.getElementById('loginForm').addEventListener('submit', async function (e) {
                    e.preventDefault();

                    const formData = new FormData(e.target);
                    const loginData = {
                        email: formData.get('email'),
                        password: formData.get('password')
                    };

                    const loginBtn = document.getElementById('loginBtn');
                    loginBtn.disabled = true;
                    loginBtn.textContent = 'Đang đăng nhập...';

                    try {
                        const response = await fetch('/api/auth/login', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(loginData)
                        });

                        const result = await response.json();

                        if (result.success) {
                            showMessage('Đăng nhập thành công!', 'success');

                            // Redirect based on provided URL from server
                            setTimeout(() => {
                                const redirectUrl = result.redirectUrl || 
                                    (result.user.isAdmin ? '/web/admin' : '/web/dashboard');
                                window.location.href = redirectUrl;
                            }, 1000);
                        } else {
                            showMessage(result.message || 'Đăng nhập thất bại', 'error');
                        }
                    } catch (error) {
                        console.error('Login error:', error);
                        showMessage('Lỗi kết nối. Vui lòng thử lại.', 'error');
                    } finally {
                        loginBtn.disabled = false;
                        loginBtn.textContent = 'Đăng nhập';
                    }
                });

                function showMessage(message, type = 'info') {
                    const messageArea = document.getElementById('messageArea');
                    messageArea.innerHTML = `<div class="message ${type}">${message}</div>`;

                    if (type === 'success') {
                        setTimeout(() => {
                            messageArea.innerHTML = '';
                        }, 3000);
                    }
                }
            </script>
        </body>

        </html>