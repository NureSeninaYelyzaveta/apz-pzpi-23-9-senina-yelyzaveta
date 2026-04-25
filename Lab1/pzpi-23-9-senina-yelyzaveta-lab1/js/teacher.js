const API_URL = "https://localhost:7033/api";

// ID вчителя з localStorage
const teacherId = Number(localStorage.getItem("teacherId"));

if (!teacherId) {
    alert("Користувач не авторизований!");
    window.location.href = "login.html";
}


//         профіль
async function loadProfile() {
    try {
        const res = await fetch(`${API_URL}/teachers/${teacherId}`);
        const data = await res.json();

        document.getElementById("teacherName").textContent = data.name;
        document.getElementById("teacherPosition").textContent = `Посада: ${data.position}`;
        document.getElementById("teacherEmail").textContent = `Email: ${data.contactInfo}`;

    } catch (err) {
        console.error("Помилка профілю:", err);
    }
}

//               повідомлення
function loadNotificationsFromProfile(notifications) {
    const container = document.getElementById("notifications");
    if (!container) return;

    container.innerHTML = "";

    if (!notifications || notifications.length === 0) {
        container.innerHTML = "<p>Немає повідомлень</p>";
        return;
    }

    const latest = notifications.slice(-3).reverse();

    latest.forEach(n => {
        const div = document.createElement("div");
        div.className = "notification-item";
        div.textContent = `${n.text} (${new Date(n.date).toLocaleString()})`;
        container.appendChild(div);
    });
}

//   фото и опис
const desc = document.getElementById("teacherDescription");
if (desc) {
    desc.value = localStorage.getItem(`teacherDescription_${teacherId}`) || "";

    desc.addEventListener("input", () => {
        localStorage.setItem(`teacherDescription_${teacherId}`, desc.value);
    });
}

document.getElementById("uploadPhoto")?.addEventListener("change", function (e) {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function () {
        document.getElementById("profilePhoto").src = reader.result;
        localStorage.setItem(`teacherPhoto_${teacherId}`, reader.result);
    };
    reader.readAsDataURL(file);
});


//           учні
let students = [];

async function loadStudents() {
    try {
        const res = await fetch(`https://localhost:7033/api/students`);
        students = await res.json();
    } catch (err) {
        console.error("Помилка студентів:", err);
    }
}

//          автопошук
const searchInput = document.getElementById("studentSearch");
const suggestions = document.getElementById("studentSuggestions");
let selectedStudentId = null;

searchInput?.addEventListener("input", () => {
    const value = searchInput.value.toLowerCase();
    suggestions.innerHTML = "";

    if (!value) return;

    const filtered = students.filter(s => s.name.toLowerCase().includes(value));

    filtered.forEach(s => {
        const li = document.createElement("li");
        li.textContent = s.name;

        li.onclick = () => {
            searchInput.value = s.name;
            selectedStudentId = s.studentID;
            suggestions.innerHTML = "";
        };

        suggestions.appendChild(li);
    });
});

//         предмети
async function loadDisciplines() {
    try {
        const res = await fetch(`https://localhost:7033/api/disciplines`);
        const data = await res.json();

        const select = document.getElementById("disciplineSelect");
        data.forEach(d => {
            const option = document.createElement("option");
            option.value = d.disciplineID;
            option.textContent = d.name;
            select.appendChild(option);
        });
    } catch (err) {
        console.error("Помилка предметів:", err);
    }
}

//           додавання оцінки
document.getElementById("addGradeBtn")?.addEventListener("click", async () => {
    const disciplineID = Number(document.getElementById("disciplineSelect").value);
    const value = Number(document.getElementById("gradeValue").value);


    if (!selectedStudentId || !disciplineID || !value) {
        alert("Заповніть всі поля");
        return;
    } 

    const newGrade = {
        studentID: Number(selectedStudentId),
        teacherID: Number(teacherId),
        disciplineID: Number(disciplineID),
        value: Number(value),

    };

   


    try {
        const res = await fetch(`https://localhost:7033/api/grades`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newGrade)
        });

        if (res.ok) {
            alert("Оцінка додана");
            document.getElementById("gradeValue").value = "";
            document.getElementById("studentSearch").value = "";
            selectedStudentId = null;
            loadTeacherGrades();
        } else {
            alert("Помилка при додаванні оцінки");
        }

    } catch (err) {
        console.error(err);
    }
});

//              оцінки
async function loadTeacherGrades() {
    try {
        const res = await fetch(`https://localhost:7033/api/grades`);
        const data = await res.json();

        const table = document.getElementById("gradesTable");
        if (!table) return;

        table.innerHTML = "";

        const myGrades = data
            .filter(g =>
                g.teacherID === teacherId ||
                g.teacherId === teacherId ||
                g.teacher?.teacherID === teacherId
            )
            .slice(-5)
            .reverse();

        if (myGrades.length === 0) {
            table.innerHTML = `<tr><td colspan="4">Оцінок поки що немає</td></tr>`;
            return;
        }

        myGrades.forEach(g => {
            const row = `
        <tr>
            <td>${g.studentID}</td>
            <td>${g.disciplineID}</td>
            <td>${g.value}</td>
            <td>${g.date ? new Date(g.date).toLocaleDateString() : "-"}</td>
        </tr>
    `;
            table.innerHTML += row;
        });

    } catch (err) {
        console.error("Помилка оцінок:", err);
    }
}

window.addEventListener("DOMContentLoaded", () => {
    loadProfile();
    loadStudents();
    loadDisciplines();
    loadTeacherGrades();

    const savedPhoto = localStorage.getItem(`teacherPhoto_${teacherId}`);
    if (savedPhoto) {
        const img = document.getElementById("profilePhoto");
        if (img) img.src = savedPhoto;
    }
});