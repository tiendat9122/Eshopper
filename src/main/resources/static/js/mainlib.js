//Xử lý sau khi đăng xuất. Không thể nhấn nút back trở lại
if(localStorage.getItem('jwt') == null) {
    location.href='/admin/login'        
}

//Gửi JWT đi kèm theo HTTP mỗi lần gửi yêu cầu
function genConfig() {
  const jwt = JSON.parse(localStorage.getItem("jwt"));
  return {
    headers: {
      Authorization: "Bearer " + jwt.token,
      "Content-Type": "application/json",
    },
  };
}

//Lấy dữ liệu JWT trong Local Storage
function getJwtToken() {
    return JSON.parse(localStorage.getItem("jwt")).token;
}

//Xử lý đăng xuất. Xóa JWT được lưu trong Local Storage
function dangXuat() {
    localStorage.clear()
    location.href='/admin/login'                                       
}


