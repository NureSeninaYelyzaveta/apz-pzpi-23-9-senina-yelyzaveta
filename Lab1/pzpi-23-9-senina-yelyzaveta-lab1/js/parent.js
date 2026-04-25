const API_URL = "https://localhost:7033/api";

const parentId = Number(localStorage.getItem("parentID"));

console.log("parentId:", parentId);

if (!parentId) {
    alert("Користувач не авторизований!");
    window.location.href = "login.html";
}

// профіль
async function loadParentProfile() {
    try {
        const res = await fetch(`${API_URL}/parents/${parentId}`);
        if (!res.ok) throw new Error("Parent not found");

        const parent = await res.json();

        console.log("Parent data:", parent);

        document.getElementById("parentName").textContent =
            parent.name ?? "Без імені";

        document.getElementById("parentEmail").textContent =
            parent.contactInfo ?? "Без email";

    } catch (err) {
        console.error("Помилка профілю:", err);
    }
}

// оцінки дитини
async function loadChildData() {
    try {
        const relRes = await fetch(`${API_URL}/ParentStudents`);
        const relations = await relRes.json();

        console.log("Relations:", relations);

        const relation = relations.find(r =>
            Number(r.parentID) === Number(parentId)
        );

        if (!relation) {
            console.warn("Немає зв'язку parent-student");
            return;
        }

        const studentId = relation.studentID;

        const studentRes = await fetch(`${API_URL}/students/${studentId}`);
        const student = await studentRes.json();

        console.log("Student:", student);

        document.getElementById("childName").textContent =
            student.name ?? "Без імені дитини";

        renderGrades(student.grades || []);

    } catch (err) {
        console.error("Помилка дитини:", err);
    }
}

// таблиця
function renderGrades(grades) {
    const table = document.getElementById("gradesTable");
    table.innerHTML = "";

    if (!grades || grades.length === 0) {
        table.innerHTML = `<tr><td colspan="4">Немає оцінок</td></tr>`;
        document.getElementById("averageGrade").textContent = "";
        return;
    }

    let sum = 0;

    grades.forEach(g => {
        sum += g.value ?? 0;

        const row = `
            <tr>
                <td>${g.disciplineName ?? "-"}</td>
                <td>${g.value ?? "-"}</td>
                <td>${g.teacherName ?? "-"}</td>
                <td>${g.date ? new Date(g.date).toLocaleDateString() : "-"}</td>
            </tr>
        `;

        table.innerHTML += row;
    });

    const avg = (sum / grades.length).toFixed(2);

    document.getElementById("averageGrade").textContent =
        `Середній бал: ${avg}`;
}

// фото
document.getElementById("uploadPhoto")?.addEventListener("change", function (e) {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = function () {
        document.getElementById("profilePhoto").src = reader.result;
        localStorage.setItem(`parentPhoto_${parentId}`, reader.result);
    };

    reader.readAsDataURL(file);
});

const savedPhoto = localStorage.getItem(`parentPhoto_${parentId}`);
if (savedPhoto) {
    document.getElementById("profilePhoto").src = savedPhoto;
}



window.addEventListener("DOMContentLoaded", () => {
    loadParentProfile();
    loadChildData();

    const savedPhoto = localStorage.getItem(`parentPhoto_${parentId}`);
    if (savedPhoto) {
        document.getElementById("profilePhoto").src = savedPhoto;
    }
});