const API_BASE_URL = 'http://localhost:8080'; 

async function fetchPlayers() {
    const response = await fetch(`${API_BASE_URL}/players`);
    const players = await response.json();
    const playersList = document.getElementById('players');
    const playerSelect = document.getElementById('player-select'); 
    playersList.innerHTML = '';
    playerSelect.innerHTML = '<option value="">Select Player</option>'; 

    players.forEach(player => {
        const li = document.createElement('li');
        li.innerHTML = `ID: ${player.id}, Name: ${player.name}, Age: ${player.age}
            <button onclick="editPlayer(${player.id}, '${player.name}', ${player.age})">Edit</button>
            <button onclick="deletePlayer(${player.id})">Delete</button>`;
        playersList.appendChild(li);

      
        const option = document.createElement('option');
        option.value = player.id;
        option.textContent = `${player.name} (ID: ${player.id})`;
        playerSelect.appendChild(option);
    });
}

async function addPlayer(name, age) {
    await fetch(`${API_BASE_URL}/players`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, age }),
    });
    fetchPlayers();
    alert('Player added successfully!');
}

async function deletePlayer(id) {
    const confirmDelete = confirm("Are you sure you want to delete this player?");
    if (confirmDelete) {
        await fetch(`${API_BASE_URL}/players/${id}`, { method: 'DELETE' });
        fetchPlayers();
        alert('Player deleted successfully!');
    }
}


async function editPlayer(id, currentName, currentAge) {
    const name = prompt('Edit Name:', currentName);
    const age = prompt('Edit Age:', currentAge);

    if (name && age) {
        await fetch(`${API_BASE_URL}/players/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, age }),
        });
        fetchPlayers();
        alert('Player edited successfully!');
    }
}


async function editStats(id, currentPlayerId, currentScore, currentGamesPlayed) {
    const playerId = prompt('Edit Player ID:', currentPlayerId);
    const score = prompt('Edit Score:', currentScore);
    const gamesPlayed = prompt('Edit Games Played:', currentGamesPlayed);

    if (playerId && score && gamesPlayed) {
        await fetch(`${API_BASE_URL}/stats/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ playerId, score, gamesPlayed }),
        });
        fetchStats();
        alert('Player stats edited successfully!');
    }
}



async function fetchStats() {
    const response = await fetch(`${API_BASE_URL}/stats`);
    
    
    const rawText = await response.text();
    console.log('Response text:', rawText);  

    
    let stats = [];
    try {
        stats = JSON.parse(rawText);
    } catch (error) {
        console.error('Error parsing response:', error);
        alert('There was an issue with the response from the server.');
        return;
    }

    const statsList = document.getElementById('stats');
    statsList.innerHTML = '';

    stats.forEach(stat => {
        console.log(stat);  

        
        const score = stat.score !== undefined ? stat.score : 'N/A';
        const gamesPlayed = stat.gamesPlayed !== undefined ? stat.gamesPlayed : 'N/A';
        const playerId = stat.playerId !== undefined ? stat.playerId : 'N/A';

        const li = document.createElement('li');
        li.innerHTML = `
            ID: ${stat.id}, Player ID: ${playerId}, Score: ${score}, Games Played: ${gamesPlayed}
            <button onclick="editStats(${stat.id}, ${playerId}, ${score}, ${gamesPlayed})">Edit</button>
            <button onclick="deleteStats(${stat.id})">Delete</button>
        `;
        statsList.appendChild(li);
    });
}





async function addStats(playerId, score, gamesPlayed) {
    
    console.log('Player ID:', playerId);
    console.log('Score:', score);
    console.log('Games Played:', gamesPlayed);

    
    if (isNaN(score) || isNaN(gamesPlayed) || !playerId) {
        alert('Please ensure all fields are filled correctly!');
        return;
    }

    await fetch(`${API_BASE_URL}/stats`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            player: { id: playerId },  
            score: score,  
            gamesPlayed: gamesPlayed  
        }),
    });
    fetchStats();
    alert('Player stats added successfully!');
}






async function deleteStats(id) {
    const confirmDelete = confirm("Are you sure you want to delete these stats?");
    if (confirmDelete) {
        await fetch(`${API_BASE_URL}/stats/${id}`, { method: 'DELETE' });
        fetchStats();
        alert('Player stats deleted successfully!');
    }
}

document.getElementById('player-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const name = document.getElementById('player-name').value;
    const age = parseInt(document.getElementById('player-age').value, 10);

    await addPlayer(name, age);
});




document.getElementById('stats-form').addEventListener('submit', (e) => {
    e.preventDefault();

    const playerId = parseInt(document.getElementById('player-select').value, 10);
    const score = parseInt(document.getElementById('player-score').value, 10);
    const gamesPlayed = parseInt(document.getElementById('games-played').value, 10);

  
    console.log('Player ID:', playerId);
    console.log('Score:', score);
    console.log('Games Played:', gamesPlayed);

   
    addStats(playerId, score, gamesPlayed);
});



document.addEventListener('DOMContentLoaded', () => {
    fetchPlayers();
    fetchStats();
});
