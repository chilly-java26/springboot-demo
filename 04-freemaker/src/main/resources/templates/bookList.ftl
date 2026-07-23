<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书管理系统</title>
    <!-- 引入 Bootstrap 美化界面 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="container mt-4">
    <h2>📚 图书管理</h2>
    
    <!-- 新增图书表单 -->
    <div class="card mt-3">
        <div class="card-body">
            <h5>新增图书</h5>
            <div class="row g-3">
                <div class="col-md-4">
                    <input type="text" id="bookName" class="form-control" placeholder="书名">
                </div>
                <div class="col-md-3">
                    <input type="text" id="bookAuthor" class="form-control" placeholder="作者">
                </div>
                <div class="col-md-2">
                    <input type="number" id="bookPrice" class="form-control" placeholder="价格">
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary" onclick="addBook()">添加</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 图书列表表格 -->
    <table class="table table-striped table-hover mt-3">
        <thead>
            <tr>
                <th>ID</th>
                <th>书名</th>
                <th>作者</th>
                <th>价格</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody id="bookTableBody">
            <!-- JS 动态渲染 -->
        </tbody>
    </table>
</div>

<script>
    // 获取 JWT Token（从 localStorage 读取，如果没有则提示）
    const token = localStorage.getItem('token') || 'Bearer xxx'; 
    // 如果项目三启动了 JWT，这里需要用真实的 Bearer token
    // 为了方便演示，先沿用项目三的 token，或手动在控制台执行 localStorage.setItem('token', 'Bearer xxx')

    // 1. 加载图书列表
    function loadBooks() {
        $.ajax({
            url: '/api/books',
            type: 'GET',
            headers: {
                'Authorization': token.startsWith('Bearer ') ? token : 'Bearer ' + token
            },
            success: function(res) {
                if (res.code === 200) {
                    const list = res.data;
                    let html = '';
                    list.forEach(book => {
                        html += '<tr>' +
                            '<td>' + book.id + '</td>' +
                            '<td>' + book.name + '</td>' +
                            '<td>' + book.author + '</td>' +
                            '<td>' + book.price + '</td>' +
                            '<td>' +
                                '<button class="btn btn-danger btn-sm" onclick="deleteBook(' + book.id + ')">删除</button>' +
                            '</td>' +
                        '</tr>';
                    });
                    $('#bookTableBody').html(html);
                } else {
                    alert('加载失败：' + res.msg);
                }
            },
            error: function(xhr) {
                alert('请求失败：' + xhr.status);
            }
        });
    }

    // 2. 新增图书
    function addBook() {
        const name = $('#bookName').val();
        const author = $('#bookAuthor').val();
        const price = $('#bookPrice').val();
        if (!name) { alert('请输入书名'); return; }

        $.ajax({
            url: '/api/books',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': token.startsWith('Bearer ') ? token : 'Bearer ' + token
            },
            data: JSON.stringify({ name, author, price }),
            success: function(res) {
                if (res.code === 200) {
                    alert('添加成功！');
                    $('#bookName').val('');
                    $('#bookAuthor').val('');
                    $('#bookPrice').val('');
                    loadBooks(); // 刷新列表
                } else {
                    alert('添加失败：' + res.msg);
                }
            }
        });
    }

    // 3. 删除图书
    function deleteBook(id) {
        if (!confirm('确定删除 ID 为 ' + id + ' 的图书吗？')) return;
        $.ajax({
            url: '/api/books/' + id,
            type: 'DELETE',
            headers: {
                'Authorization': token.startsWith('Bearer ') ? token : 'Bearer ' + token
            },
            success: function(res) {
                if (res.code === 200) {
                    alert('删除成功！');
                    loadBooks();
                } else {
                    alert('删除失败：' + res.msg);
                }
            }
        });
    }

    // 页面加载完成后加载列表
    $(document).ready(function() {
        loadBooks();
    });
</script>
</body>
</html>