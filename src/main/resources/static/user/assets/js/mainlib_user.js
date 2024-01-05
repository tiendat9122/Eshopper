
//Biên dịch mã Token JWT được lưu trong localStorage
function parseJwt(token) {
  var base64Url = token.split(".")[1];
  var base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
  var jsonPayload = decodeURIComponent(
    window
      .atob(base64)
      .split("")
      .map(function (c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );

  return JSON.parse(jsonPayload);
}

//Xử lý sau khi đăng xuất. Không thể nhấn nút back trở lại
// if(localStorage.getItem('jwt') == null) {
//     location.href='/userauth/login'
// } else {
//   const data = parseJwt(localStorage.getItem('jwt'))
//   const date = new Date(0);
//   if(data.exp < Date.now() / 1000) {
//     dangXuatUser()
//   } else {
//     setTimeout(()=>{
//       dangXuatUser()
//     }, data.exp * 1000 - Date.now())
//   }
// }

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
  return JSON.parse(localStorage.getItem("user"));
}

//Lấy dữ liệu JWT trong Local Storage
function getJwtToken() {
  return JSON.parse(localStorage.getItem("jwt")).token;
}

//Xử lý đăng xuất. Xóa JWT được lưu trong Local Storage
function dangXuatUser() {
  localStorage.clear();
  location.href = "/userauth/login";
}

//Xử lý tài khoản chưa đăng nhập nhưng muốn sử dụng chức năng cần xác thực
function chuaDangNhap() {
  var result = confirm("Bạn chưa đăng nhập, thực hiện đăng nhập?");
  if (result) {
    dangXuatUser();
  }
}

// function disableSubmit(e) {
//   e.preventDefault()
//   return false;
// }

async function layThongTinNguoiDungHienTai() {
  const info = parseJwt(localStorage.getItem("jwt"));
  const res = await fetch("/eshopper/user?id=" + info.userId);
  if (res.ok) {
    const userInfo = await res.json();
    localStorage.setItem("user", JSON.stringify(userInfo));
  }
}

function capNhatThongTinNguoiDungHienTai() {
  const userCurrent = getUserInfo();

  if (userCurrent != null) {
    document.getElementById("user-fullname-nav").innerText =
      userCurrent.full_name;

    var imgAvatarNav = document.getElementById("user-avatar-nav");
    if (userCurrent.avatar != null) {
      imgAvatarNav.src = "/eshopper/download/" + userCurrent.avatar;
    } else {
      imgAvatarNav.src = "/content/admin/img_default/default_avatar.jpg";
    }
  } else {
    document.getElementById("user-fullname-nav").innerHTML = "";
    document.getElementById("user-avatar-nav").src = "";
  }
}

function themVaoGio(productId) {
  const user = getUserInfo();
  if (user != null) {
    fetch(
      "/user/orderdetail/get?productId=" + productId + "&userId=" + user.id,
      { ...genConfig() }
    )
      .then((res) => res.json())
      .then((data) => {
        var iconContainer = document.getElementById(
          "iconAddOrderDetail-" + productId
        );
        var icon = document.createElement("div");
        icon.classList.add("icon-add-orderdetail");
        icon.innerHTML = `<i class="fas fa-shopping-cart text-primary mr-1"></i>`;
        iconContainer.appendChild(icon);
      });
  } else {
    chuaDangNhap();
  }
}
