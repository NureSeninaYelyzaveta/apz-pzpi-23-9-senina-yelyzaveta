document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const role = document.getElementById("role").value;

    if (!role) {
        alert("Оберіть роль");
        return;
    }

    let url = "";

    // ================= ADMIN (ФРОНТ-ЛОГИКА) =================
    if (role === "admin") {
        const adminEmail = "admin@artprogress.com";
        const adminName = "Admin";

        if (email !== adminEmail) {
            alert("Невірні дані адміністратора");
            return;
        }

        localStorage.clear();

        localStorage.setItem("role", "admin");
        localStorage.setItem("adminName", adminName);
        localStorage.setItem("adminEmail", adminEmail);

        window.location.href = "admin.html";
        return; // важно остановить выполнение
    }

    // ================= ОСТАЛЬНЫЕ РОЛИ =================
    switch (role) {
        case "student":
            url = `https://localhost:7033/api/students/byEmail?email=${encodeURIComponent(email)}`;
            break;
        case "teacher":
            url = `https://localhost:7033/api/teachers/byEmail?email=${encodeURIComponent(email)}`;
            break;
        case "parent":
            url = `https://localhost:7033/api/parents/byEmail?email=${encodeURIComponent(email)}`;
            break;

        case "admin":
            
            break;
    }

    try {
        const response = await fetch(url);

        if (response.ok) {
            const data = await response.json();

            const user = Array.isArray(data)
                ? data.find(u => u.contactInfo?.toLowerCase() === email.toLowerCase())
                : data;

            if (!user) {
                alert("Користувач з таким email не знайдений");
                return;
            }

            localStorage.clear();

            if (role === "student") {
                const id = user.studentID ?? user.id;
                localStorage.setItem("studentId", id);
                localStorage.setItem("studentName", user.name);
                localStorage.setItem("studentEmail", user.contactInfo);
            }
            else if (role === "teacher") {
                const id = user.teacherID ?? user.id;

                localStorage.removeItem("teacherId");
                localStorage.removeItem("teacherID");

                localStorage.setItem("teacherId", id);
                localStorage.setItem("teacherName", user.name);
                localStorage.setItem("teacherEmail", user.contactInfo);
            }
            else if (role === "parent") {
                const id = user.parentID ?? user.id;
                localStorage.setItem("parentID", id);
                localStorage.setItem("parentName", user.name);
                localStorage.setItem("parentEmail", user.contactInfo);
            }

            localStorage.setItem("role", role);

            switch (role) {
                case "student": window.location.href = "student.html"; break;
                case "teacher": window.location.href = "teacher.html"; break;
                case "parent": window.location.href = "parent.html"; break;
                case "admin":
                   
                    if (email !== "admin@artprogress.com") {
                        alert("Невірні дані адміністратора");
                        return;
                    }

                    localStorage.setItem("adminId", "1");
                    localStorage.setItem("adminName", "Адміністратор");
                    localStorage.setItem("adminEmail", email);

                    localStorage.setItem("role", "admin");

                    window.location.href = "admin.html";
                    return;
            }

        } else {
            alert("Помилка при перевірці користувача");
        }

    } catch (err) {
        console.error(err);
        alert("Помилка з'єднання з сервером");
    }
});


// ================= ФУНКЦИИ УЧИТЕЛЯ (НЕ ТРОГАЛА) =================
function addGrade() {
    const studentId = document.getElementById('student-id').value;
    const disciplineId = document.getElementById('discipline-id').value;
    const gradeValue = document.getElementById('grade-value').value;
    alert(`Оцінка ${gradeValue} для студента ${studentId} додана!`);
}


// ================= ПЕРЕКЛЮЧЕНИЕ ФОРМ =================
document.getElementById("showRegister").onclick = function () {
    document.getElementById("loginForm").style.display = "none";
    document.getElementById("registerForm").style.display = "block";
};


// ================= РЕГИСТРАЦИЯ =================
document.getElementById("registerForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const name = document.getElementById("regName").value;
    const contact = document.getElementById("regContact").value;
    const role = document.getElementById("regRole").value;

    let url = "";
    let data = {};

    if (role === "student") {
        url = "https://localhost:7033/api/students";
        data = { name: name, group: "A1", contactInfo: contact };
    }
    else if (role === "teacher") {
        url = "https://localhost:7033/api/teachers";
        data = { name: name, position: "Teacher", contactInfo: contact };
    }
    else if (role === "parent") {
        url = "https://localhost:7033/api/parents";
        data = { name: name, contactInfo: contact };
    }

    else if (role === "admin") {
        localStorage.clear();
        localStorage.setItem("role", "admin");
        localStorage.setItem("adminName", "Admin");

        window.location.href = "admin.html";
        return;
    }

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert("Успішна реєстрація!");

            document.getElementById("registerForm").style.display = "none";
            document.getElementById("loginForm").style.display = "block";
        } else {
            alert("Помилка при реєстрації");
        }
    } catch (err) {
        console.error(err);
        alert("Помилка з'єднання з сервером");
    }
});