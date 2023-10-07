getUserTable();

function validatePasswords() {
    const password = document.getElementById('password').value;
    const repeatedPassword = document.getElementById('confirmedPassword').value;
    const passwordAlert = document.getElementById('passwordAlert');

    if (password !== repeatedPassword) {
        passwordAlert.classList.remove('d-none');
        return false;
    } else {
        passwordAlert.classList.add('d-none');
        return true;
    }
}

function toggleAll(source, containerId) {
    let container = document.getElementById(containerId);
    let checkboxes = container.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = source.checked;
    });
}

function getUserTable() {
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/api/user", true);
    xhr.responseType = "json";

    xhr.onload = () => {
        let status = xhr.status;
        if (status === 200 && xhr.response != null) {
            let userList = xhr.response;
            renderTable(userList);
        } else if (status !== 200 || xhr.response == null) {
            location.reload();
        } else {
            throw new Error("Could not render the table: " + status);
        }
    };
    xhr.send();
}

function renderTable(userList) {
    let defaultLocale = "en-US";
    let table = document.getElementById("user-table");
    table.innerHTML = "";

    for (let i = 0; i < userList.length; i++) {
        let status = userList[i].status;

        switch(status) {
            case "Active":
                status = "<span class=\"badge badge-success rounded-pill d-inline\">Active</span>\n";
                break;
            case "Blocked":
                status = "<span class=\"badge badge-danger rounded-pill d-inline\">Blocked</span>\n";
                break;
        }

        table.innerHTML +=
            "<tr>\n" +
            "    <td><input type=\"checkbox\" value=\"" + userList[i].id + "\"></td>\n" +
            "    <td>\n" +
            "        <div class=\"d-flex align-items-center\">\n" +
            "            <div class=\"ms-3\">\n" +
            "                <p class=\"fw-bold mb-1\">" + userList[i].fullName + "</p>\n" +
            "                <p class=\"text-muted mb-0\">" + userList[i].position + "</p>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </td>\n" +
            "    <td>\n" +
            "        <p class=\"fw-normal mb-1\">" + userList[i].email + "</p>\n" +
            "    </td>\n" +
            "    <td>\n" +
            "       <p class=\"text-muted mb-0\">" + parseDateTime(userList[i].registrationDateTime, defaultLocale) + "</p>\n" +
            "    </td>\n" +
            "    <td>\n" +
            "       <p class=\"text-muted mb-0\">" + parseDateTime(userList[i].lastSignInDateTime, defaultLocale) + "</p>\n" +
            "    </td>\n" +
            "    <td>\n"
            + status +
            "    </td>\n" +
            "</tr>"
    }
}

function performAction(action, containerId) {
    const container = document.getElementById(containerId);
    let selectedUserIds = [];
    const checkboxes = container.querySelectorAll('input[type="checkbox"]');
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            selectedUserIds.push(checkbox.value);
        }
    });

    let endpoint = "/api/user?action=";

    fetch(endpoint + action, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(selectedUserIds)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not: " + response.status);
            }
            getUserTable();
            return response.text();
        })
        .catch(error => {
            if (error.response && error.response.status === 500) {
                location.reload();
            }
        });
}

function parseDateTime(datetime, locale) {
    const options = { year: "numeric", month: "short", day: "numeric" };
    const dateTime = new Date(datetime);
    const timeString = dateTime.toLocaleTimeString(locale, { hour12: false });
    const dateString = dateTime.toLocaleDateString(locale, options);
    return `${timeString} ${dateString}`;
}