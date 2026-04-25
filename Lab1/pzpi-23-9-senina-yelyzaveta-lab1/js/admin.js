const API_URL = "https://localhost:7033/api";

// ================== ДАННЫЕ ==================
let students = [];
let teachers = [];
let disciplines = [];
let grades = [];

// ================== INIT ==================
window.addEventListener("DOMContentLoaded", async () => {
    await loadAllData();
    calculateStats();
    renderDisciplines();
});

// ================== ЗАГРУЗКА ==================
async function loadAllData() {
    try {
        const [s, t, d, g] = await Promise.all([
            fetch(`${API_URL}/students`).then(r => r.json()),
            fetch(`${API_URL}/teachers`).then(r => r.json()),
            fetch(`${API_URL}/disciplines`).then(r => r.json()),
            fetch(`${API_URL}/grades`).then(r => r.json())
        ]);

        students = s || [];
        teachers = t || [];
        disciplines = d || [];
        grades = g || [];

    } catch (err) {
        console.error("Помилка завантаження:", err);
    }
}

// ================== СТАТИСТИКА ==================
function calculateStats() {

    const avg = grades.length
        ? (grades.reduce((a, g) => a + g.value, 0) / grades.length).toFixed(2)
        : 0;

    document.getElementById("studentsCount").textContent = students.length;
    document.getElementById("teachersCount").textContent = teachers.length;
    document.getElementById("disciplinesCount").textContent = disciplines.length;
    document.getElementById("gradesCount").textContent = grades.length;
    document.getElementById("avgGrade").textContent = avg;
}

// ================== ДОБАВИТЬ ДИСЦИПЛИНУ ==================
document.getElementById("addDisciplineBtn")?.addEventListener("click", async () => {
    const input = document.getElementById("disciplineInput"); // ❗ ВАЖНО
    const name = input?.value?.trim();

    if (!name) return;

    try {
        const res = await fetch(`${API_URL}/admin/disciplines`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },

            // backend string
            body: JSON.stringify(name)
        });

        if (!res.ok) throw new Error("API error");

        input.value = "";

        await loadAllData();
        calculateStats();
        renderDisciplines();

    } catch (err) {
        console.error(err);

        let local = JSON.parse(localStorage.getItem("adminDisciplines")) || [];

        local.push({
            id: Date.now(),
            name
        });

        localStorage.setItem("adminDisciplines", JSON.stringify(local));

        input.value = "";
        renderDisciplines();
    }
});

// ================== УДАЛЕНИЕ ==================
async function deleteDiscipline(id) {
    try {
        await fetch(`${API_URL}/admin/disciplines/${id}`, {
            method: "DELETE"
        });

        await loadAllData();
        calculateStats();
        renderDisciplines();

    } catch {
        let local = JSON.parse(localStorage.getItem("adminDisciplines")) || [];
        local = local.filter(d => d.id !== id);
        localStorage.setItem("adminDisciplines", JSON.stringify(local));
        renderDisciplines();
    }
}

// ================== ВИЗУАЛ СПИСКА ==================
function renderDisciplines() {
    const container = document.getElementById("disciplinesList");
    if (!container) return;

    const local = JSON.parse(localStorage.getItem("adminDisciplines")) || [];

    const all = [...disciplines, ...local];

    container.innerHTML = "";

    all.forEach(d => {
        const div = document.createElement("div");
        div.className = "discipline-item";

        div.innerHTML = `
            <span>${d.name}</span>
            <button onclick="deleteDiscipline(${d.disciplineID || d.id})">✕</button>
        `;

        container.appendChild(div);
    });
}