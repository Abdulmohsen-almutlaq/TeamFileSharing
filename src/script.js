// Check cookies on load
window.onload = function() {
    const userId = getCookie("userId");
    if (userId) {
        showDashboard(getCookie("username"));
    }
    setupDragAndDrop();
};

function setupDragAndDrop() {
    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('fileInput');
    const filenameInput = document.getElementById('uploadFilename');
    const contentInput = document.getElementById('uploadContent');

    if (!dropZone) return;

    dropZone.addEventListener('click', () => fileInput.click());

    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.style.borderColor = '#007bff';
        dropZone.style.backgroundColor = '#f0f8ff';
    });

    dropZone.addEventListener('dragleave', () => {
        dropZone.style.borderColor = '#ccc';
        dropZone.style.backgroundColor = 'transparent';
    });

    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.style.borderColor = '#ccc';
        dropZone.style.backgroundColor = 'transparent';
        
        if (e.dataTransfer.files.length > 0) {
            handleFile(e.dataTransfer.files[0]);
        }
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files.length > 0) {
            handleFile(fileInput.files[0]);
        }
    });

    function handleFile(file) {
        filenameInput.value = file.name;
        // Store the file object globally or on the element to access it later
        document.getElementById("uploadContent").file = file;
        document.getElementById("uploadContent").value = "File selected: " + file.name + " (" + file.size + " bytes)";
        document.getElementById("uploadContent").disabled = true;
    }
}

function setCookie(name, value, days) {
    const d = new Date();
    d.setTime(d.getTime() + (days*24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

function getCookie(name) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');
    for(let i=0;i < ca.length;i++) {
        let c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function logout() {
    setCookie("userId", "", -1);
    setCookie("username", "", -1);
    setCookie("teamId", "", -1);
    location.reload();
}

function showDashboard(username) {
    document.getElementById("authSection").classList.add("hidden");
    document.getElementById("dashboardSection").classList.remove("hidden");
    document.getElementById("userNameDisplay").innerText = username;
    loadFiles();
}

async function login() {
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;
    
    const response = await fetch(`/login?email=${email}&password=${password}`, { method: 'POST' });
    const text = await response.text();
    
    if (text.startsWith("LOGIN_SUCCESS")) {
        const parts = text.split(",");
        // LOGIN_SUCCESS,id,username,teamId
        setCookie("userId", parts[1], 1);
        setCookie("username", parts[2], 1);
        setCookie("teamId", parts[3], 1);
        showDashboard(parts[2]);
    } else {
        alert(text);
    }
}

async function register() {
    const username = document.getElementById("regUsername").value;
    const email = document.getElementById("regEmail").value;
    const password = document.getElementById("regPassword").value;
    const teamId = document.getElementById("regTeamId").value;

    const response = await fetch(`/register?username=${username}&email=${email}&password=${password}&teamId=${teamId}`, { method: 'POST' });
    const text = await response.text();
    alert(text);
}

async function uploadFile() {
    const filename = document.getElementById("uploadFilename").value;
    const fileInput = document.getElementById("uploadContent");
    const file = fileInput.file;
    const uploaderId = getCookie("userId");
    const teamId = getCookie("teamId");

    if (!filename || !file) {
        alert("Please select a file");
        return;
    }

    const response = await fetch(`/upload?filename=${filename}&uploaderId=${uploaderId}&teamId=${teamId}`, {
        method: 'POST',
        body: file
    });
    const text = await response.text();
    alert(text);
    loadFiles();
}

async function loadFiles() {
    const teamId = getCookie("teamId");
    const currentUserId = getCookie("userId");
    const response = await fetch(`/files?teamId=${teamId}`);
    const text = await response.text();
    
    const list = document.getElementById("fileList");
    list.innerHTML = "";

    if (text.startsWith("Files:")) {
        const lines = text.split("\n");
        for (let i = 1; i < lines.length; i++) {
            if (lines[i].trim() === "") continue;
            // Parse line: ID: 1 | Name: report.txt | TeamID: 1 | UploaderID: 1
            const parts = lines[i].split("|");
            const idPart = parts[0].trim().split(" ")[1];
            const namePart = parts[1].trim().split(" ")[1];
            const uploaderPart = parts[3] ? parts[3].trim().split(" ")[1] : null;

            const li = document.createElement("li");
            let deleteBtn = "";
            
            if (uploaderPart === currentUserId) {
                deleteBtn = `<button onclick="deleteFile(${idPart})" style="background-color: #ef4444; margin-left: 10px; width: auto; padding: 8px 16px;">Delete</button>`;
            }

            li.innerHTML = `
                <span>${namePart}</span> 
                <div>
                    <a href="/download?fileId=${idPart}" target="_blank">Download</a>
                    ${deleteBtn}
                </div>`;
            list.appendChild(li);
        }
    } else {
        list.innerHTML = "<li>No files found or error loading files.</li>";
    }
}

async function deleteFile(fileId) {
    if (!confirm("Are you sure you want to delete this file?")) return;
    
    const userId = getCookie("userId");
    const response = await fetch(`/delete?fileId=${fileId}&userId=${userId}`, { method: 'POST' });
    const text = await response.text();
    alert(text);
    loadFiles();
}