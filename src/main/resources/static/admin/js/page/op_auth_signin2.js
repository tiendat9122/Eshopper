const form = document.getElementById("login");
const errorMsg = document.getElementById("error");
const loginButton = document.getElementById("login-button");

function parseJwt (token) {
  var base64Url = token.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}

loginButton.addEventListener("click", () => {
  const formData = new FormData(form);
  const data = Object.fromEntries(formData.entries());
  console.log("bind xong");
  fetch("/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  }).then((res) => {
    if (res.ok) {
      res.json().then((data) => {
        localStorage.setItem("jwt", JSON.stringify(data));
        document.cookie =
          "AUTHORIZATION=Bearer " + data.token + ";path=/;expires=max-age";
        const info = parseJwt(data.token)
        fetch('/user/get?id=' + info.userId).then(res => {
          if (res.ok) {
            res.json().then(userInfo => {
              localStorage.setItem("user", JSON.stringify(userInfo))
              location.href = "/admin/role";
            })
          }
        })
      });
    } else {
      error.style.display = "unset";
    }
  });
});
