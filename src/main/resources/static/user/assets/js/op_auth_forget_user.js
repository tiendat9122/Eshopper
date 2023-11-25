const formForget = document.getElementById("user-forget");
const errorForgetMsg = document.getElementById("user-forgetererror");
const forgetButton = document.getElementById("user-forgetbutton");

forgetButton.addEventListener("click", () => {
    const formData = new FormData(formForget);
    const data = Object.fromEntries(formData.entries());
    console.log("done bind forget");
    fetch("/auth/forget", {
        method: "PUT",
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
