<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>图书管理系统</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="container mt-4">
    <h2>📚 图书管理</h2>
    
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
        </tbody>
    </table>
</div>

<script>
    // 获取上下文路径
    function getContextPath() {
        let path = window.location.pathname;
        if (path.endsWith('/index')) {
            path = path.substring(0, path.length - 6);
        } else if (path.endsWith('/')) {
            path = path.substring(0, path.length - 1);
        }
        return path; // 可能是 "" 或 "/springboot-demo-1.0"
    }
    const ctx = getContextPath();

    // 获取 token
    function getAuthHeader() {
        let token = localStorage.getItem('token');
        if (!token) {
            alert('请先设置 token！在控制台执行：localStorage.setItem("token", "Bearer <你的JWT>")');
            return '';
        }
        return token.startsWith('Bearer ') ? token : 'Bearer ' + token;
    }

    function loadBooks() {
        $.ajax({
            url: ctx + '/api/books',
            type: 'GET',
            headers: { 'Authorization': getAuthHeader() },
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
                            '<td><button class="btn btn-danger btn-sm" onclick="deleteBook(' + book.id + ')">删除</button></td>' +
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

    function addBook() {
        const name = $('#bookName').val();
        const author = $('#bookAuthor').val();
        const price = $('#bookPrice').val();
        if (!name) { alert('请输入书名'); return; }

        $.ajax({
            url: ctx + '/api/books',
            type: 'POST',
            contentType: 'application/json',
            headers: { 'Authorization': getAuthHeader() },
            data: JSON.stringify({ name, author, price }),
            success: function(res) {
                if (res.code === 200) {
                    alert('添加成功！');
                    $('#bookName').val('');
                    $('#bookAuthor').val('');
                    $('#bookPrice').val('');
                    loadBooks();
                } else {
                    alert('添加失败：' + res.msg);
                }
            }
        });
    }

    function deleteBook(id) {
        if (!confirm('确定删除 ID 为 ' + id + ' 的图书吗？')) return;
        $.ajax({
            url: ctx + '/api/books/' + id,
            type: 'DELETE',
            headers: { 'Authorization': getAuthHeader() },
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

    $(document).ready(function() {
        loadBooks();
    });
</script>
</body>
</html>