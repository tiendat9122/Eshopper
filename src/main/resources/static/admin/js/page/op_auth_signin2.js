const form = document.getElementById("login");
const errorMsg = document.getElementById("error");
const loginButton = document.getElementById("login-button");

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
        location.href = "/admin/role";
      });
    } else {
      error.style.display = "unset";
    }
  });
});
