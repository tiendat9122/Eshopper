const formRegister = document.getElementById("user-register");
const errorRegisterMsg = document.getElementById("user-registererror");
const registerButton = document.getElementById("user-registerbutton");

registerButton.addEventListener("click", () => {
    const formData = new FormData(formRegister);
    const data = Object.fromEntries(formData.entries());
    console.log("done bind register");
    fetch("/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    }).then((res) => {
        if (res.ok) {
            res.json().then((success) => {
                location.href = "/userauth/login";
            })
        }
    })
})
