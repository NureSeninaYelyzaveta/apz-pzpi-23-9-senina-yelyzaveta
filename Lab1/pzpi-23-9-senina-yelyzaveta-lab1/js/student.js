const API_URL = "https://localhost:7033/api";

// ID студента з localStorage
const studentId = localStorage.getItem("studentId");

if (!studentId) {
    alert("Користувач не авторизований!");
    window.location.href = "login.html";
}

//      профіль
async function loadProfile() {
    try {
        const res = await fetch(`${API_URL}/students/${studentId}`);
        if (!res.ok) throw new Error("Помилка при завантаженні профілю");

        const data = await res.json();

        document.getElementById("userName").textContent = data.name;
        document.getElementById("userRole").textContent = `Група: ${data.group || "-"}`;

        if (document.getElementById("userEmail")) {
            document.getElementById("userEmail").textContent = data.contactInfo || "-";
        }

        loadGradesFromProfile(data.grades);
        loadNotificationsFromProfile(data.notifications);

    } catch (err) {
        console.error("Помилка профілю:", err);
    }
}

//               оцінки
function loadGradesFromProfile(grades) {
    const table = document.getElementById("gradesTable");
    if (!table) return;

    table.innerHTML = "";

    if (!grades || grades.length === 0) {
        table.innerHTML = `<tr><td colspan="4">Оцінок поки що немає</td></tr>`;
        return;
    }

    grades.forEach(g => {
        const row = `
            <tr>
                <td>${g.disciplineName || "-"}</td>
                <td>${g.value}</td>
                <td>${g.teacherName || "-"}</td>
                <td>${g.date ? new Date(g.date).toLocaleDateString() : "-"}</td>
            </tr>
        `;
        table.innerHTML += row;
    });
}

//                повідомлення
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

//           фото
document.getElementById("uploadPhoto")?.addEventListener("change", function (e) {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = function () {
        const img = document.getElementById("profilePhoto");
        if (img) img.src = reader.result;

        
        localStorage.setItem(`studentPhoto_${studentId}`, reader.result);
    };

    reader.readAsDataURL(file);
});

// загрузка фото
const savedPhoto = localStorage.getItem(`studentPhoto_${studentId}`);
if (savedPhoto) {
    const img = document.getElementById("profilePhoto");
    if (img) img.src = savedPhoto;
}

// опис
const userDescription = document.getElementById("userDescription");

if (userDescription) {
    userDescription.value = localStorage.getItem(`userDescription_${studentId}`) || "";

    userDescription.addEventListener("input", () => {
        localStorage.setItem(`userDescription_${studentId}`, userDescription.value);
    });
}

//                сжимання фото портф
function compressImage(file, callback) {
    const reader = new FileReader();

    reader.onload = function (event) {
        const img = new Image();
        img.src = event.target.result;

        img.onload = function () {
            const canvas = document.createElement("canvas");

            const maxWidth = 800;
            const scale = maxWidth / img.width;

            canvas.width = maxWidth;
            canvas.height = img.height * scale;

            const ctx = canvas.getContext("2d");
            ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

            const compressed = canvas.toDataURL("image/jpeg", 0.7);

            callback(compressed);
        };
    };

    reader.readAsDataURL(file);
}

//      портфоліо
const portfolioWrapper = document.getElementById("portfolioWrapper");
const addBtn = document.getElementById("addPortfolioBtn");


let portfolio = JSON.parse(localStorage.getItem(`portfolio_${studentId}`)) || [];

function renderPortfolio() {
    if (!portfolioWrapper) return;

    portfolioWrapper.innerHTML = "";

    portfolio.forEach((item, index) => {
        const div = document.createElement("div");
        div.className = "portfolio-item";

        const img = document.createElement("img");
        img.src = item.image;

        const caption = document.createElement("p");
        caption.textContent = item.text;

        const deleteBtn = document.createElement("button");
        deleteBtn.className = "delete-btn";
        deleteBtn.textContent = "×";

        deleteBtn.addEventListener("click", () => deletePortfolio(index));

        div.appendChild(img);
        div.appendChild(caption);
        div.appendChild(deleteBtn);

        portfolioWrapper.appendChild(div);
    });
}

// дод
addBtn?.addEventListener("click", () => {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "image/*";

    input.onchange = () => {
        const file = input.files[0];
        if (!file) return;

        compressImage(file, (compressedImage) => {
            const text = prompt("Введіть опис роботи:") || "";

            portfolio.push({
                image: compressedImage,
                text
            });

            localStorage.setItem(`portfolio_${studentId}`, JSON.stringify(portfolio));
            renderPortfolio();
        });
    };

    input.click();
});

// видалення
function deletePortfolio(index) {
    portfolio.splice(index, 1);
    localStorage.setItem(`portfolio_${studentId}`, JSON.stringify(portfolio));
    renderPortfolio();
}


window.addEventListener("DOMContentLoaded", () => {
    loadProfile();
    renderPortfolio();
});