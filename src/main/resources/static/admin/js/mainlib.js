function parseJwt(token) {
  var base64Url = token.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}


//Xử lý sau khi đăng xuất. Không thể nhấn nút back trở lại
if (localStorage.getItem('jwt') == null) {
  location.href = '/admin/login'
} else {
  const data = parseJwt(localStorage.getItem('jwt'))
  const date = new Date(0);
  if (data.exp < Date.now() / 1000) {
    dangXuat()
  } else {
    setTimeout(() => {
      dangXuat()
    }, data.exp * 1000 - Date.now())
  }
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

function getUserInfo() {
  return JSON.parse(localStorage.getItem("user"))
}

//Lấy dữ liệu JWT trong Local Storage
function getJwtToken() {
  return JSON.parse(localStorage.getItem("jwt")).token;
}

//Xử lý đăng xuất. Xóa JWT được lưu trong Local Storage
function dangXuat() {
  localStorage.clear()
  location.href = '/admin/login'
}


function disableSubmit(e) {
  e.preventDefault()
  return false;
}

async function layThongTinNguoiDungHienTai() {
  const info = parseJwt(localStorage.getItem('jwt'))
  await fetch('/user/get?id=' + info.userId).then(res => {
    if (res.ok) {
      res.json().then(userInfo => {
        localStorage.setItem("user", JSON.stringify(userInfo))
        location.href = "/admin/user";
      })
    }
  })
}

function capNhatThongTinNguoiDungHienTai() {
  const user = getUserInfo()
  document.querySelectorAll('.need-fullname').forEach(e => {
    e.innerText = user.full_name
  })
  document.querySelectorAll('.need-rolename').forEach(e => {
    e.innerText = user.role.map(i => i.displayName).join(" / ")
  })
  document.querySelectorAll('.need-avatar').forEach(e => {
    if (!user.avatar.isBlank())
      e.src = "/user/download/" + user.avatar
    else
      e.src = "/content/admin/assets/media/avatars/avatar0.jpg"
  })
}

