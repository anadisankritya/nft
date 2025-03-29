async function fetchUserLevels(page, size) {
    const response = await fetch(`/nft/api/user-level/list?page=${page}&size=${size}`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function fetchAllUserLevels() {
    const response = await fetch(`/nft/api/user-level/list`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    const data = response.json();
    return data;
}

async function fetchUserLevelById(id) {
    const response = await fetch(`/nft/api/user-level/${id}`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function createOrUpdateUserLevel(method, payload) {
    const url = method === 'POST' ? '/nft/api/user-level/create' : '/nft/api/user-level/update';
    const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json', 'ngrok-skip-browser-warning': 'true' },
        body: JSON.stringify(payload)
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function deleteUserLevel(id) {
    const response = await fetch(`/nft/api/user-level/${id}`, {
        method: 'DELETE',
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}