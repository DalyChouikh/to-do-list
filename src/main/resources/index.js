const app = document.getElementById('todo_app');
const tasks = fetch('http://localhost:8082/api/v1/tasks', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        }
    }).then(response => response.json())
    .then(data => {
        const taskList = data.map(task => {
            return `<li>${task.id} - ${task.description}</li>`;
        }).join('');
        app.innerHTML = `<ul>${taskList}</ul>`;
    });